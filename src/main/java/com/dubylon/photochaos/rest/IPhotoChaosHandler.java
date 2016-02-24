package com.dubylon.photochaos.rest;

import javax.servlet.http.HttpServletRequest;

public interface IPhotoChaosHandler {

  PCHandlerResponse handleRequest(HttpServletRequest request) throws PCHandlerError;
  
}
