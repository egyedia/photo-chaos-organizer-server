package com.dubylon.photochaos.app;

import com.dubylon.photochaos.util.PhotoChaosFileType;

import java.util.List;

public class AppConfigFileType {
  private PhotoChaosFileType fileType;
  private boolean canHaveMeta;
  private boolean canHaveOnTheFlyImageThumb;
  private boolean canShowThumb;
  private List<String> extensions;

  public AppConfigFileType() {
  }

  public PhotoChaosFileType getFileType() {
    return fileType;
  }

  public void setFileType(PhotoChaosFileType fileType) {
    this.fileType = fileType;
  }

  public boolean isCanHaveMeta() {
    return canHaveMeta;
  }

  public void setCanHaveMeta(boolean canHaveMeta) {
    this.canHaveMeta = canHaveMeta;
  }

  public boolean isCanHaveOnTheFlyImageThumb() {
    return canHaveOnTheFlyImageThumb;
  }

  public void setCanHaveOnTheFlyImageThumb(boolean canHaveOnTheFlyImageThumb) {
    this.canHaveOnTheFlyImageThumb = canHaveOnTheFlyImageThumb;
  }

  public boolean isCanShowThumb() {
    return canShowThumb;
  }

  public void setCanShowThumb(boolean canShowThumb) {
    this.canShowThumb = canShowThumb;
  }

  public List<String> getExtensions() {
    return extensions;
  }

  public void setExtensions(List<String> extensions) {
    this.extensions = extensions;
  }
}
