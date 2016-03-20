package com.dubylon.photochaos.rest.tasktemplate;

import com.dubylon.photochaos.model.tasktemplate.TaskTemplate;
import com.dubylon.photochaos.rest.IPhotoChaosHandler;
import com.dubylon.photochaos.rest.PCHandlerError;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class TaskTemplatesGetListHandler implements IPhotoChaosHandler {

  private static final Pattern PATTERN = Pattern.compile(".*\\.json");
  private static final String FOLDER = "tasktemplates";

  public TaskTemplatesGetListHandler() {
  }

  @Override
  public TaskTemplatesGetListData handleRequest(HttpServletRequest request) throws PCHandlerError {
    TaskTemplatesGetListData response = new TaskTemplatesGetListData();

    Set<String> taskTemplates = new Reflections(FOLDER, new ResourcesScanner()).getResources(PATTERN);
    if (taskTemplates != null) {
      for (String taskTemplate : taskTemplates) {
        List<String> templateLines = null;
        try {
          templateLines = IOUtils.readLines(ClassLoader.getSystemResourceAsStream(taskTemplate));
        } catch (IOException e) {
          e.printStackTrace();
        }
        if (templateLines != null) {
          String template = StringUtils.join(templateLines, "");
          ObjectMapper mapper = new ObjectMapper();
          try {
            TaskTemplate tt = mapper.readValue(template, TaskTemplate.class);
            tt.setOriginalSource(taskTemplate);
            response.getTaskTemplates().add(tt);
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
    }
    return response;
  }
}
