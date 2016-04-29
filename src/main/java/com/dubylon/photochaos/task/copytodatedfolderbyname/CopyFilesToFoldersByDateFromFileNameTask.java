package com.dubylon.photochaos.task.copytodatedfolderbyname;

import com.dubylon.photochaos.model.operation.*;
import com.dubylon.photochaos.model.tasktemplate.TaskTemplateParameterType;
import com.dubylon.photochaos.report.TableReport;
import com.dubylon.photochaos.report.TableReportRow;
import com.dubylon.photochaos.task.*;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static com.dubylon.photochaos.report.TableReport.*;

@PcoTaskTemplate(languageKeyPrefix = "task.copyFilesByDateFromFileName.")
public class CopyFilesToFoldersByDateFromFileNameTask extends AbstractPcoTask {

  @PcoTaskTemplateParameter(
      type = TaskTemplateParameterType.PATH,
      mandatory = true,
      defaultValue = "",
      order = 1
  )
  private String sourceFolder;

  @PcoTaskTemplateParameter(
      type = TaskTemplateParameterType.PATH,
      mandatory = true,
      defaultValue = "",
      order = 2
  )
  private String destinationFolder;

  @PcoTaskTemplateParameter(
      type = TaskTemplateParameterType.FOLDERSUFFIX,
      mandatory = false,
      defaultValue = "",
      order = 3
  )
  private String newFolderSuffix;

  @PcoTaskTemplateParameter(
      type = TaskTemplateParameterType.SHORTDATEFORMAT,
      mandatory = true,
      defaultValue = "uuuuMMdd",
      order = 4
  )
  private String newFolderDateFormat;

  @PcoTaskTemplateParameter(
      type = TaskTemplateParameterType.COPYORMOVE,
      mandatory = true,
      defaultValue = "copy",
      order = 5
  )
  private TaskTemplateParameterCopyOrMove fileOperation;

  //private String targetFolderNameFormatter;
  private String fullDateTimePatternString;
  private String justDatePatternString;
  private Pattern fullDateTimePattern;
  private Pattern justDatePattern;

  private boolean performOperations;

  private Path sourcePath;
  private Path destinationPath;
  private List<Path> pathList;
  private Set<String> createdFolders;
  private DateTimeFormatter dateFormatter;


  public CopyFilesToFoldersByDateFromFileNameTask() {
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
            if (dateTime == null) {
              dateTime = extractDate(fileName);
            }
            if (dateTime != null) {
              LocalDateTime fileDateTime = null;
              try {
                fileDateTime = LocalDateTime.of(dateTime.getYear(), dateTime.getMonth(), dateTime.getDay(), dateTime
                    .getHour(), dateTime.getMinute(), dateTime.getSecond());
              } catch (DateTimeException ex) {
                ex.printStackTrace();
              }
              if (fileDateTime != null) {
                String targetDateFolderName = dateFormatter.format(fileDateTime) + newFolderSuffix;
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
              } else {
                System.out.println("Error while determining file date for:" + path);
              }
            } else {
              System.out.println("Unable to determine file date for:" + path);
            }
          });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void execute(boolean performOperations) {
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
    dateFormatter = DateTimeFormatter.ofPattern(newFolderDateFormat).withZone(ZoneOffset.UTC);

    pathList.forEach(path -> this.createOperation(path, fsOpList));

    // Create the operation report
    TableReport opReport = new TableReport();
    status.getReports().add(opReport);
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
