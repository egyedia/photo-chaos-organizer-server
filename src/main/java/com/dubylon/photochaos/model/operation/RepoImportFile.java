package com.dubylon.photochaos.model.operation;

import com.dubylon.photochaos.model.db.RepoFile;
import org.hibernate.Session;

import java.nio.file.Path;

public class RepoImportFile extends AbstractPcoOperation {

  private RepoFile repoFile;
  private Path parentPath;
  private Session session;

  public RepoImportFile(RepoFile rf, Path parentPath) {
    this.repoFile = rf;
    this.parentPath = parentPath;
  }

  @Override
  public PcoOperationType getType() {
    return PcoOperationType.IMPORTINTOREPO;
  }

  @Override
  public void perform() {
    session.save(repoFile);
    setStatus(PcoOperationStatus.SUCCESS);
  }

  @Override
  public boolean isDoingSomething() {
    return true;
  }

  public RepoFile getRepoFile() {
    return repoFile;
  }

  public Path getParentPath() {
    return parentPath;
  }

  public void injectSession(Session session) {
    this.session = session;
  }
}
