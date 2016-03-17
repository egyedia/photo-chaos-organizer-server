package com.dubylon.photochaos.rest.tasktemplate;

import com.dubylon.photochaos.model.tasktemplate.TaskTemplate;
import com.dubylon.photochaos.rest.PCHandlerResponseData;

import java.util.ArrayList;
import java.util.List;

public class TaskTemplatesGetData extends PCHandlerResponseData {

  private List<TaskTemplate> taskTemplates;

  public TaskTemplatesGetData() {
    taskTemplates = new ArrayList<>();
  }

  public List<TaskTemplate> getTaskTemplates() {
    return taskTemplates;
  }

  public void setTaskTemplates(List<TaskTemplate> taskTemplates) {
    this.taskTemplates = taskTemplates;
  }
}
