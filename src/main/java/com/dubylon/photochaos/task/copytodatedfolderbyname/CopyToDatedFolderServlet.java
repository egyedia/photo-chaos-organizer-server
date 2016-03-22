package com.dubylon.photochaos.task.copytodatedfolderbyname;

import com.dubylon.photochaos.handler.PCResponseWriter;
import com.dubylon.photochaos.rest.PCHandlerError;
import com.dubylon.photochaos.servlet.AbstractPhotoChaosServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CopyToDatedFolderServlet extends AbstractPhotoChaosServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    CopyToDatedFolderGetHandler h = new CopyToDatedFolderGetHandler();
    try {
      CopyToDatedFolderGetData pcResponse = h.handleRequest(request);
      PCResponseWriter.writeSuccess(response, pcResponse, pcResponse);
    } catch (PCHandlerError err) {
      PCResponseWriter.writeError(response, err);
    }
  }
}
