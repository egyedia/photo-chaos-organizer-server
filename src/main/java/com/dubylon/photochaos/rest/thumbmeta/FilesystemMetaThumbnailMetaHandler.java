package com.dubylon.photochaos.rest.thumbmeta;

import com.dubylon.photochaos.rest.generic.AbstractPCHandlerMetaThumbnail;
import com.dubylon.photochaos.rest.PCHandlerError;
import com.dubylon.photochaos.rest.PCHandlerResponse;
import com.dubylon.photochaos.rest.PCHandlerResponseError;

import javax.servlet.http.HttpServletRequest;

public class FilesystemMetaThumbnailMetaHandler extends AbstractPCHandlerMetaThumbnail {

  @Override
  public FilesystemMetaThumbnailMetaData doGet(HttpServletRequest request) throws PCHandlerError {
    FilesystemMetaThumbnailMetaData response = new FilesystemMetaThumbnailMetaData();
    handlePath(request, response);
    handleFile(request, response);
    handleMetadataObject(request, response);
    handleMetadata(request, response, false);
    return response;
  }

  @Override
  public PCHandlerResponse doPost(HttpServletRequest request) throws PCHandlerError {
    return PCHandlerResponseError.methodNotAllowed();
  }

  @Override
  public PCHandlerResponse doPut(HttpServletRequest request) throws PCHandlerError {
    return PCHandlerResponseError.methodNotAllowed();
  }

  @Override
  public PCHandlerResponse doDelete(HttpServletRequest request) throws PCHandlerError {
    return PCHandlerResponseError.methodNotAllowed();
  }

}
