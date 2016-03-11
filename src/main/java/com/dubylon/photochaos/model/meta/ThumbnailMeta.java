package com.dubylon.photochaos.model.meta;

public class ThumbnailMeta {

  private long width;
  private long height;
  private int orientation;
  private long dateTimeOriginal;

  public long getDateTimeOriginal() {
    return dateTimeOriginal;
  }

  public void setDateTimeOriginal(long dateTimeOriginal) {
    this.dateTimeOriginal = dateTimeOriginal;
  }

  public long getHeight() {
    return height;
  }

  public void setHeight(long height) {
    this.height = height;
  }

  public int getOrientation() {
    return orientation;
  }

  public void setOrientation(int orientation) {
    this.orientation = orientation;
  }

  public long getWidth() {
    return width;
  }

  public void setWidth(long width) {
    this.width = width;
  }
}
