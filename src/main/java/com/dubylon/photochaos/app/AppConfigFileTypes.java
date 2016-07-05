package com.dubylon.photochaos.app;

import java.util.Map;

public class AppConfigFileTypes {

  private Map<String, AppConfigFileType> types;

  public AppConfigFileTypes() {
  }

  public Map<String, AppConfigFileType> getTypes() {
    return types;
  }

  public void setTypes(Map<String, AppConfigFileType> types) {
    this.types = types;
  }
}
