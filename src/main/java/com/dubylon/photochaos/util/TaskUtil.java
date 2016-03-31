package com.dubylon.photochaos.util;

import com.dubylon.photochaos.task.IPcoTask;

import java.lang.reflect.Field;
import java.util.Map;

public final class TaskUtil {
  private TaskUtil() {
  }

  public static IPcoTask buildTaskWithParameters(String className, Map<String, String> parameters) {
    //TODO handle errors, make it fool-proof
    IPcoTask task = null;

    Class c = null;
    try {
      c = Class.forName(className);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }

    if (c != null) {
      try {
        task = (IPcoTask) c.newInstance();
      } catch (InstantiationException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
    }

    if (task != null) {
      for (String paramName : parameters.keySet()) {
        String paramValue = parameters.get(paramName);
        Field field = null;
        try {
          field = task.getClass().getDeclaredField(paramName);
          field.setAccessible(true);
        } catch (NoSuchFieldException e) {
          e.printStackTrace();
        }
        if (field != null) {
          Class<?> type = field.getType();
          if (String.class.equals(type)) {
            try {
              field.set(task, paramValue);
            } catch (IllegalAccessException e) {
              e.printStackTrace();
            }
          } else if (PcoEnum.class.isAssignableFrom(type)) {
            Object[] enumConstants = type.getEnumConstants();
            for(Object o : enumConstants) {
              if (((PcoEnum)o).getValue().equals(paramValue)) {
                try {
                  field.set(task, o);
                } catch (IllegalAccessException e) {
                  e.printStackTrace();
                }
              }
            }
          }
        }
      }
    }
    return task;
  }
}
