package com.dubylon.photochaos.rest.range;

import com.dubylon.photochaos.handler.PCResponseWriter;
import com.dubylon.photochaos.rest.PCHandlerError;
import com.dubylon.photochaos.servlet.AbstractPhotoChaosServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FilesystemRangeServlet extends AbstractPhotoChaosServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    FilesystemRangeGetHandler h = new FilesystemRangeGetHandler();
    try {
      h.setHttpServletResponse(response);
      h.handleRequest(request);
    } catch (PCHandlerError err) {
      PCResponseWriter.writeError(response, err);
    }
  }

}
