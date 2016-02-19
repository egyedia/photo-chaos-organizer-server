package com.dubylon.photochaos;

import java.util.HashMap;
import java.util.Map;

public final class Defaults {
  private Defaults() {

  }

  public static final Map<String, Boolean> IMAGE_EXTENSIONS;

  static {
    IMAGE_EXTENSIONS = new HashMap<>();
    IMAGE_EXTENSIONS.put("jpg", true);
    IMAGE_EXTENSIONS.put("jpeg", true);
    IMAGE_EXTENSIONS.put("tiff", true);
    IMAGE_EXTENSIONS.put("png", false);
    IMAGE_EXTENSIONS.put("gif", false);
    IMAGE_EXTENSIONS.put("cr2", false);
  }
}
