package com.dubylon.photochaos.rest.task;

import com.dubylon.photochaos.dao.TaskDefinitionDao;
import com.dubylon.photochaos.model.db.TaskDefinition;
import com.dubylon.photochaos.rest.PCHandlerError;
import com.dubylon.photochaos.rest.PCHandlerResponse;
import com.dubylon.photochaos.rest.generic.AbstractPCHandler;
import com.dubylon.photochaos.task.IPcoTask;
import org.reflections.Reflections;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;

public class TaskPreviewGetHandler extends AbstractPCHandler {

  public TaskPreviewGetHandler() {
  }

  @Override
  public TaskPreviewGetData handleRequest(HttpServletRequest request) throws PCHandlerError {
    long id = extractIdFromPathInfo(request, "Task id");
    long userId = getUserId(request);

    TaskDefinitionDao tdd = new TaskDefinitionDao();
    TaskDefinition td = tdd.getById(id, userId, TaskDefinition.class);

    if (td == null) {
      throw new PCHandlerError(PCHandlerResponse.NOT_FOUND, "NO_SUCH_TASK");
    } else {
      Class c = null;
      try {
        c = Class.forName(td.getClassName());
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }

      IPcoTask task = null;
      try {
        task = (IPcoTask) c.newInstance();
      } catch (InstantiationException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }

      Reflections classReflections = new Reflections("");
      for (String paramName : td.getParameters().keySet()) {
        String paramValue = td.getParameters().get(paramName);
        Field field = null;
        Object o = null;
        try {
          field = task.getClass().getDeclaredField(paramName);
          field.setAccessible(true);
        } catch (NoSuchFieldException e) {
          e.printStackTrace();
        }
        if (field != null) {
          Class<?> type = field.getType();
          /*System.out.println("------");
          System.out.println(paramName);
          System.out.println(paramValue);
          System.out.println(type);*/
          if (String.class.equals(type)) {
            try {
              field.set(task, paramValue);
            } catch (IllegalAccessException e) {
              e.printStackTrace();
            }
          }
        }
      }

      TaskPreviewGetData response = new TaskPreviewGetData();

      task.execute(response, false);

      response.setReport(null);
      return response;
    }
  }
}
