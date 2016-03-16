package com.dubylon.photochaos.model.response.meta;

public class ImageExtractedMeta {

  private ThumbnailMeta thumbnail;
  private BigImageMeta image;

  public ThumbnailMeta getThumbnail() {
    return thumbnail;
  }

  public void setThumbnail(ThumbnailMeta thumbnail) {
    this.thumbnail = thumbnail;
  }

  public BigImageMeta getImage() {
    return image;
  }

  public void setImage(BigImageMeta image) {
    this.image = image;
  }
}
