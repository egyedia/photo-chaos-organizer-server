package com.dubylon.photochaos.rest.control;

import com.dubylon.photochaos.rest.PCHandlerResponseData;
import com.dubylon.photochaos.rest.generic.AbstractRequestedPathData;

import java.util.HashMap;
import java.util.Map;

public class AppControlPlayVideoGetData extends AbstractRequestedPathData {

  private Map<String, Object> playVideoInfo;

  public AppControlPlayVideoGetData() {
    playVideoInfo = new HashMap<>();
  }

  public Map<String, Object> getPlayVideoInfo() {
    return playVideoInfo;
  }

  public void setPlayVideoInfo(Map<String, Object> playVideoInfo) {
    this.playVideoInfo = playVideoInfo;
  }
}
