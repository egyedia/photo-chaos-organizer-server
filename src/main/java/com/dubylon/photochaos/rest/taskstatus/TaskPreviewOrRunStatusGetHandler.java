package com.dubylon.photochaos.rest.taskstatus;

import com.dubylon.photochaos.rest.PCHandlerError;
import com.dubylon.photochaos.rest.PCHandlerResponse;
import com.dubylon.photochaos.rest.generic.AbstractPCHandler;
import com.dubylon.photochaos.task.TaskCentralizer;
import com.dubylon.photochaos.task.TaskRunInstance;

import javax.servlet.http.HttpServletRequest;

public class TaskPreviewOrRunStatusGetHandler extends AbstractPCHandler {

  public TaskPreviewOrRunStatusGetHandler() {

  }

  @Override
  public TaskPreviewOrRunStatusGetData handleRequest(HttpServletRequest request) throws PCHandlerError {
    long id = extractIdFromPathInfo(request, "Task id");
    long userId = getUserId(request);
    // we could test the launcher user
    TaskRunInstance instance = TaskCentralizer.getInstance().getTaskInstance(id);

    if (instance == null) {
      throw new PCHandlerError(PCHandlerResponse.NOT_FOUND, "NO_SUCH_RUNNING_TASK");
    } else {
      TaskPreviewOrRunStatusGetData response = new TaskPreviewOrRunStatusGetData();
      response.setReports(instance.getTask().getStatus().getReports());
      response.setInfo(instance.getInstance());
      return response;
    }
  }
}
