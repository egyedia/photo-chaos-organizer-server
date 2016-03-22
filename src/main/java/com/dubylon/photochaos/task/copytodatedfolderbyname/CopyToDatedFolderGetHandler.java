package com.dubylon.photochaos.task.copytodatedfolderbyname;

import com.dubylon.photochaos.rest.IPhotoChaosHandler;
import com.dubylon.photochaos.rest.PCHandlerError;

import javax.servlet.http.HttpServletRequest;

public class CopyToDatedFolderGetHandler implements IPhotoChaosHandler {

  public CopyToDatedFolderGetHandler() {
  }

  @Override
  public CopyToDatedFolderGetData handleRequest(HttpServletRequest request) throws PCHandlerError {
    CopyToDatedFolderGetData response = new CopyToDatedFolderGetData();
    String perf = request.getParameter("perform");
    boolean perform = perf != null && "true".equals(perf);

    CopyFilesToFoldersByDateFromFileNameTask task = new CopyFilesToFoldersByDateFromFileNameTask(response, perform);
    task.execute();
    return response;
  }
}
