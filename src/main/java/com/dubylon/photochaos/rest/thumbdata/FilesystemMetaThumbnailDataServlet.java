package com.dubylon.photochaos.rest.thumbdata;

import com.dubylon.photochaos.handler.PCResponseWriter;
import com.dubylon.photochaos.rest.PCHandlerError;
import com.dubylon.photochaos.rest.thumbmeta.FilesystemMetaThumbnailMetaGetData;
import com.dubylon.photochaos.servlet.AbstractPhotoChaosServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FilesystemMetaThumbnailDataServlet extends AbstractPhotoChaosServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    FilesystemMetaThumbnailDataGetHandler h = new FilesystemMetaThumbnailDataGetHandler();
    try {
      FilesystemMetaThumbnailMetaGetData pcResponse = h.handleRequest(request);
      Map<String, String> headers = new HashMap<>();
      headers.put("PCO-height", String.valueOf(pcResponse.getExtractedMeta().getHeight()));
      headers.put("PCO-width", String.valueOf(pcResponse.getExtractedMeta().getWidth()));
      headers.put("PCO-orientation", String.valueOf(pcResponse.getExtractedMeta().getOrientation()));
      headers.put("PCO-dateTimeOriginal", String.valueOf(pcResponse.getExtractedMeta().getDateTimeOriginal()));
      String contentType = "image/jpg";
      PCResponseWriter.writeSuccess(response, contentType, headers, pcResponse.getThumbnailData());
    } catch (PCHandlerError err) {
      PCResponseWriter.writeError(response, err);
    }
  }

}
