package com.dubylon.photochaos.model.operation;

public interface IPcoOperation {

  PcoOperationType getType();

  void setStatus(PcoOperationStatus status);

  PcoOperationStatus getStatus();

  void perform();

  String getErrorMessage();
}
