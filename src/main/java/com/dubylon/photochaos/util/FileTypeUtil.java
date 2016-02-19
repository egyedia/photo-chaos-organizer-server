package com.dubylon.photochaos.util;

import com.dubylon.photochaos.Defaults;

public final class FileTypeUtil {
  private FileTypeUtil() {
  }

  public static boolean canHaveThumbnail(String extension) {
    if (extension != null) {
      extension = extension.toLowerCase();
    }
    Boolean canHaveMeta = Defaults.IMAGE_EXTENSIONS.get(extension);
    if (canHaveMeta != null) {
      return canHaveMeta;
    } else {
      return false;
    }
  }
}
