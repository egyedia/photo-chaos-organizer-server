package com.dubylon.photochaos.rest.stream;

import com.dubylon.photochaos.handler.PCResponseWriter;
import com.dubylon.photochaos.rest.PCHandlerError;
import com.dubylon.photochaos.servlet.AbstractPhotoChaosServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FilesystemStreamServlet extends AbstractPhotoChaosServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    FilesystemStreamGetHandler h = new FilesystemStreamGetHandler();
    try {
      h.setHttpServletResponse(response);
      h.handleRequest(request);
    } catch (PCHandlerError err) {
      PCResponseWriter.writeError(response, err);
    }
  }

}
