package com.dubylon.photochaos.task;

import com.dubylon.photochaos.util.PcoEnum;

public enum TaskTemplateParameterCopyOrMove implements PcoEnum {
  COPY("copy"),
  MOVE("move");

  private final String value;

  TaskTemplateParameterCopyOrMove(String value) {
    this.value = value;
  }

  @Override
  public String getValue() {
    return value;
  }

}
