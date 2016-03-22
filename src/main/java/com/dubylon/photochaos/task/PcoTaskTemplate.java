package com.dubylon.photochaos.task;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface PcoTaskTemplate {
  String languageKeyPrefix();
}
