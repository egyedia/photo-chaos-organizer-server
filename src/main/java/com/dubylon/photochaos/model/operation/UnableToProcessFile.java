package com.dubylon.photochaos.model.operation;

import java.nio.file.Path;

public class UnableToProcessFile extends AbstractFilesystemOperation {

  private Path fileName;
  private Path sourcePath;

  public UnableToProcessFile(Path fileName, Path sourcePath) {
    this.fileName = fileName;
    this.sourcePath = sourcePath;
  }

  @Override
  public PcoOperationType getType() {
    return PcoOperationType.REPORTERROR;
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
