package com.dubylon.photochaos.rest.fsraw;

import com.dubylon.photochaos.handler.PCResponseWriter;
import com.dubylon.photochaos.rest.PCHandlerError;
import com.dubylon.photochaos.servlet.AbstractPhotoChaosServlet;
import org.apache.commons.io.FileUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FilesystemRawServlet extends AbstractPhotoChaosServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    FilesystemRawGetHandler h = new FilesystemRawGetHandler();
    try {
      FilesystemRawGetData pcResponse = h.handleRequest(request);

      String contentType = "image/jpg";
      PCResponseWriter.writeSuccess(response, contentType, FileUtils.openInputStream(pcResponse.getFile()));
    } catch (PCHandlerError err) {
      PCResponseWriter.writeError(response, err);
    }
  }

}
