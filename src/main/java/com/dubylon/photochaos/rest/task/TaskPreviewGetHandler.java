package com.dubylon.photochaos.rest.task;

import com.dubylon.photochaos.dao.TaskDefinitionDao;
import com.dubylon.photochaos.model.db.TaskDefinition;
import com.dubylon.photochaos.rest.PCHandlerError;
import com.dubylon.photochaos.rest.PCHandlerResponse;
import com.dubylon.photochaos.rest.generic.AbstractPCHandler;
import com.dubylon.photochaos.task.IPcoTask;
import com.dubylon.photochaos.util.TaskUtil;

import javax.servlet.http.HttpServletRequest;

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
      IPcoTask task = TaskUtil.buildTaskWithParameters(td.getClassName(), td.getParameters());
      TaskPreviewGetData response = new TaskPreviewGetData();
      task.execute(response, false);
      return response;
    }
  }
}
