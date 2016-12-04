package com.dubylon.photochaos.task.repoimport;

import com.dubylon.photochaos.model.db.RepoFile;
import com.dubylon.photochaos.model.operation.RepoImportFile;
import com.dubylon.photochaos.model.tasktemplate.TaskTemplateParameterType;
import com.dubylon.photochaos.report.TableReport;
import com.dubylon.photochaos.report.TableReportRow;
import com.dubylon.photochaos.task.*;
import com.dubylon.photochaos.util.FileSystemUtil;
import com.dubylon.photochaos.util.HibernateUtil;
import org.hibernate.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.dubylon.photochaos.report.TableReport.*;

@PcoTaskTemplate(languageKeyPrefix = "task.importFilesIntoRepo.")
public class ImportFilesIntoRepoTask extends AbstractPcoTask {

  @PcoTaskTemplateParameter(
      type = TaskTemplateParameterType.PATH,
      mandatory = true,
      defaultValue = "",
      order = 1
  )
  private String sourceFolder;

  /*
  @PcoTaskTemplateParameter(
      type = TaskTemplateParameterType.REPONAME,
      mandatory = true,
      defaultValue = "Default Repo",
      order = 2
  )
  private String repoName;
  */

  private String knownGlobFilter;
  private Path sourcePath;
  private List<Path> pathList;

  public ImportFilesIntoRepoTask() {
  }

  private void createOperation(Path currentPath, List<RepoImportFile> fsol) {
    final PathMatcher filter = currentPath.getFileSystem().getPathMatcher(knownGlobFilter);
    try (final Stream<Path> stream = Files.list(currentPath)) {
      stream
          .filter(path -> path.toFile().isFile() && filter.matches(path.getFileName()))
          .forEach(path -> {
            Path namePath = path.getFileName();
            RepoFile rf = RepoFile.buildFrom(namePath, currentPath);
            final RepoImportFile fileOp = new RepoImportFile(rf, currentPath);
            fsol.add(fileOp);
          });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static TableReport buildOperationReport() {
    TableReport opReport = new TableReport();
    opReport.addHeader(FSOP_OPERATION);
    opReport.addHeader(FSOP_SOURCE);
    opReport.addHeader(FSOP_SOURCE_NAME);
    opReport.addHeader(FSOP_STATUS);
    return opReport;
  }

  protected void executeOperations(List<RepoImportFile> fsOpList, TableReport opReport, Path sourcePath,
                                   PreviewOrRun previewOrRun) {

    SessionFactory sessionFactory = null;
    Session session = null;
    Transaction tx = null;

    if (previewOrRun == PreviewOrRun.RUN) {
      try {
        sessionFactory = HibernateUtil.getSessionFactory();
        session = sessionFactory.openSession();
        tx = session.beginTransaction();

        String hql = String.format("delete from %s", RepoFile.class.getSimpleName());
        Query query = session.createQuery(hql);
        query.executeUpdate();
      } catch (HibernateException e) {
        e.printStackTrace();
        if (tx != null) {
          tx.rollback();
        }
      }
    }

    int i = 0;
    for (RepoImportFile op : fsOpList) {
      op.injectSession(session);
      boolean addRow = i % 100 == 0;
      try {
        PcoOperationPerformer.perform(op, previewOrRun);
      } catch (HibernateException e) {
        e.printStackTrace();
        addRow = true;
      }
      if (addRow) {
        TableReportRow row = opReport.createRow();
        row.set(FSOP_OPERATION, op.getType());
        row.set(FSOP_SOURCE, op.getParentPath() == null ? null : sourcePath.relativize(op.getParentPath()));
        row.set(FSOP_SOURCE_NAME, op.getRepoFile().getName());
        row.set(FSOP_STATUS, op.getStatus());
      }
      i++;
    }

    if (previewOrRun == PreviewOrRun.RUN && tx != null) {
      try {
        tx.commit();
      } catch (HibernateException e) {
        e.printStackTrace();
        if (tx != null) {
          tx.rollback();
        }
      } finally {
        session.close();
      }
    }
  }

  @Override
  public void execute(PreviewOrRun previewOrRun) {
    sourcePath = Paths.get(sourceFolder);

    // Check source folder
    boolean sourceFolderOk = FileSystemUtil.isDirectoryAndReadable(sourcePath);

    // Detect all subfolders
    if (sourceFolderOk) {
      pathList = FileSystemUtil.getAllSubfoldersIncluding(sourcePath);
    } else {
      pathList = new ArrayList<>();
    }

    // Create the operation list
    List<RepoImportFile> fsOpList = new ArrayList<>();

    knownGlobFilter = buildKnownGlobFilter();

    pathList.forEach(path -> this.createOperation(path, fsOpList));

    // Create the operation report
    TableReport opReport = buildOperationReport();
    status.getReports().add(opReport);

    executeOperations(fsOpList, opReport, sourcePath, previewOrRun);

  }

}
