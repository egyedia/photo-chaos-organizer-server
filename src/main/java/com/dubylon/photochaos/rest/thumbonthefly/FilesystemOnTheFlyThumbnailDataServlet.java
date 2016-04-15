package com.dubylon.photochaos.rest.thumbonthefly;

import com.dubylon.photochaos.handler.PCResponseWriter;
import com.dubylon.photochaos.rest.PCHandlerError;
import com.dubylon.photochaos.servlet.AbstractPhotoChaosServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FilesystemOnTheFlyThumbnailDataServlet extends AbstractPhotoChaosServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    FilesystemOnTheFlyThumbnailDataGetHandler h = new FilesystemOnTheFlyThumbnailDataGetHandler();
    try {
      FilesystemOnTheFlyThumbnailDataGetData pcResponse = h.handleRequest(request);
      PCResponseWriter.writeSuccess(response, pcResponse.getThumbnail());
    } catch (PCHandlerError err) {
      PCResponseWriter.writeError(response, err);
    }
  }

}
