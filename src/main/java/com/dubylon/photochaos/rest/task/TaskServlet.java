package com.dubylon.photochaos.rest.task;

import com.dubylon.photochaos.handler.PCResponseWriter;
import com.dubylon.photochaos.rest.PCHandlerError;
import com.dubylon.photochaos.rest.user.UserGetData;
import com.dubylon.photochaos.rest.user.UserGetHandler;
import com.dubylon.photochaos.servlet.AbstractPhotoChaosServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TaskServlet extends AbstractPhotoChaosServlet {

  public static final String PREVIEW = "preview";

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String action = request.getParameter("action");
    if (PREVIEW.equals(action)) {
      TaskPreviewGetHandler h = new TaskPreviewGetHandler();
      try {
        TaskPreviewGetData pcResponse = h.handleRequest(request);
        PCResponseWriter.writeSuccess(response, pcResponse, pcResponse.getReport());
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
