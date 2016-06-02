package com.dubylon.photochaos.rest.clientsettings;

import com.dubylon.photochaos.model.config.ConfigClientData;
import com.dubylon.photochaos.rest.PCHandlerResponseData;

public class PcoClientSettingsServletGetData extends PCHandlerResponseData {

  private ConfigClientData configData;

  public PcoClientSettingsServletGetData() {
  }

  public ConfigClientData getConfigData() {
    return configData;
  }

  public void setConfigData(ConfigClientData configData) {
    this.configData = configData;
  }
}
