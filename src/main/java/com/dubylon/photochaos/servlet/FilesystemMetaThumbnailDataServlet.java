package com.dubylon.photochaos.servlet;

import com.dubylon.photochaos.handler.FilesystemMetaThumbnailDataHandler;
import com.dubylon.photochaos.handler.PCResponseObject;
import com.dubylon.photochaos.handler.PCResponseWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FilesystemMetaThumbnailDataServlet extends AbstractPhotoChaosServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    FilesystemMetaThumbnailDataHandler h = new FilesystemMetaThumbnailDataHandler();
    PCResponseObject pcResponse = h.doGet(request);
    if (pcResponse.isOk()) {
      Map<String, String> headers = new HashMap<>();
      headers.put("PCO-height", (String) pcResponse.getData("height"));
      headers.put("PCO-width", (String) pcResponse.getData("width"));
      headers.put("PCO-orientation", (String) pcResponse.getData("orientation"));
      headers.put("PCO-dateTimeOriginal", (String) pcResponse.getData("dateTimeOriginal"));
      byte[] thumbnailData = (byte[]) pcResponse.getData("thumbnailData");
      PCResponseWriter.writeSuccess(response, "image/jpg", headers, thumbnailData);
    } else {
      PCResponseWriter.writeError(response, pcResponse);
    }
  }
}
