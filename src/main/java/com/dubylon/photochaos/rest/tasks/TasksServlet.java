package com.dubylon.photochaos.rest.tasks;

import com.dubylon.photochaos.handler.PCResponseWriter;
import com.dubylon.photochaos.rest.PCHandlerError;
import com.dubylon.photochaos.servlet.AbstractPhotoChaosServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TasksServlet extends AbstractPhotoChaosServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    TasksGetHandler h = new TasksGetHandler();
    try {
      TasksGetData pcResponse = h.handleRequest(request);
      Map<String, String> headers = new HashMap<>();
      PCResponseWriter.writeSuccess(response, pcResponse, headers, pcResponse.getTasks());
    } catch (PCHandlerError err) {
      PCResponseWriter.writeError(response, err);
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    TasksPostHandler h = new TasksPostHandler();
    try {
      TasksPostData pcResponse = h.handleRequest(request);
      Map<String, String> headers = new HashMap<>();
      String newLocation = request.getRequestURL().append("/").append(pcResponse.getId()).toString();
      headers.put("Location", newLocation);
      PCResponseWriter.writeSuccess(response, pcResponse, headers, pcResponse.getCreatedObject());
    } catch (PCHandlerError err) {
      PCResponseWriter.writeError(response, err);
    }
  }

}
