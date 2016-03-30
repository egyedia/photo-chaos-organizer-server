package com.dubylon.photochaos.model.operation;

import com.fasterxml.jackson.annotation.JsonValue;

public enum FilesystemOperationType {
  COPYFILE("copyFile"),
  MOVEFILE("moveFile"),
  CREATEFOLDER("createFolder"),
  DONOTHING("doNothing");

  private final String value;

  FilesystemOperationType(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

}
