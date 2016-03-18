package com.dubylon.photochaos.model.tasktemplate;

import java.util.List;

public class TaskTemplateParameter {
  private String name;
  private List<String> description;
  private boolean mandatory;
  private TaskTemplateParameterType type;
  private String defaultValue;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<String> getDescription() {
    return description;
  }

  public void setDescription(List<String> description) {
    this.description = description;
  }

  public boolean isMandatory() {
    return mandatory;
  }

  public void setMandatory(boolean mandatory) {
    this.mandatory = mandatory;
  }

  public TaskTemplateParameterType getType() {
    return type;
  }

  public void setType(TaskTemplateParameterType type) {
    this.type = type;
  }

  public String getDefaultValue() {
    return defaultValue;
  }

  public void setDefaultValue(String defaultValue) {
    this.defaultValue = defaultValue;
  }
}
