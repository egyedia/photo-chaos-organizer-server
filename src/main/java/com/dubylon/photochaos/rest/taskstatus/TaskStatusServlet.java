package com.dubylon.photochaos.rest.taskstatus;

import com.dubylon.photochaos.handler.PCResponseWriter;
import com.dubylon.photochaos.rest.PCHandlerError;
import com.dubylon.photochaos.rest.task.TaskGetData;
import com.dubylon.photochaos.rest.task.TaskGetHandler;
import com.dubylon.photochaos.rest.task.TaskPreviewOrRunGetData;
import com.dubylon.photochaos.rest.task.TaskPreviewOrRunGetHandler;
import com.dubylon.photochaos.servlet.AbstractPhotoChaosServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TaskStatusServlet extends AbstractPhotoChaosServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    TaskPreviewOrRunStatusGetHandler h = new TaskPreviewOrRunStatusGetHandler();
    try {
      TaskPreviewOrRunStatusGetData pcResponse = h.handleRequest(request);
      PCResponseWriter.writeSuccess(response, pcResponse, pcResponse);
    } catch (PCHandlerError err) {
      PCResponseWriter.writeError(response, err);
    }
  }

}
