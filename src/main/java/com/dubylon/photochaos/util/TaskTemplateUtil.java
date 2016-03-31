package com.dubylon.photochaos.util;

import com.dubylon.photochaos.model.tasktemplate.TaskTemplate;
import com.dubylon.photochaos.model.tasktemplate.TaskTemplateParameter;
import com.dubylon.photochaos.task.PcoTaskTemplate;
import com.dubylon.photochaos.task.PcoTaskTemplateParameter;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public final class TaskTemplateUtil {

  private TaskTemplateUtil() {
  }

  public static List<TaskTemplate> getTaskTemplates() {
    List<TaskTemplate> ret = new ArrayList<>();

    Reflections classReflections = new Reflections("");
    Set<Class<?>> annotatedTaskTemplates =
        classReflections.getTypesAnnotatedWith(PcoTaskTemplate.class);
    for (Class c : annotatedTaskTemplates) {
      TaskTemplate tt = buildTaskTemplate(c);
      ret.add(tt);
    }
    Collections.sort(ret);
    return ret;
  }

  public static TaskTemplate buildTaskTemplate(Class c) {
    TaskTemplate tt = new TaskTemplate();
    PcoTaskTemplate taskAnnotation = (PcoTaskTemplate) c.getAnnotation(PcoTaskTemplate.class);
    tt.setClassName(c.getCanonicalName());
    tt.setName(taskAnnotation.languageKeyPrefix() + "name");
    tt.setDescription(taskAnnotation.languageKeyPrefix() + "description");

    FieldAnnotationsScanner fas = new FieldAnnotationsScanner();
    Reflections fieldReflections = new Reflections(c.getCanonicalName(), fas);
    Set<Field> fields =
        fieldReflections.getFieldsAnnotatedWith(PcoTaskTemplateParameter.class);
    List<TaskTemplateParameter> parameterList = new ArrayList<>();
    for (Field f : fields) {
      PcoTaskTemplateParameter fieldAnnotation = f.getAnnotation(PcoTaskTemplateParameter.class);
      TaskTemplateParameter ttp = new TaskTemplateParameter();
      String fieldName = f.getName();
      ttp.setName(fieldName);
      ttp.setType(fieldAnnotation.type());
      ttp.setDefaultValue(fieldAnnotation.defaultValue());
      ttp.setMandatory(fieldAnnotation.mandatory());
      ttp.setNameKey(taskAnnotation.languageKeyPrefix() + "field." + fieldName + ".name");
      ttp.setDescriptionKey(taskAnnotation.languageKeyPrefix() + "field." + fieldName + ".description");
      ttp.setOrder(fieldAnnotation.order());
      parameterList.add(ttp);
    }
    Collections.sort(parameterList);
    parameterList.forEach(p -> tt.getParameters().put(p.getName(), p));
    return tt;
  }

  public static TaskTemplate buildTaskTemplate(String className) {
    Class c = null;
    try {
      c = Class.forName(className);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    return buildTaskTemplate(c);
  }
}
