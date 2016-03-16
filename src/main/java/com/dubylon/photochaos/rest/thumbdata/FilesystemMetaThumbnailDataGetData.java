package com.dubylon.photochaos.rest.thumbdata;

import com.dubylon.photochaos.rest.generic.AbstractFilesystemMetadataData;

public class FilesystemMetaThumbnailDataGetData extends AbstractFilesystemMetadataData {

  private byte[] thumbnailData;

  public FilesystemMetaThumbnailDataGetData() {
  }

  public byte[] getThumbnailData() {
    return thumbnailData;
  }

  public void setThumbnailData(byte[] thumbnailData) {
    this.thumbnailData = thumbnailData;
  }
}
