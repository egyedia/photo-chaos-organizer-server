package com.dubylon.photochaos.rest.commandclonetask;

import com.dubylon.photochaos.handler.PCResponseWriter;
import com.dubylon.photochaos.rest.PCHandlerError;
import com.dubylon.photochaos.rest.PCHandlerResponse;
import com.dubylon.photochaos.servlet.AbstractPhotoChaosServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CommandCloneTaskServlet extends AbstractPhotoChaosServlet {

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
      IOException {
    CommandCloneTaskHandler h = new CommandCloneTaskHandler();
    try {
      CommandCloneTaskData pcResponse = h.handleRequest(request);
      if (pcResponse.isCloned()) {
        pcResponse.setResponseCode(PCHandlerResponse.NO_CONTENT);
        PCResponseWriter.writeSuccess(response, pcResponse);
      } else {
        pcResponse.setResponseCode(PCHandlerResponse.ERROR);
        PCResponseWriter.writeSuccess(response, pcResponse, pcResponse);
      }
    } catch (PCHandlerError err) {
      PCResponseWriter.writeError(response, err);
    }
  }
}
