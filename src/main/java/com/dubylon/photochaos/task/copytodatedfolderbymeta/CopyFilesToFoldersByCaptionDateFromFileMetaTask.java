package com.dubylon.photochaos.task.copytodatedfolderbymeta;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.dubylon.photochaos.model.operation.*;
import com.dubylon.photochaos.model.tasktemplate.TaskTemplateParameterType;
import com.dubylon.photochaos.report.TableReport;
import com.dubylon.photochaos.report.TableReportRow;
import com.dubylon.photochaos.task.*;
import com.dubylon.photochaos.util.FileSystemUtil;
import com.dubylon.photochaos.util.Pair;
import com.dubylon.photochaos.util.ReportUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Stream;

import static com.dubylon.photochaos.report.TableReport.*;

@PcoTaskTemplate(languageKeyPrefix = "task.copyFilesByDateFromFileMeta.")
public class CopyFilesToFoldersByCaptionDateFromFileMetaTask extends AbstractFileSystemTask {

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

  private String knownGlobFilter;
  private Path sourcePath;
  private Path destinationPath;
  private List<Path> pathList;
  private Set<String> createdFolders;
  private DateTimeFormatter dateFormatter;

  public CopyFilesToFoldersByCaptionDateFromFileMetaTask() {

  }

  private Pair<Instant, Exception> readFileDateFromMetadata(Path path) {
    Instant captureInstant = null;
    File imageFile = path.toFile();
    Metadata metadata = null;
    //read metadata
    try {
      metadata = ImageMetadataReader.readMetadata(imageFile);
    } catch (IOException | ImageProcessingException e) {
      return new Pair(null, e);
    }

    if (metadata != null) {
      final Directory exifSubIFDDirectory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
      final ExifIFD0Directory exifIFD0Directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);

      // read ExifIFD0Directory/ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL
      if (exifSubIFDDirectory != null) {
        if (exifSubIFDDirectory.containsTag(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL)) {
          Date dto = exifSubIFDDirectory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
          if (dto != null) {
            captureInstant = dto.toInstant();
          }
        }
      }

      // read ExifIFD0Directory/ExifIFD0Directory.TAG_DATETIME
      if (captureInstant == null) {
        if (exifIFD0Directory != null) {
          if (exifIFD0Directory.containsTag(ExifIFD0Directory.TAG_DATETIME)) {
            Date dto = exifIFD0Directory.getDate(ExifIFD0Directory.TAG_DATETIME);
            if (dto != null) {
              captureInstant = dto.toInstant();
            }
          }
        }
      }
    }

    // fall back to file creation date
    if (captureInstant == null) {
      try {
        BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
        FileTime fileTime = attr.creationTime();
        if (fileTime != null) {
          captureInstant = fileTime.toInstant();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return new Pair(captureInstant, null);
  }

  private void createOperation(Path currentPath, List<FilesystemOperation> fsol) {
    final PathMatcher filter = currentPath.getFileSystem().getPathMatcher(knownGlobFilter);
    try (final Stream<Path> stream = Files.list(currentPath)) {
      stream
          .filter(path -> path.toFile().isFile() && filter.matches(path.getFileName()))
          .forEach(path -> {

            /*try {
              System.out.println("Create operation");
              Thread.sleep(200);
            } catch (Exception e) {
              System.out.println("Not able to sleep");
            }*/

            Pair<Instant, Exception> metaDate = readFileDateFromMetadata(path);

            Instant captureInstant = metaDate.getLeft();

            Path namePath = path.getFileName();
            Path newPath = null;
            final FilesystemOperation fileOp;
            if (captureInstant != null) {
              String targetDateFolderName = dateFormatter.format(captureInstant) + newFolderSuffix;
              Path targetDatePath = Paths.get(targetDateFolderName);
              newPath = destinationPath.resolve(targetDatePath);
              String newPathString = newPath.toString();
              final FilesystemOperation folderOp;
              if (newPath.toFile().exists() || createdFolders.contains(newPathString)) {
                folderOp = new FolderAlreadyPresent(newPath);
              } else {
                folderOp = new CreateFolder(destinationPath, targetDatePath);
                createdFolders.add(newPathString);
              }
              fsol.add(folderOp);
            }
            if (TaskTemplateParameterCopyOrMove.COPY == fileOperation) {
              fileOp = new CopyFile(namePath, currentPath, newPath);
            } else {
              fileOp = new MoveFile(namePath, currentPath, newPath);
            }
            fileOp.setException(metaDate.getRight());
            fsol.add(fileOp);
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
    if (sourceFolderOk && destinationFolderOk) {
      TableReportRow row1 = phaseReport.createRow();
      row1.set(FSOP_PHASE, "detectingFolders");
      pathList = FileSystemUtil.getAllSubfoldersIncluding(sourcePath);
    } else {
      pathList = new ArrayList<>();
    }

    TableReportRow row2 = phaseReport.createRow();
    row2.set(FSOP_PHASE, "detectingFiles");
    // Create the operation list
    List<FilesystemOperation> fsOpList = new ArrayList<>();
    createdFolders = new HashSet<>();
    dateFormatter = DateTimeFormatter.ofPattern(newFolderDateFormat).withZone(ZoneOffset.UTC);

    knownGlobFilter = buildKnownGlobFilter();

    pathList.forEach(path -> this.createOperation(path, fsOpList));

    // Create the operation report
    TableReport opReport = ReportUtil.buildOperationReport();
    status.getReports().add(opReport);

    TableReportRow row3 = phaseReport.createRow();
    row3.set(FSOP_PHASE, "workingOnFiles");
    executeOperations(fsOpList, opReport, sourcePath, destinationPath, previewOrRun);
  }
}
