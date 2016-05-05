package com.dubylon.photochaos.rest.task;

import com.dubylon.photochaos.rest.PCHandlerResponseData;

public class TaskDeleteData extends PCHandlerResponseData {

  private boolean deleted;

  public TaskDeleteData() {
  }

  public boolean isDeleted() {
    return deleted;
  }

  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }
}
