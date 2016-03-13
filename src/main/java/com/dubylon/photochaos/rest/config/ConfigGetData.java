package com.dubylon.photochaos.rest.config;

import com.dubylon.photochaos.model.config.ConfigData;
import com.dubylon.photochaos.rest.PCHandlerResponseData;

public class ConfigGetData extends PCHandlerResponseData {

  private ConfigData configData;

  public ConfigGetData() {
  }

  public ConfigData getConfigData() {
    return configData;
  }

  public void setConfigData(ConfigData configData) {
    this.configData = configData;
  }
}
