package com.dubylon.photochaos.rest.thumbdata;

import com.dubylon.photochaos.handler.PCResponseWriter;
import com.dubylon.photochaos.rest.PCHandlerError;
import com.dubylon.photochaos.servlet.AbstractPhotoChaosServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FilesystemMetaThumbnailDataServlet extends AbstractPhotoChaosServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    FilesystemMetaThumbnailDataGetHandler h = new FilesystemMetaThumbnailDataGetHandler();
    try {
      FilesystemMetaThumbnailDataGetData pcResponse = h.handleRequest(request);
      String contentType = "image/jpg";
      PCResponseWriter.writeSuccess(response, contentType, pcResponse.getThumbnailData());
    } catch (PCHandlerError err) {
      PCResponseWriter.writeError(response, err);
    }
  }

}
