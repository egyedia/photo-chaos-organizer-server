package com.dubylon.photochaos.servlet;

import com.dubylon.photochaos.resource.ClasspathResourceHandler;
import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.util.resource.Resource;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RemainderServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    Resource resource = Resource.newClassPathResource("index.html");
    response.setContentType("text/html");
    response.setStatus(HttpServletResponse.SC_OK);
    IOUtils.copy(resource.getInputStream(), response.getOutputStream());
  }
}
