package com.dubylon.photochaos.rest.task;

import com.dubylon.photochaos.handler.PCResponseWriter;
import com.dubylon.photochaos.rest.PCHandlerError;
import com.dubylon.photochaos.servlet.AbstractPhotoChaosServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TaskServlet extends AbstractPhotoChaosServlet {

  public static final String PREVIEW = "preview";
  public static final String RUN = "run";

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String action = request.getParameter("action");
    Boolean runTask = null;
    if (PREVIEW.equals(action)) {
      runTask = Boolean.FALSE;
    } else if (RUN.equals(action)) {
      runTask = Boolean.TRUE;
    }

    if (runTask != null) {
      TaskPreviewOrRunGetHandler h = new TaskPreviewOrRunGetHandler(runTask);
      try {
        TaskPreviewOrRunGetData pcResponse = h.handleRequest(request);
        PCResponseWriter.writeSuccess(response, pcResponse, pcResponse);
      } catch (PCHandlerError err) {
        PCResponseWriter.writeError(response, err);
      }
    } else {
      TaskGetHandler h = new TaskGetHandler();
      try {
        TaskGetData pcResponse = h.handleRequest(request);
        PCResponseWriter.writeSuccess(response, pcResponse, pcResponse.getTask());
      } catch (PCHandlerError err) {
        PCResponseWriter.writeError(response, err);
      }
    }
  }

}
