package com.dubylon.photochaos.task;

import com.dubylon.photochaos.util.PcoEnum;

public enum TaskTemplateParameterYesOrNo implements PcoEnum {
  YES("yes"),
  NO("no");

  private final String value;

  TaskTemplateParameterYesOrNo(String value) {
    this.value = value;
  }

  @Override
  public String getValue() {
    return value;
  }

}
