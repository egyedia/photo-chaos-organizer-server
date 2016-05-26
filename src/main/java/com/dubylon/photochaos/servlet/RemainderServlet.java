package com.dubylon.photochaos.servlet;

import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.util.resource.Resource;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RemainderServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    Resource resource = Resource.newClassPathResource("index.html");
    response.setContentType("text/html");
    response.setStatus(HttpServletResponse.SC_OK);
    //System.out.println(resource);
    //System.out.println(resource.getURI());
    if (resource != null) {
      IOUtils.copy(resource.getInputStream(), response.getOutputStream());
    } else {
      response.getWriter().write("Photo Chaos Organizer Server");
    }
  }
}
