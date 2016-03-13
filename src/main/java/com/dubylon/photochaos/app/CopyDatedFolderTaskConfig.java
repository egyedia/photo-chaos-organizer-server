package com.dubylon.photochaos.app;

public class CopyDatedFolderTaskConfig extends TaskConfig {

  private String sourceFolder;
  private String destinationFolder;
  private String destinationFolderSuffix;

  public String getSourceFolder() {
    return sourceFolder;
  }

  public void setSourceFolder(String sourceFolder) {
    this.sourceFolder = sourceFolder;
  }

  public String getDestinationFolder() {
    return destinationFolder;
  }

  public void setDestinationFolder(String destinationFolder) {
    this.destinationFolder = destinationFolder;
  }

  public String getDestinationFolderSuffix() {
    return destinationFolderSuffix;
  }

  public void setDestinationFolderSuffix(String destinationFolderSuffix) {
    this.destinationFolderSuffix = destinationFolderSuffix;
  }
}
