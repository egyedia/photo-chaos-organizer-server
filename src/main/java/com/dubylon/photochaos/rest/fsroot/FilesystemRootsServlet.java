package com.dubylon.photochaos.rest.fsroot;

import com.dubylon.photochaos.handler.PCResponseObject;
import com.dubylon.photochaos.handler.PCResponseWriter;
import com.dubylon.photochaos.rest.PCHandlerError;
import com.dubylon.photochaos.rest.PCHandlerResponseData;
import com.dubylon.photochaos.servlet.AbstractPhotoChaosServlet;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FilesystemRootsServlet extends AbstractPhotoChaosServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    FilesystemRootsHandler h = new FilesystemRootsHandler();
    try {
      FilesystemRootsData pcResponse = h.doGet(request);
      PCResponseWriter.writeSuccess(response, pcResponse, pcResponse.getRoots());
    } catch (PCHandlerError err) {
      PCResponseWriter.writeError(response, err);
    }
  }
}
