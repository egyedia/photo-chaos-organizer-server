package com.dubylon.photochaos.task;

import com.dubylon.photochaos.model.tasktemplate.TaskTemplateParameterType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface PcoTaskTemplateParameter {
  TaskTemplateParameterType type();

  boolean mandatory();

  String defaultValue();
}
