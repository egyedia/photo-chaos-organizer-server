package com.dubylon.photochaos.model.response.meta;

public class BigImageMeta extends ImageMeta{

  private boolean dateTimeOriginalRead;
  private long dateTimeOriginal;

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
}
