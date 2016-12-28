package com.dubylon.photochaos.model.operation;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PcoOperationType {
  COPYFILE("copyFile"),
  MOVEFILE("moveFile"),
  CREATEFOLDER("createFolder"),
  DONOTHING("doNothing"),
  IMPORTINTOREPO("importIntoRepo"),
  PREVIEWIMPORTINTOREPO("previewImportIntoRepo"),
  REPORTERROR("reportError");

  private final String value;

  PcoOperationType(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

}
