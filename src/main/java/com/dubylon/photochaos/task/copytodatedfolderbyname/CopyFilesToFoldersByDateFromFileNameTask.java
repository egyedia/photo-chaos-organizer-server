package com.dubylon.photochaos.task.copytodatedfolderbyname;

import com.dubylon.photochaos.Defaults;
import com.dubylon.photochaos.app.CopyDatedFolderTaskConfig;
import com.dubylon.photochaos.model.operation.*;
import com.dubylon.photochaos.model.tasktemplate.TaskTemplateParameterType;
import com.dubylon.photochaos.report.TableReport;
import com.dubylon.photochaos.report.TableReportRow;
import com.dubylon.photochaos.rest.task.TaskPreviewOrRunGetData;
import com.dubylon.photochaos.task.*;
import com.dubylon.photochaos.util.PhotoChaosFileType;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static com.dubylon.photochaos.report.TableReport.*;
import static com.dubylon.photochaos.report.TableReport.FSOP_DESTINATION_NAME;
import static com.dubylon.photochaos.report.TableReport.FSOP_STATUS;

@PcoTaskTemplate(languageKeyPrefix = "task.copyFilesByDateFromFileName.")
public class CopyFilesToFoldersByDateFromFileNameTask implements IPcoTask {

  @PcoTaskTemplateParameter(
      type = TaskTemplateParameterType.PATH,
      mandatory = true,
      defaultValue = ""
  )
  private String sourceFolder;

  @PcoTaskTemplateParameter(
      type = TaskTemplateParameterType.PATH,
      mandatory = true,
      defaultValue = ""
  )
  private String destinationFolder;

  @PcoTaskTemplateParameter(
      type = TaskTemplateParameterType.FOLDERSUFFIX,
      mandatory = true,
      defaultValue = ""
  )
  private String newFolderSuffix;

  @PcoTaskTemplateParameter(
      type = TaskTemplateParameterType.SHORTDATEFORMAT,
      mandatory = true,
      defaultValue = "yyyyMMdd"
  )
  private String newFolderDateFormat;

  @PcoTaskTemplateParameter(
      type = TaskTemplateParameterType.COPYORMOVE,
      mandatory = true,
      defaultValue = "copy"
  )
  private TaskTemplateParameterCopyOrMove fileOperation;

  //private String targetFolderNameFormatter;
  private String fullDateTimePatternString;
  private String justDatePatternString;
  private Pattern fullDateTimePattern;
  private Pattern justDatePattern;

  private TaskPreviewOrRunGetData response;
  private boolean performOperations;

  private Path sourcePath;
  private Path destinationPath;
  private List<Path> pathList;
  private Set<String> createdFolders;
  private SimpleDateFormat dateFormat;
  private TimeZone timeZone = TimeZone.getTimeZone("UTC");


  public CopyFilesToFoldersByDateFromFileNameTask() {
    //this.targetFolderNameFormatter = "%1$04d%2$02d%3$02d%4$s";
    this.fullDateTimePatternString = "([^\\d]*)([\\d]{2,4})([^\\d]*)([\\d]{1,2})([^\\d]*)([\\d]{1,2})" +
        "([^\\d]*)([\\d]{1,2})([^\\d]*)([\\d]{1,2})([^\\d]*)([\\d]{1,2})([^\\d]*)";
    this.justDatePatternString = "([^\\d]*)([\\d]{2,4})([^\\d]*)([\\d]{1,2})([^\\d]*)([\\d]{1,2})([^\\d]*)";
    this.fullDateTimePattern = Pattern.compile(fullDateTimePatternString);
    this.justDatePattern = Pattern.compile(justDatePatternString);
  }

