package com.dubylon.photochaos.rest.clientsettings;

import com.dubylon.photochaos.model.config.ConfigData;
import com.dubylon.photochaos.rest.PCHandlerResponseData;

public class PcoClientSettingsServletGetData extends PCHandlerResponseData {

  private ConfigData configData;

  public PcoClientSettingsServletGetData() {
  }

  public ConfigData getConfigData() {
    return configData;
  }

  public void setConfigData(ConfigData configData) {
    this.configData = configData;
  }
}
