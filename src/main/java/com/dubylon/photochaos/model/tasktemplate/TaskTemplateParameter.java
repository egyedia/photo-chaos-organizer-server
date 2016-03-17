package com.dubylon.photochaos.model.tasktemplate;

import java.util.Map;

public class TaskTemplateParameter {
  private String name;
  private String description;
  private TaskTemplateParameterType type;
  private boolean mandatory;
  private Map<String, String> options;

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

  public TaskTemplateParameterType getType() {
    return type;
  }

  public void setType(TaskTemplateParameterType type) {
    this.type = type;
  }

  public boolean isMandatory() {
    return mandatory;
  }

  public void setMandatory(boolean mandatory) {
    this.mandatory = mandatory;
  }

  public Map<String, String> getOptions() {
    return options;
  }

  public void setOptions(Map<String, String> options) {
    this.options = options;
  }
}
