package com.dubylon.photochaos.util;

public class PhotoChaosFileTypeDescriptor {

  private PhotoChaosFileType fileType;
  private boolean canHaveMeta;
  private boolean canHaveOnTheFlyImageThumb;
  private boolean canShowThumb;

  public PhotoChaosFileTypeDescriptor(PhotoChaosFileType fileType, boolean canHaveMeta, boolean
      canHaveOnTheFlyImageThumb, boolean canShowThumb) {
    this.canHaveMeta = canHaveMeta;
    this.canHaveOnTheFlyImageThumb = canHaveOnTheFlyImageThumb;
    this.fileType = fileType;
    this.canShowThumb = canShowThumb;
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

  public PhotoChaosFileType getFileType() {
    return fileType;
  }

  public void setFileType(PhotoChaosFileType fileType) {
    this.fileType = fileType;
  }
}
