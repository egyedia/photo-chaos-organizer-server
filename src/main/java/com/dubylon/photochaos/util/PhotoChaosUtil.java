package com.dubylon.photochaos.util;

import java.nio.file.Path;

public final class PhotoChaosUtil {

  private PhotoChaosUtil() {
  }

  public static String getNormalPath(String s) {
    return s == null ? null : s.replace("\\", "/").replace("//", "/");
  }

  public static String getNormalPath(Path p) {
    return p == null ? null : getNormalPath(p.toString());
  }
}
