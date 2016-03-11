package com.dubylon.photochaos.task.copytodatedfolder;

import com.dubylon.photochaos.rest.IPhotoChaosHandler;
import com.dubylon.photochaos.rest.PCHandlerError;

import javax.servlet.http.HttpServletRequest;

public class CopyToDatedFolderGetHandler implements IPhotoChaosHandler {

  public CopyToDatedFolderGetHandler() {
  }

  @Override
  public CopyToDatedFolderGetData handleRequest(HttpServletRequest request) throws PCHandlerError {
    CopyToDatedFolderGetData response = new CopyToDatedFolderGetData();

    CopyFilesToFoldersByCaptureDateFromFileNameTask task = new CopyFilesToFoldersByCaptureDateFromFileNameTask(response, false);
    task.execute();
    return response;
  }
}
