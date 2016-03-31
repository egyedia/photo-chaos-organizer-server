package com.dubylon.photochaos.model.tasktemplate;

public class TaskTemplateParameter implements Comparable<TaskTemplateParameter> {
  private String name;
  private String nameKey;
  private String descriptionKey;
  private boolean mandatory;
  private TaskTemplateParameterType type;
  private String defaultValue;
  private int order;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getNameKey() {
    return nameKey;
  }

  public void setNameKey(String nameKey) {
    this.nameKey = nameKey;
  }

  public String getDescriptionKey() {
    return descriptionKey;
  }

  public void setDescriptionKey(String descriptionKey) {
    this.descriptionKey = descriptionKey;
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

  public int getOrder() {
    return order;
  }

  public void setOrder(int order) {
    this.order = order;
  }

  @Override
  public int compareTo(TaskTemplateParameter o) {
    if (o == null) {
      return -1;
    } else {
      if (this.order < o.order) {
        return -1;
      } else if (this.order == o.order) {
        return 0;
      } else {
        return 1;
      }
    }
  }
}
