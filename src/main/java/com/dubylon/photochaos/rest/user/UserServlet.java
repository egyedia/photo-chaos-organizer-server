package com.dubylon.photochaos.rest.user;

import com.dubylon.photochaos.handler.PCResponseWriter;
import com.dubylon.photochaos.rest.PCHandlerError;
import com.dubylon.photochaos.servlet.AbstractPhotoChaosServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserServlet extends AbstractPhotoChaosServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    UserGetHandler h = new UserGetHandler();
    try {
      UserGetData pcResponse = h.handleRequest(request);
      PCResponseWriter.writeSuccess(response, pcResponse, pcResponse.getUser());
    } catch (PCHandlerError err) {
      PCResponseWriter.writeError(response, err);
    }
  }

}
