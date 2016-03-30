package com.dubylon.photochaos.task.moverawtosubfolder;

import com.dubylon.photochaos.Defaults;
import com.dubylon.photochaos.model.operation.*;
import com.dubylon.photochaos.model.tasktemplate.TaskTemplateParameterType;
import com.dubylon.photochaos.report.TableReport;
import com.dubylon.photochaos.report.TableReportRow;
import com.dubylon.photochaos.rest.task.TaskPreviewOrRunGetData;
import com.dubylon.photochaos.task.FilesystemOperationPerformer;
import com.dubylon.photochaos.task.IPcoTask;
import com.dubylon.photochaos.task.PcoTaskTemplate;
import com.dubylon.photochaos.task.PcoTaskTemplateParameter;
import com.dubylon.photochaos.util.PhotoChaosFileType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

import static com.dubylon.photochaos.report.TableReport.*;

@PcoTaskTemplate(languageKeyPrefix = "task.moveRawImagesToSubfolders.")
public class MoveRawImagesToSubfolderTask implements IPcoTask {

  @PcoTaskTemplateParameter(
      type = TaskTemplateParameterType.PATH,
      mandatory = true,
      defaultValue = ""
  )
  private String workingFolder;

  @PcoTaskTemplateParameter(
      type = TaskTemplateParameterType.FOLDERNAME,
      mandatory = true,
      defaultValue = "raw"
  )
  private String rawFolder;
  private Path rawPath;

  private TaskPreviewOrRunGetData response;
  private boolean performOperations;
  private String rawGlobFilter;

  private List<Path> pathList;

  public MoveRawImagesToSubfolderTask() {
  }

  private void detectPaths(Path currentPath) {
    try (final Stream<Path> stream = Files.list(currentPath)) {
      stream
          .filter(path -> path.toFile().isDirectory() && !rawFolder.equalsIgnoreCase(path.getFileName().toString()))
          .forEach(path -> {
            pathList.add(path);
            detectPaths(path);
          });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  private void createOperation(Path currentPath, List<IFilesystemOperation> fsol) {
    final IFilesystemOperation folderOp;
    Path newPath = currentPath.resolve(rawPath);
    if (newPath.toFile().exists()) {
      if (newPath.toFile().isDirectory()) {
        folderOp = new FolderAlreadyPresent(newPath);
      } else {
        folderOp = new NotFolderAlreadyPresent(newPath);
        return;
      }
    } else {
      folderOp = new CreateFolder(currentPath, rawPath);
    }

    final AtomicBoolean folderAlreadyCreated = new AtomicBoolean();
    folderAlreadyCreated.set(false);
    final PathMatcher filter = currentPath.getFileSystem().getPathMatcher(rawGlobFilter);
    try (final Stream<Path> stream = Files.list(currentPath)) {
      stream
          .filter(path -> path.toFile().isFile() && filter.matches(path.getFileName()))
          .forEach(path -> {
            if (!folderAlreadyCreated.get()) {
              fsol.add(folderOp);
              folderAlreadyCreated.set(true);
            }
            IFilesystemOperation moveOp = new MoveFile(path.getFileName(), currentPath, newPath);
            fsol.add(moveOp);
          });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void execute(TaskPreviewOrRunGetData response, boolean performOperations) {
    this.response = response;
    this.performOperations = performOperations;

    rawPath = Paths.get(rawFolder);

    pathList = new ArrayList<>();
    // Check working folder
    Path workingFolderPath = Paths.get(workingFolder);
    File workingFolderFile = workingFolderPath.toFile();
    boolean workingFolderOk = workingFolderFile.exists()
        && workingFolderFile.isDirectory()
        && workingFolderFile.canRead();

    // Detect all subfolders
    if (workingFolderOk) {
      pathList.add(workingFolderPath);
      detectPaths(workingFolderPath);
    }

    // Build the glob for matching raw files
    StringBuilder sb = new StringBuilder();
    sb.append("regex:");
    sb.append("([^\\s]+(\\.(?i)(");
    final StringBuilder separator = new StringBuilder();
    Defaults.FILE_EXTENSIONS.forEach((ext, desc) -> {
      if (PhotoChaosFileType.IMAGE_RAW.equals(desc.getFileType())) {
        sb.append(separator);
        sb.append(ext);
        if (separator.length() == 0) {
          separator.append("|");
        }
      }
    });
    sb.append("))$)");
    rawGlobFilter = sb.toString();

    // Create the operation list
    List<IFilesystemOperation> fsOpList = new ArrayList<>();
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
      row.set(FSOP_SOURCE, op.getSource() == null ? null : workingFolderPath.relativize(op.getSource()));
      row.set(FSOP_SOURCE_NAME, op.getSourceName());
      row.set(FSOP_DESTINATION, op.getDestination() == null ? null : workingFolderPath.relativize(op.getDestination()));
      row.set(FSOP_DESTINATION_NAME, op.getDestinationName());
      row.set(FSOP_STATUS, op.getStatus());
    });

    response.getReports().add(opReport);

  }

}
