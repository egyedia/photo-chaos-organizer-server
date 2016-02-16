package com.dubylon.photochaos.handler;

import com.dubylon.photochaos.handler.iface.IPhotoChaosHandler;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FilesystemFavoritesHandler implements IPhotoChaosHandler {

  @Override
  public PCResponseObject doGet(HttpServletRequest request) {
    return PCResponseObject.methodNotAllowed();
  }

  @Override
  public PCResponseObject doPost(HttpServletRequest request) {
    String content = null;
    try {
      content = IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8);
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println("FilesystemFavoritesHandler.doPost:" + content);
    PCResponseObject response = PCResponseObject.created();
    return response;
  }

  @Override
  public PCResponseObject doPut(HttpServletRequest request) {
    return null;
  }

  @Override
  public PCResponseObject doDelete(HttpServletRequest request) {
    return PCResponseObject.methodNotAllowed();
  }

}
