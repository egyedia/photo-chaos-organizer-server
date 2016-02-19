package com.dubylon.photochaos.rest;

import javax.servlet.http.HttpServletRequest;

public interface IPhotoChaosHandler {

  PCHandlerResponse doGet(HttpServletRequest request) throws PCHandlerError;

  PCHandlerResponse doPost(HttpServletRequest request) throws PCHandlerError;

  PCHandlerResponse doPut(HttpServletRequest request) throws PCHandlerError;

  PCHandlerResponse doDelete(HttpServletRequest request) throws PCHandlerError;

}
