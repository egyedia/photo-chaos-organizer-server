package com.dubylon.photochaos.model.tasktemplate;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TaskTemplateParameterType {
  FOLDER("folder"),
  SHORTDATEFORMAT("shortDateFormat"),
  COPYORMOVE("copyOrMove"),
  FOLDERSUFFIX("folderSuffix");

  private final String value;

  TaskTemplateParameterType(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

}
