package com.dubylon.photochaos.handler;

import java.nio.file.NoSuchFileException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletResponse;

public final class PCResponseObject {

  private final int responseCode;
  private String errorCode;
  private String errorDescription;
  private Exception exception;
  private Map<String, Object> dataMap;

  private PCResponseObject(int responseCode) {
    this.responseCode = responseCode;
    dataMap = new HashMap<>();
  }

  public PCResponseObject(PCResponseObject src) {
    this(src.getResponseCode());
    Set<String> keys = src.dataMap.keySet();
    for (String key : keys) {
      setData(key, src.getData(key));
    }
  }

  public static PCResponseObject ok() {
    PCResponseObject r = new PCResponseObject(HttpServletResponse.SC_OK);
    return r;
  }

  public static PCResponseObject created(String location, Object obj) {
    PCResponseObject r = new PCResponseObject(HttpServletResponse.SC_CREATED);
    r.setData("location", location);
    r.setData("createdObject", obj);
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

  public PCResponseObject setData(String key, Object value) {
    dataMap.put(key, value);
    return this;
  }

  public Object getData(String key) {
    return dataMap.get(key);
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
