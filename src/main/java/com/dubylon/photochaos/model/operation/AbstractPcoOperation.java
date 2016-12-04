package com.dubylon.photochaos.model.operation;

public abstract class AbstractPcoOperation implements PcoOperation {

  protected PcoOperationStatus status = PcoOperationStatus.UNDEFINED;

  protected String errorMessage = null;
  protected Exception exception;

  @Override
  public PcoOperationStatus getStatus() {
    return status;
  }

  @Override
  public void setStatus(PcoOperationStatus status) {
    this.status = status;
  }

  @Override
  public String getErrorMessage() {
    return errorMessage;
  }

  @Override
  public void setException(Exception exception) {
    this.exception = exception;
    if (exception != null) {
      this.errorMessage = exception.getMessage();
    }
  }

  @Override
  public Exception getException() {
    return exception;
  }

}
