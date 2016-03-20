package com.dubylon.photochaos.rest.tasktemplates;

import com.dubylon.photochaos.handler.PCResponseWriter;
import com.dubylon.photochaos.rest.PCHandlerError;
import com.dubylon.photochaos.servlet.AbstractPhotoChaosServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TaskTemplatesServlet extends AbstractPhotoChaosServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    TaskTemplatesGetHandler h = new TaskTemplatesGetHandler();
    try {
      TaskTemplatesGetData pcResponse = h.handleRequest(request);
      PCResponseWriter.writeSuccess(response, pcResponse, pcResponse.getTaskTemplates());
    } catch (PCHandlerError err) {
      PCResponseWriter.writeError(response, err);
    }
  }
}
