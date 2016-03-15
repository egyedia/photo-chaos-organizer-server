package com.dubylon.photochaos.model.response.fs;

import com.dubylon.photochaos.util.FileSystemAttributes;
import com.dubylon.photochaos.util.FileSystemFileType;
import com.dubylon.photochaos.util.PhotoChaosFileTypeDescriptor;

import java.util.regex.Pattern;

public class FsEntry implements Comparable<FsEntry> {

  private String name;
  private String orderingName;
  private FileSystemFileType entryType;
  private PhotoChaosFileTypeDescriptor fileType;
  private FileSystemAttributes attributes;
  private boolean readError;

  private final static Pattern PATTERN_STRIP_NON_NUMBERS = Pattern.compile("[^\\d]");

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public FileSystemFileType getEntryType() {
    return entryType;
  }

  public void setEntryType(FileSystemFileType entryType) {
    this.entryType = entryType;
  }

  public PhotoChaosFileTypeDescriptor getFileType() {
    return fileType;
  }

  public void setFileType(PhotoChaosFileTypeDescriptor fileType) {
    this.fileType = fileType;
  }

  public FileSystemAttributes getAttributes() {
    return attributes;
  }

  public void setAttributes(FileSystemAttributes attributes) {
    this.attributes = attributes;
  }

  public boolean isReadError() {
    return readError;
  }

  public void setReadError(boolean readError) {
    this.readError = readError;
  }

  private boolean isDir() {
    return this.getEntryType() == FileSystemFileType.DIR;
  }

  @Override
  public int compareTo(FsEntry o) {
    if (o == null) {
      return -1;
    }
    if (this.isDir()) {
      if (o.isDir()) {
        return this.compareByName(o);
      } else {
        return -1;
      }
    } else {
      if (o.isDir()) {
        return 1;
      } else {
        return this.compareByName(o);
      }
    }
  }

  private int compareByName(FsEntry o) {
    if (this.name != null) {
      if (o.name != null) {
        if (this.orderingName == null) {
          this.orderingName = this.computeOrderingName();
        }
        if (o.orderingName == null) {
          o.orderingName = o.computeOrderingName();
        }
        if (this.orderingName != null) {
          return this.orderingName.compareTo(o.orderingName);
        } else {
          return -1;
        }
      } else {
        return -1;
      }
    } else {
      if (o.name != null) {
        return 1;
      } else {
        return 0;
      }
    }
  }

  private String computeOrderingName() {
    if (isDir()) {
      return this.name.toLowerCase();
    } else {
      return PATTERN_STRIP_NON_NUMBERS.matcher(this.name).replaceAll("");
    }
  }
}
