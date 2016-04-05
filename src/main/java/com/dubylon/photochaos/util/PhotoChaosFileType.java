package com.dubylon.photochaos.util;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PhotoChaosFileType {
  IMAGE("image"),
  IMAGE_RAW("image-raw"),
  VIDEO("video"),
  IMAGE_META("image-meta"),
  VIDEO_META("video-meta"),
  THUMBNAIL("thumbnail"),
  SOUND("sound"),
  OTHER("other");

  private final String value;

  PhotoChaosFileType(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }
}
