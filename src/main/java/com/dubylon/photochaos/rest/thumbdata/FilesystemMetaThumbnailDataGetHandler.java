package com.dubylon.photochaos.rest.thumbdata;

import com.dubylon.photochaos.rest.PCHandlerError;
import com.dubylon.photochaos.rest.generic.AbstractPCHandlerMetaThumbnail;
import com.dubylon.photochaos.rest.thumbmeta.FilesystemMetaThumbnailMetaGetData;

import javax.servlet.http.HttpServletRequest;

public class FilesystemMetaThumbnailDataGetHandler extends AbstractPCHandlerMetaThumbnail {

  @Override
  public FilesystemMetaThumbnailMetaGetData handleRequest(HttpServletRequest request) throws PCHandlerError {
    FilesystemMetaThumbnailMetaGetData response = new FilesystemMetaThumbnailMetaGetData();
    handlePath(request, response);
    handleFile(request, response);
    handleMetadataObject(request, response);
    handleMetadata(request, response, true);
    return response;
  }

}
