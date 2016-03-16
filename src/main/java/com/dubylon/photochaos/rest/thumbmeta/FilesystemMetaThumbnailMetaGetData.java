package com.dubylon.photochaos.rest.thumbmeta;

import com.dubylon.photochaos.model.response.meta.ImageExtractedMeta;
import com.dubylon.photochaos.rest.generic.AbstractFilesystemMetadataData;

public class FilesystemMetaThumbnailMetaGetData extends AbstractFilesystemMetadataData {

  private ImageExtractedMeta extractedMeta;

  public FilesystemMetaThumbnailMetaGetData() {
  }

  public ImageExtractedMeta getExtractedMeta() {
    return extractedMeta;
  }

  public void setExtractedMeta(ImageExtractedMeta extractedMeta) {
    this.extractedMeta = extractedMeta;
  }
}
