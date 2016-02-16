package com.dubylon.photochaos.handler;

import javax.servlet.http.HttpServletRequest;

public class FilesystemMetaThumbnailMetaHandler extends AsbtractMetaThumbnailHandler {

  @Override
  public PCResponseObject doGet(HttpServletRequest request) {
    PCResponseObject response = handlePath(request);
    if (response.isSuccess()) {
      response = handleFile(request, response);
      if (response.isSuccess()) {
        response = handleMetadataObject(request, response);
        if (response.isSuccess()) {
          response = handleMetadata(request, response, false);
        }
      }
    }
    return response;
  }

  @Override
  public PCResponseObject doPost(HttpServletRequest request) {
    return PCResponseObject.methodNotAllowed();
  }

  @Override
  public PCResponseObject doPut(HttpServletRequest request) {
    return null;
  }

  @Override
  public PCResponseObject doDelete(HttpServletRequest request) {
    return PCResponseObject.methodNotAllowed();
  }

}
