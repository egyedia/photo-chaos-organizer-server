package com.dubylon.photochaos.util;

import com.dubylon.photochaos.Defaults;

import java.nio.file.attribute.BasicFileAttributes;

public final class FileTypeUtil {
  private FileTypeUtil() {
  }

  public static PhotoChaosFileTypeDescriptor getPhotoChaosFileTypeDescriptor(String extension) {
    if (extension != null) {
      extension = extension.toLowerCase();
    }
    PhotoChaosFileTypeDescriptor ftd = Defaults.FILE_EXTENSIONS.get(extension);
    if (ftd == null) {
      ftd = Defaults.FILE_OTHER;
    }
    return ftd;
  }

  public static FileSystemFileType getFileSystemFileTypeDescriptor(BasicFileAttributes attrs) {
    if (attrs.isRegularFile()) {
      return FileSystemFileType.FILE;
    } else if (attrs.isDirectory()) {
      return FileSystemFileType.DIR;
    } else if (attrs.isSymbolicLink()) {
      return FileSystemFileType.SYMLINK;
    } else {
      return FileSystemFileType.OTHER;
    }
  }

  public static FileSystemAttributes getFileSystemAttributes(BasicFileAttributes attrs, boolean hidden) {
    return new FileSystemAttributes(attrs.size(), attrs.lastModifiedTime().toMillis(), hidden);
  }
}
