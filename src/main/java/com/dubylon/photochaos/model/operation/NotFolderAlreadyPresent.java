package com.dubylon.photochaos.model.operation;

import java.nio.file.Path;

public class NotFolderAlreadyPresent implements FilesystemOperation {

  private Path path;

  public NotFolderAlreadyPresent(Path path) {
    this.path = path;
  }

  @Override
  public FilesystemOperationType getType() {
    return FilesystemOperationType.DONOTHING;
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
  public FilesystemOperationStatus getStatus() {
    return FilesystemOperationStatus.SUCCESS;
  }
}
