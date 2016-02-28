package com.dubylon.photochaos.util;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PhotoChaosFileType {
  IMAGE("image"),
  VIDEO("video"),
  IMAGE_META("image-meta"),
  VIDEO_META("video-meta"),
  OTHER("other");

  private String value;

  PhotoChaosFileType(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }
}
