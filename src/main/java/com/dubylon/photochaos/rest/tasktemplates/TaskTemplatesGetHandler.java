package com.dubylon.photochaos.rest.tasktemplates;

import com.dubylon.photochaos.model.tasktemplate.TaskTemplate;
import com.dubylon.photochaos.rest.PhotoChaosHandler;
import com.dubylon.photochaos.rest.PCHandlerError;
import com.dubylon.photochaos.util.TaskTemplateUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class TaskTemplatesGetHandler implements PhotoChaosHandler {

  public TaskTemplatesGetHandler() {
  }

  @Override
  public TaskTemplatesGetData handleRequest(HttpServletRequest request) throws PCHandlerError {
    TaskTemplatesGetData response = new TaskTemplatesGetData();
    List<TaskTemplate> taskTemplates = TaskTemplateUtil.getTaskTemplates();
    for (TaskTemplate tt : taskTemplates) {
      response.getTaskTemplates().add(tt);
    }
    return response;
  }
}
