package com.dubylon.photochaos.model.operation;

import java.nio.file.Path;

public class MoveFile implements FilesystemOperation {

  private Path fileName;
  private Path sourcePath;
  private Path destinationPath;

  public MoveFile(Path fileName, Path sourcePath, Path destinationPath) {
    this.fileName = fileName;
    this.sourcePath = sourcePath;
    this.destinationPath = destinationPath;
  }

  @Override
  public FilesystemOperationType getType() {
    return FilesystemOperationType.MOVEFILE;
  }

  @Override
  public Path getSource() {
    return sourcePath;
  }

  @Override
  public Path getSourceName() {
    return fileName;
  }

  @Override
  public Path getDestination() {
    return destinationPath;
  }

  @Override
  public Path getDestinationName() {
    return fileName;
  }

  @Override
  public FilesystemOperationStatus getStatus() {
    return FilesystemOperationStatus.SUCCESS;
  }
}
