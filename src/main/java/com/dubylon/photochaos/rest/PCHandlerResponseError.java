package com.dubylon.photochaos.rest;

public class PCHandlerResponseError extends PCHandlerResponse {

  public static PCHandlerResponseError methodNotAllowed() {
    return new PCHandlerResponseMethodNotAllowed();
  }

}
