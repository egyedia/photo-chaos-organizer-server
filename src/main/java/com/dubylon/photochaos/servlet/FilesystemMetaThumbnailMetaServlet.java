package com.dubylon.photochaos.servlet;

import com.dubylon.photochaos.handler.FilesystemMetaThumbnailMetaHandler;
import com.dubylon.photochaos.handler.PCResponseObject;
import com.dubylon.photochaos.handler.PCResponseWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FilesystemMetaThumbnailMetaServlet extends AbstractPhotoChaosServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    FilesystemMetaThumbnailMetaHandler h = new FilesystemMetaThumbnailMetaHandler();
    PCResponseObject pcResponse = h.doGet(request);
    if (pcResponse.isOk()) {
      Map<String, Object> rm = new HashMap<>();
      rm.put("height", pcResponse.getData("height"));
      rm.put("width", pcResponse.getData("width"));
      rm.put("orientation", pcResponse.getData("orientation"));
      rm.put("dateTimeOriginal", pcResponse.getData("dateTimeOriginal"));
      PCResponseWriter.writeSuccess(response, pcResponse, rm);
    } else {
      PCResponseWriter.writeError(response, pcResponse);
    }
  }
}
