package com.dubylon.photochaos.model.tasktemplate;

import java.util.LinkedHashMap;
import java.util.Map;

public class TaskTemplate implements Comparable<TaskTemplate> {

  protected String name;
  protected String description;
  protected String className;
  protected Map<String, TaskTemplateParameter> parameters;

  public TaskTemplate() {
    parameters = new LinkedHashMap<>();
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

  @Override
  public int compareTo(TaskTemplate o) {
    if (this.name != null) {
      return this.name.compareTo(o.name);
    } else {
      return 1;
    }
  }
}
