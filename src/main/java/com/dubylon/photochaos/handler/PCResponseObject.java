package com.dubylon.photochaos.handler;

import javax.servlet.http.HttpServletResponse;
import java.nio.file.NoSuchFileException;

public final class PCResponseObject {

  private final int responseCode;
  private String errorCode;
  private String errorDescription;
  private Exception exception;

  private PCResponseObject(int responseCode) {
    this.responseCode = responseCode;
    //dataMap = new HashMap<>();
  }

  /*public PCResponseObject(PCResponseObject src) {
    this(src.getResponseCode());
    // TODO handle plain response
    Set<String> keys = src.dataMap.keySet();
    for (String key : keys) {
      setData(key, src.getData(key));
    }
  }*/

  public static PCResponseObject ok() {
    PCResponseObject r = new PCResponseObject(HttpServletResponse.SC_OK);
    return r;
  }

  public static PCResponseObject created(String location, Object obj) {
    PCResponseObject r = new PCResponseObject(HttpServletResponse.SC_CREATED);
    //TODO created handle location and object
    //r.setData("location", location);
    //r.setData("createdObject", obj);
    return r;
  }

  public void setErrorCode(String errorCode) {
    this.errorCode = errorCode;
  }

  public void setErrorDescription(String errorDescription) {
    this.errorDescription = errorDescription;
  }

  public void setException(Exception exception) {
    this.exception = exception;
  }

  public static PCResponseObject error(String errorCode, String errorDescription) {
    PCResponseObject r = new PCResponseObject(HttpServletResponse.SC_BAD_REQUEST);
    r.setErrorCode(errorCode);
    r.setErrorDescription(errorDescription);
    return r;
  }

  public static PCResponseObject error(String errorCode, Exception ex) {
    PCResponseObject r = new PCResponseObject(HttpServletResponse.SC_BAD_REQUEST);
    r.setErrorCode(errorCode);
    r.setException(ex);
    return r;
  }

  public static PCResponseObject error(String errorCode) {
    PCResponseObject r = new PCResponseObject(HttpServletResponse.SC_BAD_REQUEST);
    r.setErrorCode(errorCode);
    return r;
  }

  public static PCResponseObject methodNotAllowed() {
    PCResponseObject r = new PCResponseObject(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    return r;
  }

  public static PCResponseObject notFound(String errorCode) {
    PCResponseObject r = new PCResponseObject(HttpServletResponse.SC_NOT_FOUND);
    r.setErrorCode(errorCode);
    return r;
  }

  public static PCResponseObject notFound(String errorCode, NoSuchFileException ex) {
    PCResponseObject r = new PCResponseObject(HttpServletResponse.SC_NOT_FOUND);
    r.setErrorCode(errorCode);
    r.setException(ex);
    return r;
  }

  public boolean isSuccess() {
    return responseCode == HttpServletResponse.SC_OK
        || responseCode == HttpServletResponse.SC_CREATED;
  }

  public String getErrorCode() {
    return errorCode;
  }

  public String getErrorDescription() {
    return errorDescription;
  }

  public Exception getException() {
    return exception;
  }

  public int getResponseCode() {
    return responseCode;
  }
}
