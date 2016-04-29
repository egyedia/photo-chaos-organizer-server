package com.dubylon.photochaos.task;

import java.time.Instant;

public class TaskRunInstance {

  private IPcoTask task;
  private long userId;
  private RunningInstanceInfo instance;

  public TaskRunInstance(IPcoTask task) {
    this.task = task;
    this.instance = new RunningInstanceInfo();
  }

  public IPcoTask getTask() {
    return task;
  }

  public void setTask(IPcoTask task) {
    this.task = task;
  }

  public long getUserId() {
    return userId;
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }

  public RunningInstanceInfo getInstance() {
    return instance;
  }

  public void setInstance(RunningInstanceInfo instance) {
    this.instance = instance;
  }

  public void start(long userId) {
    instance.setStartedAt(Instant.now());
    instance.setRunning(true);
    this.userId = userId;
  }

  public void finish() {
    instance.setFinishedAt(Instant.now());
    instance.setRunning(false);
  }
}
