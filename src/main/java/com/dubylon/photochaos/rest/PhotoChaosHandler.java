package com.dubylon.photochaos.rest;

import javax.servlet.http.HttpServletRequest;

public interface PhotoChaosHandler {

  PCHandlerResponse handleRequest(HttpServletRequest request) throws PCHandlerError;
  
}
