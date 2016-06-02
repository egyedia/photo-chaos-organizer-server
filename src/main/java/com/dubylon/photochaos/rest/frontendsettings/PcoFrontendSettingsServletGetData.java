package com.dubylon.photochaos.rest.frontendsettings;

import com.dubylon.photochaos.model.config.ConfigFrontendData;
import com.dubylon.photochaos.rest.PCHandlerResponseData;

public class PcoFrontendSettingsServletGetData extends PCHandlerResponseData {

  private ConfigFrontendData configData;

  public PcoFrontendSettingsServletGetData() {
  }

  public ConfigFrontendData getConfigData() {
    return configData;
  }

  public void setConfigData(ConfigFrontendData configData) {
    this.configData = configData;
  }
}
