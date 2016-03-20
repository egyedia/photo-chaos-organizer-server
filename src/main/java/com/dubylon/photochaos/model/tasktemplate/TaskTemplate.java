package com.dubylon.photochaos.model.tasktemplate;

import java.util.List;
import java.util.Map;

public class TaskTemplate {

  protected String name;
  protected List<String> description;
  protected String className;
  protected Map<String, TaskTemplateParameter> parameters;
  protected String originalSource;

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

  public String getOriginalSource() {
    return originalSource;
  }

  public void setOriginalSource(String originalSource) {
    this.originalSource = originalSource;
  }
}
