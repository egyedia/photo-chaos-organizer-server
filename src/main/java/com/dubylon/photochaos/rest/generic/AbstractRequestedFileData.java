package com.dubylon.photochaos.rest.generic;

import java.io.File;

public class AbstractRequestedFileData extends AbstractRequestedPathData {

  private File file;

  public File getFile() {
    return file;
  }

  public void setFile(File file) {
    this.file = file;
  }
}
