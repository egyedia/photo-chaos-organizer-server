package com.dubylon.photochaos.rest.fsfolder;

import com.dubylon.photochaos.handler.PCResponseWriter;
import com.dubylon.photochaos.rest.PCHandlerError;
import com.dubylon.photochaos.servlet.AbstractPhotoChaosServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FilesystemPathRenameServlet extends AbstractPhotoChaosServlet {

  @Override
  protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    FilesystemPathRenamePutHandler h = new FilesystemPathRenamePutHandler();
    try {
      FilesystemPathRenamePutData pcResponse = h.handleRequest(request);
      PCResponseWriter.writeSuccess(response, pcResponse, pcResponse);
    } catch (PCHandlerError err) {
      PCResponseWriter.writeError(response, err);
    }
  }

}
