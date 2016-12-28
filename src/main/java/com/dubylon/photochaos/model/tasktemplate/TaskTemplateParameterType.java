package com.dubylon.photochaos.model.tasktemplate;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TaskTemplateParameterType {
  COPYORMOVE("copyOrMove"),
  FOLDERNAME("folderName"),
  FOLDERSUFFIX("folderSuffix"),
  PATH("path"),
  SHORTDATEFORMAT("shortDateFormat"),
  YESORNO("yesOrNo"),
  IMPORTORPREVIEW("importOrPreview"),
  REPONAME("repoName");

  private final String value;

  TaskTemplateParameterType(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  }
