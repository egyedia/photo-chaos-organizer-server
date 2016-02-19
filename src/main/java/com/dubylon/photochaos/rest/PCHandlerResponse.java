package com.dubylon.photochaos.rest;

public class PCHandlerResponse {

  public final static int OK = 200;
  public final static int ERROR = 500;
  public final static int NOT_FOUND = 404;

  private int responseCode = OK;

  public void setResponseCode(int responseCode) {
    this.responseCode = responseCode;
  }

  public int getResponseCode() {
    return responseCode;
  }
}