  private void detectPaths(Path currentPath) {
    try (final Stream<Path> stream = Files.list(currentPath)) {
      stream
          .filter(path -> path.toFile().isDirectory())
          .forEach(path -> {
            pathList.add(path);
            detectPaths(path);
          });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  private void createOperation(Path currentPath, List<IFilesystemOperation> fsol) {
    try (final Stream<Path> stream = Files.list(currentPath)) {
      stream
          .filter(path -> path.toFile().isFile())
          .forEach(path -> {
            Path namePath = path.getFileName();
            String name = namePath.toString();
            String fileName = FilenameUtils.getBaseName(name);
            DateTimeBean dateTime = extractDateAndTime(fileName);
            String targetDateFolderName = null;
            if (dateTime == null) {
              dateTime = extractDate(fileName);
            }
            if (dateTime != null) {
              Calendar calendar = Calendar.getInstance(timeZone);
              calendar.set(Calendar.YEAR, dateTime.getYear());
              calendar.set(Calendar.MONTH, dateTime.getMonth() - 1);
              calendar.set(Calendar.DAY_OF_MONTH, dateTime.getDay());
              calendar.set(Calendar.HOUR, dateTime.getHour());
              calendar.set(Calendar.MINUTE, dateTime.getMinute());
              calendar.set(Calendar.SECOND, dateTime.getSecond());
              targetDateFolderName = dateFormat.format(calendar.getTime()) + newFolderSuffix;
              Path targetDatePath = Paths.get(targetDateFolderName);
              Path newPath = destinationPath.resolve(targetDatePath);
              String newPathString = newPath.toString();
              final IFilesystemOperation folderOp;
              if (newPath.toFile().exists() || createdFolders.contains(newPathString)) {
                folderOp = new FolderAlreadyPresent(newPath);
              } else {
                folderOp = new CreateFolder(destinationPath, targetDatePath);
                createdFolders.add(newPathString);
              }
              fsol.add(folderOp);

              final IFilesystemOperation fileOp;
              if (TaskTemplateParameterCopyOrMove.COPY == fileOperation) {
                fileOp = new CopyFile(namePath, currentPath, newPath);
              } else {
                fileOp = new MoveFile(namePath, currentPath, newPath);
              }
              fsol.add(fileOp);
            }
          });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void execute(TaskPreviewOrRunGetData response, boolean performOperations) {
    this.response = response;
    this.performOperations = performOperations;

    sourcePath = Paths.get(sourceFolder);
    destinationPath = Paths.get(destinationFolder);

    pathList = new ArrayList<>();

    // Check source folder
    File sourceFolderFile = sourcePath.toFile();
    boolean sourceFolderOk = sourceFolderFile.exists()
        && sourceFolderFile.isDirectory()
        && sourceFolderFile.canRead();

    // Check destination folder
    File destinationFolderFile = destinationPath.toFile();
    boolean destinationFolderOk = destinationFolderFile.exists()
        && destinationFolderFile.isDirectory()
        && destinationFolderFile.canWrite();

    // Detect all subfolders
    if (sourceFolderOk && destinationFolderOk) {
      pathList.add(sourcePath);
      detectPaths(sourcePath);
    }

    // Create the operation list
    List<IFilesystemOperation> fsOpList = new ArrayList<>();
    createdFolders = new HashSet<>();
    dateFormat = new SimpleDateFormat(newFolderDateFormat);
    dateFormat.setTimeZone(timeZone);
    pathList.forEach(path -> this.createOperation(path, fsOpList));

    // Create the operation report
    TableReport opReport = new TableReport();
    opReport.addHeader(FSOP_OPERATION);
    opReport.addHeader(FSOP_SOURCE);
    opReport.addHeader(FSOP_SOURCE_NAME);
    opReport.addHeader(FSOP_DESTINATION);
    opReport.addHeader(FSOP_DESTINATION_NAME);
    opReport.addHeader(FSOP_STATUS);
    fsOpList.forEach(op -> {
      FilesystemOperationPerformer.perform(op, performOperations);

      TableReportRow row = opReport.createRow();
      row.set(FSOP_OPERATION, op.getType());
      row.set(FSOP_SOURCE, op.getSource() == null ? null : sourcePath.relativize(op.getSource()));
      row.set(FSOP_SOURCE_NAME, op.getSourceName());
      row.set(FSOP_DESTINATION, op.getDestination() == null ? null : destinationPath.relativize(op.getDestination()));
      row.set(FSOP_DESTINATION_NAME, op.getDestinationName());
      row.set(FSOP_STATUS, op.getStatus());
    });

    response.getReports().add(opReport);

  }

  private DateTimeBean extractDateAndTime(String fileName) {
    DateTimeBean dtb = null;
    Matcher m = fullDateTimePattern.matcher(fileName);
    if (m.find()) {
      dtb = new DateTimeBean();
      fillDateTimeBean(dtb, m);
    }
    return dtb;
  }

  private DateTimeBean extractDate(String fileName) {
    DateTimeBean dtb = null;
    Matcher m = justDatePattern.matcher(fileName);
    if (m.find()) {
      dtb = new DateTimeBean();
      fillDate(dtb, m);
    }
    return dtb;
  }

  private static void fillDateTimeBean(DateTimeBean dtb, Matcher m) {
    dtb.setYear(getSafeIntValue(m.group(2)));
    dtb.setMonth(getSafeIntValue(m.group(4)));
    dtb.setDay(getSafeIntValue(m.group(6)));
    dtb.setHour(getSafeIntValue(m.group(8)));
    dtb.setMinute(getSafeIntValue(m.group(10)));
    dtb.setSecond(getSafeIntValue(m.group(12)));
  }

  private static void fillDate(DateTimeBean dtb, Matcher m) {
    dtb.setYear(getSafeIntValue(m.group(2)));
    dtb.setMonth(getSafeIntValue(m.group(4)));
    dtb.setDay(getSafeIntValue(m.group(6)));
  }

  private static int getSafeIntValue(String s) {
    try {
      return Integer.parseInt(s);
    } catch (NumberFormatException ex) {
      return 0;
    }
  }
}
