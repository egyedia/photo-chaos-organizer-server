package com.dubylon.photochaos.task;

public enum PreviewOrRun {

  PREVIEW("preview"),
  RUN("run");

  private final String value;

  PreviewOrRun(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
