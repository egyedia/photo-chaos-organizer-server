package com.dubylon.photochaos.rest.task;

import com.dubylon.photochaos.model.db.TaskDefinition;
import com.dubylon.photochaos.rest.PCHandlerResponseData;

public class TaskPutData extends PCHandlerResponseData {

  private TaskDefinition updatedObject;
  private Long id;

  public TaskPutData() {
  }

  public TaskDefinition getUpdatedObject() {
    return updatedObject;
  }

  public void setUpdatedObject(TaskDefinition updatedObject) {
    this.updatedObject = updatedObject;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
