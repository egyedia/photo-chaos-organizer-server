package com.dubylon.photochaos;

import com.dubylon.photochaos.util.PhotoChaosFileType;
import com.dubylon.photochaos.util.PhotoChaosFileTypeDescriptor;

import java.util.HashMap;
import java.util.Map;

public final class Defaults {
  private Defaults() {

  }

  public static final Map<String, PhotoChaosFileTypeDescriptor> FILE_EXTENSIONS;

  public static final PhotoChaosFileTypeDescriptor FILE_OTHER = new PhotoChaosFileTypeDescriptor(PhotoChaosFileType
      .OTHER, false, false, false);

  static {
    // TODO : the files should have a textual type info
    FILE_EXTENSIONS = new HashMap<>();
    FILE_EXTENSIONS.put("jpg", new PhotoChaosFileTypeDescriptor(PhotoChaosFileType.IMAGE, true, true, true));
    FILE_EXTENSIONS.put("jpeg", new PhotoChaosFileTypeDescriptor(PhotoChaosFileType.IMAGE, true, true, true));
    FILE_EXTENSIONS.put("tiff", new PhotoChaosFileTypeDescriptor(PhotoChaosFileType.IMAGE, true, true, true));
    FILE_EXTENSIONS.put("png", new PhotoChaosFileTypeDescriptor(PhotoChaosFileType.IMAGE, false, true, true));
    FILE_EXTENSIONS.put("gif", new PhotoChaosFileTypeDescriptor(PhotoChaosFileType.IMAGE, false, true, true));

    FILE_EXTENSIONS.put("cr2", new PhotoChaosFileTypeDescriptor(PhotoChaosFileType.IMAGE_RAW, false, false, false));
    FILE_EXTENSIONS.put("nef", new PhotoChaosFileTypeDescriptor(PhotoChaosFileType.IMAGE_RAW, false, false, false));

    FILE_EXTENSIONS.put("avi", new PhotoChaosFileTypeDescriptor(PhotoChaosFileType.VIDEO, false, false, true));
    FILE_EXTENSIONS.put("mk4", new PhotoChaosFileTypeDescriptor(PhotoChaosFileType.VIDEO, false, false, true));
    FILE_EXTENSIONS.put("mvi", new PhotoChaosFileTypeDescriptor(PhotoChaosFileType.VIDEO, false, false, true));
    FILE_EXTENSIONS.put("3gp", new PhotoChaosFileTypeDescriptor(PhotoChaosFileType.VIDEO, false, false, true));
    FILE_EXTENSIONS.put("mp4", new PhotoChaosFileTypeDescriptor(PhotoChaosFileType.VIDEO, false, false, true));
    FILE_EXTENSIONS.put("mov", new PhotoChaosFileTypeDescriptor(PhotoChaosFileType.VIDEO, false, false, true));
    FILE_EXTENSIONS.put("m2ts", new PhotoChaosFileTypeDescriptor(PhotoChaosFileType.VIDEO, false, false, true));

    FILE_EXTENSIONS.put("modd", new PhotoChaosFileTypeDescriptor(PhotoChaosFileType.VIDEO_META, false, false, false));
    FILE_EXTENSIONS.put("moff", new PhotoChaosFileTypeDescriptor(PhotoChaosFileType.VIDEO_META, false, false, false));

    FILE_EXTENSIONS.put("thm", new PhotoChaosFileTypeDescriptor(PhotoChaosFileType.THUMBNAIL, false, false, false));

    FILE_EXTENSIONS.put("mp3", new PhotoChaosFileTypeDescriptor(PhotoChaosFileType.SOUND, false, false, false));
    FILE_EXTENSIONS.put("wav", new PhotoChaosFileTypeDescriptor(PhotoChaosFileType.SOUND, false, false, false));
  }
}
