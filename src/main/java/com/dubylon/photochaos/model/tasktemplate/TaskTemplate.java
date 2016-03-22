package com.dubylon.photochaos.model.tasktemplate;

import java.util.Map;
import java.util.HashMap;

public class TaskTemplate {

  protected String name;
  protected String description;
  protected String className;
  protected Map<String, TaskTemplateParameter> parameters;

  public TaskTemplate() {
    parameters = new HashMap<>();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public Map<String, TaskTemplateParameter> getParameters() {
    return parameters;
  }

  public void setParameters(Map<String, TaskTemplateParameter> parameters) {
    this.parameters = parameters;
  }

}
