package com.dubylon.photochaos.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.util.ajax.JSON;

public abstract class AbstractPhotoChaosServlet extends HttpServlet {

  protected void ok(HttpServletResponse response, Object responseObject) throws IOException {
    response.setContentType("application/json");
    response.setHeader("Access-Control-Allow-Origin", "*");
    response.setStatus(HttpServletResponse.SC_OK);
    response.getWriter().println(JSON.toString(responseObject));
  }

  protected void error(HttpServletResponse response, String errorCode, String errorMessage) throws IOException {
    response.setContentType("application/json");
    response.setHeader("Access-Control-Allow-Origin", "*");
    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    Map<String, Object> rm = new HashMap<>();
    rm.put("errorCode", errorCode);
    rm.put("errorMessage", errorMessage);
    response.getWriter().println(rm);
  }

  protected void notfound(HttpServletResponse response, String errorCode, String errorMessage) throws IOException {
    response.setContentType("application/json");
    response.setHeader("Access-Control-Allow-Origin", "*");
    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    Map<String, Object> rm = new HashMap<>();
    rm.put("errorCode", errorCode);
    rm.put("errorMessage", errorMessage);
    response.getWriter().println(rm);
  }
}
