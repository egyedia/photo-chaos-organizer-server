package com.dubylon.photochaos.rest.user;

import com.dubylon.photochaos.handler.PCResponseWriter;
import com.dubylon.photochaos.rest.PCHandlerError;
import com.dubylon.photochaos.rest.favorite.*;
import com.dubylon.photochaos.servlet.AbstractPhotoChaosServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UsersServlet extends AbstractPhotoChaosServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    UsersGetHandler h = new UsersGetHandler();
    try {
      UsersGetData pcResponse = h.handleRequest(request);
      PCResponseWriter.writeSuccess(response, pcResponse, pcResponse.getUsers());
    } catch (PCHandlerError err) {
      PCResponseWriter.writeError(response, err);
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    UsersPostHandler h = new UsersPostHandler();
    try {
      UsersPostData pcResponse = h.handleRequest(request);
      Map<String, String> headers = new HashMap<>();
      String newLocation = request.getRequestURL().append("/").append(pcResponse.getId()).toString();
      headers.put("Location", newLocation);
      PCResponseWriter.writeSuccess(response, pcResponse, headers, pcResponse.getCreatedObject());
    } catch (PCHandlerError err) {
      PCResponseWriter.writeError(response, err);
    }
  }

}
