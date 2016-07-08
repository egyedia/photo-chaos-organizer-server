package com.dubylon.photochaos.model.operation;

public abstract class AbstractPcoOperation implements IPcoOperation {

  protected PcoOperationStatus status = PcoOperationStatus.UNDEFINED;

  protected String errorMessage = null;

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
}
