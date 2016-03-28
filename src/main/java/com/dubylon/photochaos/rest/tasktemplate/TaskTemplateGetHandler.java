package com.dubylon.photochaos.rest.tasktemplate;

import com.dubylon.photochaos.Constants;
import com.dubylon.photochaos.model.tasktemplate.TaskTemplate;
import com.dubylon.photochaos.rest.IPhotoChaosHandler;
import com.dubylon.photochaos.rest.PCHandlerError;
import com.dubylon.photochaos.util.TaskTemplateUtil;

import javax.servlet.http.HttpServletRequest;

public class TaskTemplateGetHandler implements IPhotoChaosHandler {

  public TaskTemplateGetHandler() {
  }

  @Override
  public TaskTemplateGetData handleRequest(HttpServletRequest request) throws PCHandlerError {
    TaskTemplateGetData response = new TaskTemplateGetData();
    String path = request.getPathInfo();
    if (path != null && path.indexOf(Constants.SLASH) == 0) {
      String className = path.substring(Constants.SLASH.length());
      TaskTemplate taskTemplate = TaskTemplateUtil.buildTaskTemplate(className);
      response.setTaskTemplate(taskTemplate);
    }
    return response;
  }
}
