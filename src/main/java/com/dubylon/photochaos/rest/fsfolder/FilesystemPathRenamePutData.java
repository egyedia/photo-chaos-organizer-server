package com.dubylon.photochaos.rest.fsfolder;

import com.dubylon.photochaos.rest.generic.AbstractRequestedPathData;

public class FilesystemPathRenamePutData extends AbstractRequestedPathData {

  private boolean renamed;
  private String path;

  public FilesystemPathRenamePutData() {
  }

  public boolean isRenamed() {
    return renamed;
  }

  public void setRenamed(boolean renamed) {
    this.renamed = renamed;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }
}
