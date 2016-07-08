package com.dubylon.photochaos.task.moverawtosubfolder;

import com.dubylon.photochaos.model.operation.*;
import com.dubylon.photochaos.model.tasktemplate.TaskTemplateParameterType;
import com.dubylon.photochaos.report.TableReport;
import com.dubylon.photochaos.task.AbstractFileSystemTask;
import com.dubylon.photochaos.task.PcoTaskTemplate;
import com.dubylon.photochaos.task.PcoTaskTemplateParameter;
import com.dubylon.photochaos.util.FileSystemUtil;
import com.dubylon.photochaos.util.ReportUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

@PcoTaskTemplate(languageKeyPrefix = "task.moveRawImagesToSubfolders.")
public class MoveRawImagesToSubfolderTask extends AbstractFileSystemTask {

  @PcoTaskTemplateParameter(
      type = TaskTemplateParameterType.PATH,
      mandatory = true,
      defaultValue = "",
      order = 1
  )
  private String workingFolder;

  @PcoTaskTemplateParameter(
      type = TaskTemplateParameterType.FOLDERNAME,
      mandatory = true,
      defaultValue = "raw",
      order = 2
  )
  private String rawFolder;

  private Path rawPath;
  private String rawGlobFilter;
  private List<Path> pathList;

  public MoveRawImagesToSubfolderTask() {
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
  public void execute(boolean performOperations) {
    Path workingPath = Paths.get(workingFolder);

    rawPath = Paths.get(rawFolder);
    pathList = new ArrayList<>();

    // Check working folder
    boolean workingFolderOk = FileSystemUtil.isDirectoryAndReadable(workingPath);

    // Detect all subfolders except raw
    if (workingFolderOk) {
      pathList = FileSystemUtil.getAllSubfoldersIncludingExcluding(workingPath, rawFolder);
    } else {
      pathList = new ArrayList<>();
    }

    rawGlobFilter = buildRawGlobFilter();

    // Create the operation list
    List<IFilesystemOperation> fsOpList = new ArrayList<>();
    pathList.forEach(path -> this.createOperation(path, fsOpList));

    // Create the operation report
    TableReport opReport = ReportUtil.buildOperationReport();
    status.getReports().add(opReport);

    executeOperations(fsOpList, opReport, workingPath, workingPath, performOperations);
  }

}
