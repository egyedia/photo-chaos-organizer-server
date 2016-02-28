package com.dubylon.photochaos.util;

public class FileSystemAttributes {
  private long size;
  private long lastModTime;
  private boolean hidden;

  public FileSystemAttributes(long size, long lastModTime, boolean hidden) {
    this.size = size;
    this.lastModTime = lastModTime;
    this.hidden = hidden;
  }

  public boolean isHidden() {
    return hidden;
  }

  public void setHidden(boolean hidden) {
    this.hidden = hidden;
  }

  public long getLastModTime() {
    return lastModTime;
  }

  public void setLastModTime(long lastModTime) {
    this.lastModTime = lastModTime;
  }

  public long getSize() {
    return size;
  }

  public void setSize(long size) {
    this.size = size;
  }
}
