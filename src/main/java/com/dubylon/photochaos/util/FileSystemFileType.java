package com.dubylon.photochaos.util;

import com.fasterxml.jackson.annotation.JsonValue;

public enum FileSystemFileType {
  FILE("file"),
  DIR("dir"),
  SYMLINK("symlink"),
  OTHER("other");

  private String value;

  FileSystemFileType(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }
}
