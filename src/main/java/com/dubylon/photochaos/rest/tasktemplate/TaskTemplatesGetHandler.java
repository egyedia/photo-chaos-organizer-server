package com.dubylon.photochaos.rest.tasktemplate;

import com.dubylon.photochaos.Constants;
import com.dubylon.photochaos.model.tasktemplate.TaskTemplate;
import com.dubylon.photochaos.rest.IPhotoChaosHandler;
import com.dubylon.photochaos.rest.PCHandlerError;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

public class TaskTemplatesGetHandler implements IPhotoChaosHandler {


  public TaskTemplatesGetHandler() {
  }

  @Override
  public TaskTemplatesGetData handleRequest(HttpServletRequest request) throws PCHandlerError {
    TaskTemplatesGetData response = new TaskTemplatesGetData();
    String path = request.getPathInfo();
    if (path != null && path.indexOf(Constants.SLASH) == 0) {
      path = path.substring(Constants.SLASH.length());
    }

    List<String> templateLines = null;
    try {
      templateLines = IOUtils.readLines(ClassLoader.getSystemResourceAsStream(path));
    } catch (IOException e) {
      e.printStackTrace();
    }
    if (templateLines != null) {
      String template = StringUtils.join(templateLines, "");
      ObjectMapper mapper = new ObjectMapper();
      try {
        TaskTemplate tt = mapper.readValue(template, TaskTemplate.class);
        tt.setOriginalSource(path);
        response.setTaskTemplate(tt);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return response;
  }
}
