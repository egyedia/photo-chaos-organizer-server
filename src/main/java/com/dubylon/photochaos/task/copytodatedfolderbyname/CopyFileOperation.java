package com.dubylon.photochaos.task.copytodatedfolderbyname;

public class CopyFileOperation {

  private String fileName;

  private String destinationFolderName;

  private boolean copied;

  private String reason;

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public boolean isCopied() {
    return copied;
  }

  public void setCopied(boolean copied) {
    this.copied = copied;
  }

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public String getDestinationFolderName() {
    return destinationFolderName;
  }

  public void setDestinationFolderName(String destinationFolderName) {
    this.destinationFolderName = destinationFolderName;
  }
}

