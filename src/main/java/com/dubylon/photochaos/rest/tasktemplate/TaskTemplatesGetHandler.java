package com.dubylon.photochaos.rest.tasktemplate;

import com.dubylon.photochaos.rest.IPhotoChaosHandler;
import com.dubylon.photochaos.rest.PCHandlerError;
import com.dubylon.photochaos.task.copytodatedfolder.CopyToDatedFolderByFileNameTaskTemplate;

import javax.servlet.http.HttpServletRequest;

public class TaskTemplatesGetHandler implements IPhotoChaosHandler {

  public TaskTemplatesGetHandler() {
  }

  @Override
  public TaskTemplatesGetData handleRequest(HttpServletRequest request) throws PCHandlerError {
    TaskTemplatesGetData response = new TaskTemplatesGetData();
    response.getTaskTemplates().add(new CopyToDatedFolderByFileNameTaskTemplate());
    return response;
  }
}
