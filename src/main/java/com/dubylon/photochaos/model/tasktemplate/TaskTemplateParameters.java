package com.dubylon.photochaos.model.tasktemplate;

import java.util.ArrayList;
import java.util.List;

public class TaskTemplateParameters {
  private List<TaskTemplateParameter> parameters;

  public TaskTemplateParameters() {
    this.parameters = new ArrayList<>();
  }

  public List<TaskTemplateParameter> getParameters() {
    return parameters;
  }
}
