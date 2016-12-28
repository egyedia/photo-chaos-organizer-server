package com.dubylon.photochaos.model.operation;

public interface PcoOperation {

  PcoOperationType getType();

  void setStatus(PcoOperationStatus status);

  PcoOperationStatus getStatus();

  void perform();

  String getErrorMessage();

  void setException(Exception e);

  Exception getException();

  boolean isDoingSomething();

}
