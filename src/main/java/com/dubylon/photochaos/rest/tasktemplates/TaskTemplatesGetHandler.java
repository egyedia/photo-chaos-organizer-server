package com.dubylon.photochaos.rest.tasktemplates;

import com.dubylon.photochaos.model.tasktemplate.TaskTemplate;
import com.dubylon.photochaos.rest.IPhotoChaosHandler;
import com.dubylon.photochaos.rest.PCHandlerError;
import com.dubylon.photochaos.util.TaskTemplateJsonUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class TaskTemplatesGetHandler implements IPhotoChaosHandler {

  public TaskTemplatesGetHandler() {
  }

  @Override
  public TaskTemplatesGetData handleRequest(HttpServletRequest request) throws PCHandlerError {
    TaskTemplatesGetData response = new TaskTemplatesGetData();
    Map<String, TaskTemplate> taskTemplates = TaskTemplateJsonUtil.getTaskTemplates();
    for (TaskTemplate tt : taskTemplates.values()) {
      response.getTaskTemplates().add(tt);
    }
    return response;
  }
}
