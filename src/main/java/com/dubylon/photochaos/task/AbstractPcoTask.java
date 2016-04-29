package com.dubylon.photochaos.task;

public abstract class AbstractPcoTask implements IPcoTask {

  protected TaskStatus status;

  public AbstractPcoTask() {
    this.status = new TaskStatus();
  }

  @Override
  public TaskStatus getStatus() {
    return status;
  }

  public void setStatus(TaskStatus status) {
    this.status = status;
  }
}
