package com.dubylon.photochaos;

import com.dubylon.photochaos.app.AppConfigFileType;
import com.dubylon.photochaos.app.AppConfigFileTypes;
import com.dubylon.photochaos.util.PhotoChaosFileType;
import com.dubylon.photochaos.util.PhotoChaosFileTypeDescriptor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Defaults {
  private Defaults() {

  }

  public static final Map<String, PhotoChaosFileTypeDescriptor> FILE_EXTENSIONS;

  public static final PhotoChaosFileTypeDescriptor FILE_OTHER = new PhotoChaosFileTypeDescriptor(PhotoChaosFileType
      .OTHER, false, false, false);

  static {
    FILE_EXTENSIONS = new HashMap<>();
  }

  public static void initFrom(AppConfigFileTypes fileTypes) {
    for (AppConfigFileType type : fileTypes.getTypes().values()) {
      List<String> extensions = type.getExtensions();
      for (String ext : extensions) {
        FILE_EXTENSIONS.put(ext, new PhotoChaosFileTypeDescriptor(type.getFileType(),
            type.isCanHaveMeta(),
            type.isCanHaveOnTheFlyImageThumb(),
            type.isCanShowThumb()));
      }
    }
  }
}
