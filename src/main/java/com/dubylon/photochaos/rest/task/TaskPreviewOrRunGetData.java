package com.dubylon.photochaos.rest.task;

import com.dubylon.photochaos.rest.PCHandlerResponseData;

public class TaskPreviewOrRunGetData extends PCHandlerResponseData {

  private boolean started = false;
  private boolean alreadyRunning = false;

  public TaskPreviewOrRunGetData() {

  }

  public boolean isStarted() {
    return started;
  }

  public void setStarted(boolean started) {
    this.started = started;
  }

  public boolean isAlreadyRunning() {
    return alreadyRunning;
  }

  public void setAlreadyRunning(boolean alreadyRunning) {
    this.alreadyRunning = alreadyRunning;
  }
}
