package com.dubylon.photochaos.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AbstractPhotoChaosServlet extends HttpServlet {

  @Override
  protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException,
      IOException {
    super.doOptions(request, response);
    response.setHeader("Access-Control-Allow-Origin", "*");
    response.setHeader("Allow", "*");
    response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, OPTIONS");
    response.setHeader("Access-Control-Allow-Headers", "Origin, Content-Type, Accept, Referer, Authorization");
  }
}

