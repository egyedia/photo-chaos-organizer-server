package com.dubylon.photochaos.rest.thumbmeta;

import com.dubylon.photochaos.rest.PCHandlerError;
import com.dubylon.photochaos.rest.generic.AbstractPCHandlerMetaThumbnail;

import javax.servlet.http.HttpServletRequest;

public class FilesystemMetaThumbnailMetaGetHandler extends AbstractPCHandlerMetaThumbnail {

  @Override
  public FilesystemMetaThumbnailMetaGetData handleRequest(HttpServletRequest request) throws PCHandlerError {
    FilesystemMetaThumbnailMetaGetData response = new FilesystemMetaThumbnailMetaGetData();
    handlePath(request, response);
    handleFile(request, response);
    handleMetadataObject(request, response);
    handleMetadata(request, response);
    return response;
  }


}
