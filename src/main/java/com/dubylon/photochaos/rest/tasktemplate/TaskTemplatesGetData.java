package com.dubylon.photochaos.rest.tasktemplate;

import com.dubylon.photochaos.model.tasktemplate.TaskTemplate;
import com.dubylon.photochaos.rest.PCHandlerResponseData;

public class TaskTemplatesGetData extends PCHandlerResponseData {

  private TaskTemplate taskTemplate;

  public TaskTemplatesGetData() {
  }

  public TaskTemplate getTaskTemplate() {
    return taskTemplate;
  }

  public void setTaskTemplate(TaskTemplate taskTemplate) {
    this.taskTemplate = taskTemplate;
  }
}
