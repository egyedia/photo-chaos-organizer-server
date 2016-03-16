package com.dubylon.photochaos.model.response.meta;

public abstract class ImageMeta {

  protected boolean widthRead;
  protected long width;
  protected boolean heightRead;
  protected long height;
  protected boolean orientationRead;
  protected int orientation;

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
}
