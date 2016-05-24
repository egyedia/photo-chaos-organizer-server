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
public class CopyFilesToFoldersByCaptionDateFromFileMetaTask extends AbstractPcoTask {

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

  private boolean performOperations;
  private String knownGlobFilter;
  private Path sourcePath;
  private Path destinationPath;
  private List<Path> pathList;
  private Set<String> createdFolders;
  private DateTimeFormatter dateFormatter;

  public CopyFilesToFoldersByCaptionDateFromFileMetaTask() {

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
    final PathMatcher filter = currentPath.getFileSystem().getPathMatcher(knownGlobFilter);
    try (final Stream<Path> stream = Files.list(currentPath)) {
      stream
          .filter(path -> path.toFile().isFile() && filter.matches(path.getFileName()))
          .forEach(path -> {

            File imageFile = path.toFile();
            Metadata metadata = null;
            try {
              metadata = ImageMetadataReader.readMetadata(imageFile);
            } catch (IOException e) {
              e.printStackTrace();
            } catch (ImageProcessingException e) {
              e.printStackTrace();
            }

            Instant captureInstant = null;

            if (metadata != null) {
              final Directory exifSubIFDDirectory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
              final ExifIFD0Directory exifIFD0Directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);

              if (exifSubIFDDirectory != null) {
                if (exifSubIFDDirectory.containsTag(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL)) {
                  Date dto = exifSubIFDDirectory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
                  if (dto != null) {
                    captureInstant = dto.toInstant();
                  }
                }
              }

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

            if (captureInstant != null) {
              Path namePath = path.getFileName();
              String targetDateFolderName = dateFormatter.format(captureInstant) + newFolderSuffix;
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
              System.out.println("unable to determine file date for:" + path);
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

    knownGlobFilter = buildKnownGlobFilter();

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
}
