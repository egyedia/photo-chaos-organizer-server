package com.dubylon.photochaos.task.copytodatedfolderbyname;

import com.dubylon.photochaos.model.operation.*;
import com.dubylon.photochaos.model.tasktemplate.TaskTemplateParameterType;
import com.dubylon.photochaos.report.TableReport;
import com.dubylon.photochaos.report.TableReportRow;
import com.dubylon.photochaos.task.*;
import com.dubylon.photochaos.util.FileNameDateUtil;
import com.dubylon.photochaos.util.FileSystemUtil;
import com.dubylon.photochaos.util.Pair;
import com.dubylon.photochaos.util.ReportUtil;
import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static com.dubylon.photochaos.report.TableReport.FSOP_PHASE;
import static com.dubylon.photochaos.report.TableReport.FSOP_PHASE_VALUE;

@PcoTaskTemplate(languageKeyPrefix = "task.copyFilesByDateFromFileName.")
public class CopyFilesToFoldersByDateFromFileNameTask extends AbstractFileSystemTask {

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

  private String knownGlobFilter;
  private Path sourcePath;
  private Path destinationPath;
  private List<Path> pathList;
  private Set<String> createdFolders;
  private DateTimeFormatter dateFormatter;

  public CopyFilesToFoldersByDateFromFileNameTask() {
  }

  private Pair<LocalDateTime, Exception> readFileDateFromFileName(Path path) {
    LocalDateTime fileDateTime = null;

    Path namePath = path.getFileName();
    String name = namePath.toString();
    String fileName = FilenameUtils.getBaseName(name);
    DateTimeBean dateTime = FileNameDateUtil.extractDateAndTime(fileName);
    if (dateTime == null) {
      dateTime = FileNameDateUtil.extractDate(fileName);
    }
    if (dateTime != null) {
      try {
        fileDateTime = LocalDateTime.of(dateTime.getYear(), dateTime.getMonth(), dateTime.getDay(), dateTime
            .getHour(), dateTime.getMinute(), dateTime.getSecond());
      } catch (DateTimeException ex) {
        return new Pair(null, ex);
      }
    } else {
      return new Pair(null, new IllegalArgumentException("Unable to determine file date"));
    }
    return new Pair(fileDateTime, null);
  }

  private void createOperation(Path currentPath, List<FilesystemOperation> fsol, TableReportRow row,
                               Consumer<TableReportRow> action) {
    final PathMatcher filter = currentPath.getFileSystem().getPathMatcher(knownGlobFilter);
    try (final Stream<Path> stream = Files.list(currentPath)) {
      stream
          .filter(path -> path.toFile().isFile() && filter.matches(path.getFileName()))
          .forEach(path -> {

            Pair<LocalDateTime, Exception> dateResult = readFileDateFromFileName(path);

            LocalDateTime fileDateTime = dateResult.getLeft();

            if (fileDateTime != null) {
              String targetDateFolderName = dateFormatter.format(fileDateTime) + newFolderSuffix;
              Path targetDatePath = Paths.get(targetDateFolderName);
              Path newPath = destinationPath.resolve(targetDatePath);
              String newPathString = newPath.toString();
              final FilesystemOperation folderOp;
              if (newPath.toFile().exists() || createdFolders.contains(newPathString)) {
                folderOp = new FolderAlreadyPresent(newPath);
              } else {
                folderOp = new CreateFolder(destinationPath, targetDatePath);
                createdFolders.add(newPathString);
              }
              fsol.add(folderOp);
              if (folderOp.isDoingSomething()) {
                action.accept(row);
              }

              Path namePath = path.getFileName();
              final FilesystemOperation fileOp;
              if (TaskTemplateParameterCopyOrMove.COPY == fileOperation) {
                fileOp = new CopyFile(namePath, currentPath, newPath);
              } else {
                fileOp = new MoveFile(namePath, currentPath, newPath);
              }
              fsol.add(fileOp);
              if (fileOp.isDoingSomething()) {
                action.accept(row);
              }
            } else {
              Path namePath = path.getFileName();
              final FilesystemOperation fileOp = new UnableToProcessFile(namePath, currentPath);
              fileOp.setStatus(PcoOperationStatus.ERROR);
              fileOp.setException(dateResult.getRight());
              fsol.add(fileOp);
              action.accept(row);
            }
          });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void execute(PreviewOrRun previewOrRun) {
    sourcePath = Paths.get(sourceFolder);
    destinationPath = Paths.get(destinationFolder);

    // Check source folder
    boolean sourceFolderOk = FileSystemUtil.isDirectoryAndReadable(sourcePath);

    // Check destination folder
    boolean destinationFolderOk = FileSystemUtil.isDirectoryAndWritable(destinationPath);

    TableReport phaseReport = ReportUtil.buildPhaseReport();
    status.getReports().add(phaseReport);

    // Detect all subfolders
    TableReportRow row1 = phaseReport.createRow();
    row1.set(FSOP_PHASE, "detectingFolders");
    if (sourceFolderOk && destinationFolderOk) {
      row1.set(FSOP_PHASE_VALUE, 0);
      pathList = FileSystemUtil.getAllSubfoldersIncluding(sourcePath, row1, row -> {
        row.set(FSOP_PHASE_VALUE, (Integer) row.get(FSOP_PHASE_VALUE) + 1);
      });
    } else {
      pathList = new ArrayList<>();
      row1.setStatus(PcoOperationStatus.ERROR);
      if (!sourceFolderOk) {
        row1.set(FSOP_PHASE_VALUE, "sourceFolderError");
      } else if (!destinationFolderOk) {
        row1.set(FSOP_PHASE_VALUE, "destinationFolderError");
      }
    }

    // Create the operation list
    TableReportRow row2 = phaseReport.createRow();
    row2.set(FSOP_PHASE, "creatingOperationList");
    row2.set(FSOP_PHASE_VALUE, 0);
    List<FilesystemOperation> fsOpList = new ArrayList<>();
    createdFolders = new HashSet<>();
    dateFormatter = DateTimeFormatter.ofPattern(newFolderDateFormat).withZone(ZoneOffset.UTC);

    knownGlobFilter = buildKnownGlobFilter();

    pathList.forEach(path -> this.createOperation(path, fsOpList, row2, row -> {
      row.set(FSOP_PHASE_VALUE, (Integer) row.get(FSOP_PHASE_VALUE) + 1);
    }));

    // Create the operation report
    TableReport opReport = ReportUtil.buildOperationReport();
    status.getReports().add(opReport);

    // Execute the operation list
    TableReportRow row3 = phaseReport.createRow();
    row3.set(FSOP_PHASE, "executingOperations");
    row3.set(FSOP_PHASE_VALUE, 0);
    executeOperations(fsOpList, opReport, sourcePath, destinationPath, previewOrRun, row3, row -> {
      row.set(FSOP_PHASE_VALUE, (Integer) row.get(FSOP_PHASE_VALUE) + 1);
    });
  }

}
