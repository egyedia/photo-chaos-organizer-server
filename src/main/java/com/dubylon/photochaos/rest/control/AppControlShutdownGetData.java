package com.dubylon.photochaos.rest.control;

import com.dubylon.photochaos.rest.PCHandlerResponseData;

import java.util.Map;
import java.util.HashMap;

public class AppControlShutdownGetData extends PCHandlerResponseData {

  private Map<String, Object> shutdownInfo;

  public AppControlShutdownGetData() {
    shutdownInfo = new HashMap<>();
  }

  public Map<String, Object> getShutdownInfo() {
    return shutdownInfo;
  }

  public void setShutdownInfo(Map<String, Object> shutdownInfo) {
    this.shutdownInfo = shutdownInfo;
  }
}
