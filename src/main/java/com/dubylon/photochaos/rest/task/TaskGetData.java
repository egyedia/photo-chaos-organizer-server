package com.dubylon.photochaos.rest.task;

import com.dubylon.photochaos.model.db.TaskDefinition;
import com.dubylon.photochaos.rest.PCHandlerResponseData;

public class TaskGetData extends PCHandlerResponseData {

  private TaskDefinition task;

  public TaskGetData() {
  }

  public TaskDefinition getTask() {
    return task;
  }

  public void setTask(TaskDefinition task) {
    this.task = task;
  }
}
