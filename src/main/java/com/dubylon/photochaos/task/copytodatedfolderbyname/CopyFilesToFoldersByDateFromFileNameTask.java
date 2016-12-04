package com.dubylon.photochaos.task.copytodatedfolderbyname;

import com.dubylon.photochaos.model.operation.*;
import com.dubylon.photochaos.model.tasktemplate.TaskTemplateParameterType;
import com.dubylon.photochaos.report.TableReport;
import com.dubylon.photochaos.task.*;
import com.dubylon.photochaos.util.FileNameDateUtil;
import com.dubylon.photochaos.util.FileSystemUtil;
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
import java.util.stream.Stream;

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

  private LocalDateTime readFileDateFromFileName(Path path) {
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
        System.out.println(ex.getMessage());
        //ex.printStackTrace();
      }
    } else {
      System.out.println("Unable to determine file date for:" + path);
    }
    return fileDateTime;
  }

  private void createOperation(Path currentPath, List<FilesystemOperation> fsol) {
    final PathMatcher filter = currentPath.getFileSystem().getPathMatcher(knownGlobFilter);
    try (final Stream<Path> stream = Files.list(currentPath)) {
      stream
          .filter(path -> path.toFile().isFile() && filter.matches(path.getFileName()))
          .forEach(path -> {

            LocalDateTime fileDateTime = readFileDateFromFileName(path);

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

              Path namePath = path.getFileName();
              final FilesystemOperation fileOp;
              if (TaskTemplateParameterCopyOrMove.COPY == fileOperation) {
                fileOp = new CopyFile(namePath, currentPath, newPath);
              } else {
                fileOp = new MoveFile(namePath, currentPath, newPath);
              }
              fsol.add(fileOp);
            } else {
              System.out.println("Error while determining file date for:" + path);
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

    // Detect all subfolders
    if (sourceFolderOk && destinationFolderOk) {
      pathList = FileSystemUtil.getAllSubfoldersIncluding(sourcePath);
    } else {
      pathList = new ArrayList<>();
    }

    // Create the operation list
    List<FilesystemOperation> fsOpList = new ArrayList<>();
    createdFolders = new HashSet<>();
    dateFormatter = DateTimeFormatter.ofPattern(newFolderDateFormat).withZone(ZoneOffset.UTC);

    knownGlobFilter = buildKnownGlobFilter();

    pathList.forEach(path -> this.createOperation(path, fsOpList));

    // Create the operation report
    TableReport opReport = ReportUtil.buildOperationReport();
    status.getReports().add(opReport);

    executeOperations(fsOpList, opReport, sourcePath, destinationPath, previewOrRun);
  }

}
