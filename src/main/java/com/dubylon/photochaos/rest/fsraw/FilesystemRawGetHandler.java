package com.dubylon.photochaos.rest.fsraw;

import com.dubylon.photochaos.rest.PCHandlerError;
import com.dubylon.photochaos.rest.generic.AbstractPCHandlerFile;

import javax.servlet.http.HttpServletRequest;

public class FilesystemRawGetHandler extends AbstractPCHandlerFile {

  @Override
  public FilesystemRawGetData handleRequest(HttpServletRequest request) throws PCHandlerError {
    FilesystemRawGetData response = new FilesystemRawGetData();
    handlePath(request, response);
    handleFile(request, response);
    return response;
  }

}
