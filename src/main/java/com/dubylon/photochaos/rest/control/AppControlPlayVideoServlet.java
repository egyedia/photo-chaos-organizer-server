package com.dubylon.photochaos.rest.control;

import com.dubylon.photochaos.handler.PCResponseWriter;
import com.dubylon.photochaos.rest.PCHandlerError;
import com.dubylon.photochaos.servlet.AbstractPhotoChaosServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AppControlPlayVideoServlet extends AbstractPhotoChaosServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    AppControlPlayVideoGetHandler h = new AppControlPlayVideoGetHandler();
    try {
      AppControlPlayVideoGetData pcResponse = h.handleRequest(request);
      PCResponseWriter.writeSuccess(response, pcResponse, pcResponse.getPlayVideoInfo());
    } catch (PCHandlerError err) {
      PCResponseWriter.writeError(response, err);
    }
  }

}
