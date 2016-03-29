package com.dubylon.photochaos.model.operation;

import java.nio.file.Path;

public class CreateFolder implements FilesystemOperation {

  private Path parent;
  private Path name;

  public CreateFolder(Path parent, Path name) {
    this.parent = parent;
    this.name = name;
  }

  @Override
  public FilesystemOperationType getType() {
    return FilesystemOperationType.CREATEFOLDER;
  }

  @Override
  public Path getSource() {
    return null;
  }

  @Override
  public Path getSourceName() {
    return null;
  }

  @Override
  public Path getDestination() {
    return parent;
  }

  @Override
  public Path getDestinationName() {
    return name;
  }

  @Override
  public FilesystemOperationStatus getStatus() {
    return FilesystemOperationStatus.SUCCESS;
  }
}
