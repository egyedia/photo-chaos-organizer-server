package com.dubylon.photochaos.model.operation;

public abstract class AbstractFilesystemOperation extends AbstractPcoOperation implements FilesystemOperation {

  public boolean isDoingSomething() {
    return this.getType() != PcoOperationType.DONOTHING;
  }
}
