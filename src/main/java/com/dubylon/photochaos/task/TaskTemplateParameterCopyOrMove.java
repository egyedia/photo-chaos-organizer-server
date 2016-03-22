package com.dubylon.photochaos.task;

public enum TaskTemplateParameterCopyOrMove {
  COPY("copy"),
  MOVE("move");

  private final String value;

  TaskTemplateParameterCopyOrMove(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

}
