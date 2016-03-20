package com.dubylon.photochaos.util;

import com.dubylon.photochaos.model.tasktemplate.TaskTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public final class TaskTemplateJsonUtil {

  private static final Pattern PATTERN = Pattern.compile(".*\\.json");
  private static final String FOLDER = "tasktemplates";

  private TaskTemplateJsonUtil() {
  }

  public static Map<String, TaskTemplate> getTaskTemplates() {
    Map<String, TaskTemplate> ret = new HashMap<>();
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
            ret.put(tt.getClassName(), tt);
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
    }
    return ret;
  }
}
