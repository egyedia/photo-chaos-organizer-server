package com.dubylon.photochaos.model.operation;

import com.fasterxml.jackson.annotation.JsonValue;

public enum FilesystemOperationStatus {
  SUCCESS("S"),
  WARNING("W"),
  ERROR("E");

  private final String value;

  FilesystemOperationStatus(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

}
