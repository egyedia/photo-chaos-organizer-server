package com.dubylon.photochaos.rest.config;

import com.dubylon.photochaos.app.PhotoChaosOrganizerApplication;
import com.dubylon.photochaos.model.config.ConfigData;
import com.dubylon.photochaos.rest.IPhotoChaosHandler;
import com.dubylon.photochaos.rest.PCHandlerError;

import javax.servlet.http.HttpServletRequest;

public class ConfigGetHandler implements IPhotoChaosHandler {

  public ConfigGetHandler() {
  }

  @Override
  public ConfigGetData handleRequest(HttpServletRequest request) throws PCHandlerError {
    ConfigGetData response = new ConfigGetData();
    ConfigData cd = new ConfigData();
    cd.setOsUsername(System.getProperty("user.name"));
    cd.setRestPort(PhotoChaosOrganizerApplication.getAppConfig().getRealPort());
    response.setConfigData(cd);
    return response;
  }
}
