package com.dubylon.photochaos.rest.thumbonthefly;

import com.dubylon.photochaos.rest.generic.AbstractFilesystemMetadataData;

import java.awt.image.RenderedImage;

public class FilesystemOnTheFlyThumbnailDataGetData extends AbstractFilesystemMetadataData {

  private RenderedImage thumbnail;

  public FilesystemOnTheFlyThumbnailDataGetData() {
  }

  public RenderedImage getThumbnail() {
    return thumbnail;
  }

  public void setThumbnail(RenderedImage thumbnail) {
    this.thumbnail = thumbnail;
  }
}
