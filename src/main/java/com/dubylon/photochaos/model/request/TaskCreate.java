package com.dubylon.photochaos.model.request;

import java.util.Map;

public class TaskCreate {
  private String className;
  private Map<String, String> parameters;

  public TaskCreate() {
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public Map<String, String> getParameters() {
    return parameters;
  }

  public void setParameters(Map<String, String> parameters) {
    this.parameters = parameters;
  }
}
