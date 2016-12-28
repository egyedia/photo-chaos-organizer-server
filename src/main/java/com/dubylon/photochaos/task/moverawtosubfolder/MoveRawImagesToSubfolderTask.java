package com.dubylon.photochaos.task.moverawtosubfolder;

import com.dubylon.photochaos.model.operation.*;
import com.dubylon.photochaos.model.tasktemplate.TaskTemplateParameterType;
import com.dubylon.photochaos.report.TableReport;
import com.dubylon.photochaos.report.TableReportRow;
import com.dubylon.photochaos.task.AbstractFileSystemTask;
import com.dubylon.photochaos.task.PcoTaskTemplate;
import com.dubylon.photochaos.task.PcoTaskTemplateParameter;
import com.dubylon.photochaos.task.PreviewOrRun;
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
import java.util.function.Consumer;
import java.util.stream.Stream;

import static com.dubylon.photochaos.report.TableReport.FSOP_PHASE;
import static com.dubylon.photochaos.report.TableReport.FSOP_PHASE_VALUE;

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

  private void createOperation(Path currentPath, List<FilesystemOperation> fsol, TableReportRow row,
                               Consumer<TableReportRow> action) {
    final FilesystemOperation folderOp;
    Path newPath = currentPath.resolve(rawPath);
    boolean doRecursion = true;
    if (newPath.toFile().exists()) {
      if (newPath.toFile().isDirectory()) {
        folderOp = new FolderAlreadyPresent(newPath);
      } else {
        folderOp = new NotFolderAlreadyPresent(newPath);
        doRecursion = false;
      }
    } else {
      folderOp = new CreateFolder(currentPath, rawPath);
    }

    if (doRecursion) {
      final AtomicBoolean folderAlreadyCreated = new AtomicBoolean();
      folderAlreadyCreated.set(false);
      final PathMatcher filter = currentPath.getFileSystem().getPathMatcher(rawGlobFilter);
      try (final Stream<Path> stream = Files.list(currentPath)) {
        stream
            .filter(path -> path.toFile().isFile() && filter.matches(path.getFileName()))
            .forEach(path -> {
              if (!folderAlreadyCreated.get()) {
                fsol.add(folderOp);
                if (folderOp.isDoingSomething()) {
                  action.accept(row);
                }
                folderAlreadyCreated.set(true);
              }
              FilesystemOperation moveOp = new MoveFile(path.getFileName(), currentPath, newPath);
              fsol.add(moveOp);
              if (moveOp.isDoingSomething()) {
                action.accept(row);
              }
            });
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void execute(PreviewOrRun previewOrRun) {
    Path workingPath = Paths.get(workingFolder);

    rawPath = Paths.get(rawFolder);
    pathList = new ArrayList<>();

    // Check working folder
    boolean workingFolderOk = FileSystemUtil.isDirectoryAndReadable(workingPath);

    TableReport phaseReport = ReportUtil.buildPhaseReport();
    status.getReports().add(phaseReport);

    // Detect all subfolders except raw
    TableReportRow row1 = phaseReport.createRow();
    row1.set(FSOP_PHASE, "detectingFolders");
    if (workingFolderOk) {
      row1.set(FSOP_PHASE_VALUE, 0);
      pathList = FileSystemUtil.getAllSubfoldersIncludingExcluding(workingPath, rawFolder, row1, row -> {
        row.set(FSOP_PHASE_VALUE, (Integer) row.get(FSOP_PHASE_VALUE) + 1);
      });
    } else {
      row1.set(FSOP_PHASE_VALUE, "workingFolderError");
      row1.setStatus(PcoOperationStatus.ERROR);
      pathList = new ArrayList<>();
    }

    rawGlobFilter = buildRawGlobFilter();

    // Create the operation list
    TableReportRow row2 = phaseReport.createRow();
    row2.set(FSOP_PHASE, "creatingOperationList");
    row2.set(FSOP_PHASE_VALUE, 0);
    List<FilesystemOperation> fsOpList = new ArrayList<>();
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
    executeOperations(fsOpList, opReport, workingPath, workingPath, previewOrRun, row3, row -> {
      row.set(FSOP_PHASE_VALUE, (Integer) row.get(FSOP_PHASE_VALUE) + 1);
    });
  }

}
