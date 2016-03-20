package com.dubylon.photochaos.rest.tasks;

import com.dubylon.photochaos.model.db.TaskDefinition;
import com.dubylon.photochaos.rest.PCHandlerResponseData;

public class TasksPostData extends PCHandlerResponseData {

  private TaskDefinition createdObject;
  private Long id;

  public TasksPostData() {
  }

  public TaskDefinition getCreatedObject() {
    return createdObject;
  }

  public void setCreatedObject(TaskDefinition createdObject) {
    this.createdObject = createdObject;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
