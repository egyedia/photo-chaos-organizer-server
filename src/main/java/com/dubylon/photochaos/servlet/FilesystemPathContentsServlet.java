package com.dubylon.photochaos.servlet;

import com.dubylon.photochaos.handler.FilesystemPathContentsHandler;
import com.dubylon.photochaos.handler.PCResponseObject;
import com.dubylon.photochaos.handler.PCResponseWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FilesystemPathContentsServlet extends AbstractPhotoChaosServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    FilesystemPathContentsHandler h = new FilesystemPathContentsHandler();
    PCResponseObject pcResponse = h.doGet(request);
    if (pcResponse.isSuccess()) {
      Map<String, Object> rm = new HashMap<>();
      rm.put("contentList", pcResponse.getData("contentList"));
      rm.put("pathInfo", pcResponse.getData("pathInfo"));
      rm.put("parentInfo", pcResponse.getData("parentInfo"));
      PCResponseWriter.writeSuccess(response, pcResponse, rm);
    } else {
      PCResponseWriter.writeError(response, pcResponse);
    }
  }
}
