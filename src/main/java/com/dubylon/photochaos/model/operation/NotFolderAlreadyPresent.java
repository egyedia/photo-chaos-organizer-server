package com.dubylon.photochaos.model.operation;

import java.nio.file.Path;

public class NotFolderAlreadyPresent extends AbstractFilesystemOperation {

  private Path path;

  public NotFolderAlreadyPresent(Path path) {
    this.path = path;
  }

  @Override
  public PcoOperationType getType() {
    return PcoOperationType.DONOTHING;
  }

  @Override
  public Path getSource() {
    return path;
  }

  @Override
  public Path getSourceName() {
    return null;
  }

  @Override
  public Path getDestination() {
    return null;
  }

  @Override
  public Path getDestinationName() {
    return null;
  }

  @Override
  public void perform() {
    // do nothing
  }

}
