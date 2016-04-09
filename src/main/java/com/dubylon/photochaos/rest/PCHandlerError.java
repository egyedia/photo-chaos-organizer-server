package com.dubylon.photochaos.rest;

public class PCHandlerError extends Exception {

  private String errorKey;
  private String errorText;
  private int responseCode;

  public PCHandlerError(int responseCode, String errorKey, String errorText, Exception ex) {
    super(errorText, ex);
    this.responseCode = responseCode;
    this.errorKey = errorKey;
    this.errorText = errorText;
  }

  public PCHandlerError(String errorKey, String errorText) {
    this(PCHandlerResponse.ERROR, errorKey, errorText, null);
  }

  public PCHandlerError(String errorKey, Exception ex) {
    this(PCHandlerResponse.ERROR, errorKey, ex.toString(), ex);
    ex.printStackTrace();
  }

  public PCHandlerError(int responseCode, String errorKey, Exception ex) {
    this(responseCode, errorKey, ex.toString(), ex);
    ex.printStackTrace();
  }

  public PCHandlerError(int responseCode, String errorKey) {
    this(responseCode, errorKey, null, null);
  }

  public PCHandlerError(String errorKey) {
    this(PCHandlerResponse.ERROR, errorKey, null, null);
  }

  public String getErrorKey() {
    return errorKey;
  }

  public String getErrorText() {
    return errorText;
  }

  public int getResponseCode() {
    return responseCode;
  }
}
