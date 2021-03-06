package com.dubylon.photochaos.task.detectrepoduplicates;

import com.dubylon.photochaos.model.db.RepoFile;
import com.dubylon.photochaos.model.operation.*;
import com.dubylon.photochaos.model.tasktemplate.TaskTemplateParameterType;
import com.dubylon.photochaos.report.TableReport;
import com.dubylon.photochaos.task.AbstractFileSystemTask;
import com.dubylon.photochaos.task.PcoTaskTemplate;
import com.dubylon.photochaos.task.PcoTaskTemplateParameter;
import com.dubylon.photochaos.task.PreviewOrRun;
import com.dubylon.photochaos.util.FileSystemUtil;
import com.dubylon.photochaos.util.HibernateUtil;
import com.dubylon.photochaos.util.ReportUtil;
import org.hibernate.*;
import org.hibernate.criterion.Restrictions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

@PcoTaskTemplate(languageKeyPrefix = "task.detectDuplicatesVersusRepo.")
public class DetectDuplicatesVersusRepoTask extends AbstractFileSystemTask {

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

  private String knownGlobFilter;
  private Path sourcePath;
  private Path destinationPath;
  private List<Path> pathList;

  private SessionFactory sessionFactory;
  private Transaction tx;
  private Session session;


  public DetectDuplicatesVersusRepoTask() {

  }

  private void createOperation(Path currentPath, List<FilesystemOperation> fsol) {
    final PathMatcher filter = currentPath.getFileSystem().getPathMatcher(knownGlobFilter);

    sessionFactory = null;
    tx = null;
    session = null;

    try {
      sessionFactory = HibernateUtil.getSessionFactory();
      session = sessionFactory.openSession();
      tx = session.beginTransaction();
    } catch (HibernateException e) {
      e.printStackTrace();
      if (tx != null) {
        tx.rollback();
      }
    }

    try (final Stream<Path> stream = Files.list(currentPath)) {
      stream
          .filter(path -> path.toFile().isFile() && filter.matches(path.getFileName()))
          .forEach(path -> {
            Path namePath = path.getFileName();
            Path newPath = destinationPath.resolve(sourcePath.relativize(currentPath));
            RepoFile rf = RepoFile.buildFrom(namePath, currentPath);

            Criteria crit = session.createCriteria(RepoFile.class)
                .add(Restrictions.eq("nameSizeHash", rf.getNameSizeHash()));
            RepoFile dbrf = (RepoFile)crit.uniqueResult();

            if (dbrf != null) {
              final FilesystemOperation fileOp = new MoveFile(namePath, currentPath, newPath);
              fsol.add(fileOp);
            }
          });
    } catch (IOException e) {
      e.printStackTrace();
    }

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
      //TODO: fix these nulls
      pathList = FileSystemUtil.getAllSubfoldersIncluding(sourcePath, null, null);
    } else {
      pathList = new ArrayList<>();
    }

    // Create the operation list
    List<FilesystemOperation> fsOpList = new ArrayList<>();

    knownGlobFilter = buildKnownGlobFilter();

    pathList.forEach(path -> this.createOperation(path, fsOpList));

    // Create the operation report
    TableReport opReport = ReportUtil.buildOperationReport();
    status.getReports().add(opReport);

    //TODO: fix the two nulls
    executeOperations(fsOpList, opReport, sourcePath, destinationPath, previewOrRun, null, null);
  }
}
