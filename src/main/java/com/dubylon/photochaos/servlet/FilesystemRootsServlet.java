package com.dubylon.photochaos.servlet;

import com.dubylon.photochaos.handler.FilesystemRootsHandler;
import com.dubylon.photochaos.handler.PCResponseObject;
import com.dubylon.photochaos.handler.PCResponseWriter;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FilesystemRootsServlet extends AbstractPhotoChaosServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    FilesystemRootsHandler h = new FilesystemRootsHandler();
    PCResponseObject pcResponse = h.doGet(request);
    if (pcResponse.isOk()) {
      List<Object> roots = (List<Object>) pcResponse.getData("roots");
      PCResponseWriter.writeSuccess(response, pcResponse, roots);
    } else {
      PCResponseWriter.writeError(response, pcResponse);
    }
  }
}
