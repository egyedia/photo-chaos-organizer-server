package com.dubylon.photochaos.resource;

import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.util.resource.Resource;

public class ClasspathResourceHandler extends ResourceHandler {

  @Override
  public Resource getResource(String path) {
    System.out.println("ClasspathResourceHandler.getResource:" + path + ":");
    if (path == null || "/".equals(path)) {
      path = "index.html";
    }
    System.out.println("path:" + path + ":");
    Resource resource = Resource.newClassPathResource(path);
    System.out.println("resource:" + resource + ":");
    return resource;
  }
}
