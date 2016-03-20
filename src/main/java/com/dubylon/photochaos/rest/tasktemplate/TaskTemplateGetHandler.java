package com.dubylon.photochaos.rest.tasktemplate;

import com.dubylon.photochaos.Constants;
import com.dubylon.photochaos.model.tasktemplate.TaskTemplate;
import com.dubylon.photochaos.rest.IPhotoChaosHandler;
import com.dubylon.photochaos.rest.PCHandlerError;
import com.dubylon.photochaos.util.TaskTemplateJsonUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class TaskTemplateGetHandler implements IPhotoChaosHandler {

  public TaskTemplateGetHandler() {
  }

  @Override
  public TaskTemplateGetData handleRequest(HttpServletRequest request) throws PCHandlerError {
    TaskTemplateGetData response = new TaskTemplateGetData();
    String path = request.getPathInfo();
    if (path != null && path.indexOf(Constants.SLASH) == 0) {
      path = path.substring(Constants.SLASH.length());
    }
    Map<String, TaskTemplate> taskTemplates = TaskTemplateJsonUtil.getTaskTemplates();
    TaskTemplate taskTemplate = taskTemplates.get(path);
    response.setTaskTemplate(taskTemplate);
    return response;
  }
}
