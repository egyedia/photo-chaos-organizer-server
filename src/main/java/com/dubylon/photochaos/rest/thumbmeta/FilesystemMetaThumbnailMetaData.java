package com.dubylon.photochaos.rest.thumbmeta;

import com.drew.metadata.Metadata;
import com.dubylon.photochaos.model.meta.meta.ThumbnailMeta;
import com.dubylon.photochaos.rest.generic.AbstractRequestedPathData;

import java.io.File;

public class FilesystemMetaThumbnailMetaData extends AbstractRequestedPathData {

  private Metadata metadata;
  private ThumbnailMeta extractedMeta;
  private File image;
  private byte[] thumbnailData;

  public FilesystemMetaThumbnailMetaData() {
  }

  public ThumbnailMeta getExtractedMeta() {
    return extractedMeta;
  }

  public void setExtractedMeta(ThumbnailMeta extractedMeta) {
    this.extractedMeta = extractedMeta;
  }

  public File getImage() {
    return image;
  }

  public void setImage(File image) {
    this.image = image;
  }

  public Metadata getMetadata() {
    return metadata;
  }

  public void setMetadata(Metadata metadata) {
    this.metadata = metadata;
  }

  public byte[] getThumbnailData() {
    return thumbnailData;
  }

  public void setThumbnailData(byte[] thumbnailData) {
    this.thumbnailData = thumbnailData;
  }
}
