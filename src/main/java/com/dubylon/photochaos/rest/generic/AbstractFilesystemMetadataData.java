package com.dubylon.photochaos.rest.generic;

import com.drew.metadata.Metadata;

public class AbstractFilesystemMetadataData extends AbstractRequestedFileData {

  protected Metadata metadata;

  public AbstractFilesystemMetadataData() {
  }

  public Metadata getMetadata() {
    return metadata;
  }

  public void setMetadata(Metadata metadata) {
    this.metadata = metadata;
  }

}
