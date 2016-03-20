package com.dubylon.photochaos.rest.tasktemplate;

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
    if (request.getPathInfo() == null) {
      TaskTemplatesGetListHandler h = new TaskTemplatesGetListHandler();
      try {
        TaskTemplatesGetListData pcResponse = h.handleRequest(request);
        PCResponseWriter.writeSuccess(response, pcResponse, pcResponse.getTaskTemplates());
      } catch (PCHandlerError err) {
        PCResponseWriter.writeError(response, err);
      }
    } else {
      TaskTemplatesGetHandler h = new TaskTemplatesGetHandler();
      try {
        TaskTemplatesGetData pcResponse = h.handleRequest(request);
        PCResponseWriter.writeSuccess(response, pcResponse, pcResponse.getTaskTemplate());
      } catch (PCHandlerError err) {
        PCResponseWriter.writeError(response, err);
      }

    }
  }

}
