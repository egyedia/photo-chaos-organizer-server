package com.dubylon.photochaos.rest.thumbmeta;

import com.dubylon.photochaos.handler.PCResponseWriter;
import com.dubylon.photochaos.rest.PCHandlerError;
import com.dubylon.photochaos.servlet.AbstractPhotoChaosServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FilesystemMetaThumbnailMetaServlet extends AbstractPhotoChaosServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    FilesystemMetaThumbnailMetaHandler h = new FilesystemMetaThumbnailMetaHandler();
    try {
      FilesystemMetaThumbnailMetaData pcResponse = h.doGet(request);
      PCResponseWriter.writeSuccess(response, pcResponse, pcResponse.getExtractedMeta());
    } catch (PCHandlerError err) {
      PCResponseWriter.writeError(response, err);
    }
  }
}
