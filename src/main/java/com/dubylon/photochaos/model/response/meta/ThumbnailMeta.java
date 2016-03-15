package com.dubylon.photochaos.model.response.meta;

public class ThumbnailMeta {

  private boolean exifThumbReadable;
  private boolean widthRead;
  private long width;
  private boolean heightRead;
  private long height;
  private boolean orientationRead;
  private int orientation;
  private boolean dateTimeOriginalRead;
  private long dateTimeOriginal;

  public boolean isWidthRead() {
    return widthRead;
  }

  public void setWidthRead(boolean widthRead) {
    this.widthRead = widthRead;
  }

  public long getWidth() {
    return width;
  }

  public void setWidth(long width) {
    this.width = width;
  }

  public boolean isHeightRead() {
    return heightRead;
  }

  public void setHeightRead(boolean heightRead) {
    this.heightRead = heightRead;
  }

  public long getHeight() {
    return height;
  }

  public void setHeight(long height) {
    this.height = height;
  }

  public boolean isOrientationRead() {
    return orientationRead;
  }

  public void setOrientationRead(boolean orientationRead) {
    this.orientationRead = orientationRead;
  }

  public int getOrientation() {
    return orientation;
  }

  public void setOrientation(int orientation) {
    this.orientation = orientation;
  }

  public boolean isDateTimeOriginalRead() {
    return dateTimeOriginalRead;
  }

  public void setDateTimeOriginalRead(boolean dateTimeOriginalRead) {
    this.dateTimeOriginalRead = dateTimeOriginalRead;
  }

  public long getDateTimeOriginal() {
    return dateTimeOriginal;
  }

  public void setDateTimeOriginal(long dateTimeOriginal) {
    this.dateTimeOriginal = dateTimeOriginal;
  }

  public boolean isExifThumbReadable() {
    return exifThumbReadable;
  }

  public void setExifThumbReadable(boolean exifThumbReadable) {
    this.exifThumbReadable = exifThumbReadable;
  }
}
