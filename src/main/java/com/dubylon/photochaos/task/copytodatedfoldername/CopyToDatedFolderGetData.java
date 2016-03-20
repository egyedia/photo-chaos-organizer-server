package com.dubylon.photochaos.task.copytodatedfoldername;

import com.dubylon.photochaos.rest.PCHandlerResponseData;

import java.util.ArrayList;
import java.util.List;

public class CopyToDatedFolderGetData extends PCHandlerResponseData {

  private List<CopyFileOperation> operations;
  private String sourcePath;
  private String destinationPath;
  private boolean sourceOk;
  private boolean destinationOk;
  private long copiedCount;
  private long skippedCount;

  public CopyToDatedFolderGetData() {
    operations = new ArrayList<>();
  }

  public List<CopyFileOperation> getOperations() {
    return operations;
  }

  public String getSourcePath() {
    return sourcePath;
  }

  public void setSourcePath(String sourcePath) {
    this.sourcePath = sourcePath;
  }

  public String getDestinationPath() {
    return destinationPath;
  }

  public void setDestinationPath(String destinationPath) {
    this.destinationPath = destinationPath;
  }

  public boolean isSourceOk() {
    return sourceOk;
  }

  public void setSourceOk(boolean sourceOk) {
    this.sourceOk = sourceOk;
  }

  public boolean isDestinationOk() {
    return destinationOk;
  }

  public void setDestinationOk(boolean destinationOk) {
    this.destinationOk = destinationOk;
  }

  public long getCopiedCount() {
    return copiedCount;
  }

  public void setCopiedCount(long copiedCount) {
    this.copiedCount = copiedCount;
  }

  public long getSkippedCount() {
    return skippedCount;
  }

  public void setSkippedCount(long skippedCount) {
    this.skippedCount = skippedCount;
  }
}
