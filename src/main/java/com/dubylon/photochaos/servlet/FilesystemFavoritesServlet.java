package com.dubylon.photochaos.servlet;

import com.dubylon.photochaos.handler.FilesystemFavoritesHandler;
import com.dubylon.photochaos.handler.PCResponseObject;
import com.dubylon.photochaos.handler.PCResponseWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FilesystemFavoritesServlet extends AbstractPhotoChaosServlet {

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    FilesystemFavoritesHandler h = new FilesystemFavoritesHandler();
    PCResponseObject pcResponse = h.doPost(request);
    if (pcResponse.isSuccess()) {
      PCResponseWriter.writeSuccess(response, pcResponse);
    } else {
      PCResponseWriter.writeError(response, pcResponse);
    }
  }
}
