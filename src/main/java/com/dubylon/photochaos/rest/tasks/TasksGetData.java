package com.dubylon.photochaos.rest.tasks;

import com.dubylon.photochaos.model.db.TaskDefinition;
import com.dubylon.photochaos.rest.PCHandlerResponseData;

import java.util.List;

public class TasksGetData extends PCHandlerResponseData {

  private List<TaskDefinition> tasks;

  public TasksGetData() {
  }

  public List<TaskDefinition> getTasks() {
    return tasks;
  }

  public void setTasks(List<TaskDefinition> tasks) {
    this.tasks = tasks;
  }
}
