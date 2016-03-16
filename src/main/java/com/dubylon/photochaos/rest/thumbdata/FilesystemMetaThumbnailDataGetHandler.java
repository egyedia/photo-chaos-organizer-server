package com.dubylon.photochaos.rest.thumbdata;

import com.dubylon.photochaos.rest.PCHandlerError;
import com.dubylon.photochaos.rest.generic.AbstractPCHandlerMetaThumbnail;
import com.dubylon.photochaos.rest.thumbmeta.FilesystemMetaThumbnailMetaGetData;

import javax.servlet.http.HttpServletRequest;

public class FilesystemMetaThumbnailDataGetHandler extends AbstractPCHandlerMetaThumbnail {

  @Override
  public FilesystemMetaThumbnailDataGetData handleRequest(HttpServletRequest request) throws PCHandlerError {
    FilesystemMetaThumbnailDataGetData response = new FilesystemMetaThumbnailDataGetData();
    handlePath(request, response);
    handleFile(request, response);
    handleMetadataObject(request, response);
    handleMetadataThumbnail(request, response);
    return response;
  }

}
