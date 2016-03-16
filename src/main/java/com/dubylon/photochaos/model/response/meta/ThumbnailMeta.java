package com.dubylon.photochaos.model.response.meta;

public class ThumbnailMeta extends ImageMeta {

  private boolean exifThumbReadable;

  public boolean isExifThumbReadable() {
    return exifThumbReadable;
  }

  public void setExifThumbReadable(boolean exifThumbReadable) {
    this.exifThumbReadable = exifThumbReadable;
  }
}
