package com.dubylon.photochaos.rest.fspath;

import com.dubylon.photochaos.handler.PCResponseObject;
import com.dubylon.photochaos.handler.PCResponseWriter;
import com.dubylon.photochaos.rest.PCHandlerError;
import com.dubylon.photochaos.rest.fsroot.FilesystemRootsData;
import com.dubylon.photochaos.rest.fsroot.FilesystemRootsHandler;
import com.dubylon.photochaos.servlet.AbstractPhotoChaosServlet;

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
    try {
      FilesystemPathContentsData pcResponse = h.doGet(request);
      Map<String, Object> rm = new HashMap<>();
      rm.put("contentList", pcResponse.getContentList());
      rm.put("pathInfo", pcResponse.getPathInfo());
      rm.put("parentList", pcResponse.getParentList());
      PCResponseWriter.writeSuccess(response, pcResponse, rm);
    } catch (PCHandlerError err) {
      PCResponseWriter.writeError(response, err);
    }
  }
}
