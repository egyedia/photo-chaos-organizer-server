package com.dubylon.photochaos.model.operation;

public abstract class AbstractFilesystemOperation implements IFilesystemOperation {

  protected FilesystemOperationStatus status = FilesystemOperationStatus.UNDEFINED;

  protected String errorMessage = null;

  @Override
  public FilesystemOperationStatus getStatus() {
    return status;
  }

  @Override
  public void setStatus(FilesystemOperationStatus status) {
    this.status = status;
  }

  @Override
  public String getErrorMessage() {
    return errorMessage;
  }
}
