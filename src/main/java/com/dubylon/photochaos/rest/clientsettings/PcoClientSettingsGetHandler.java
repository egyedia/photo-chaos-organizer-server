package com.dubylon.photochaos.rest.clientsettings;

import com.dubylon.photochaos.app.PhotoChaosOrganizerApplication;
import com.dubylon.photochaos.model.config.ConfigClientData;
import com.dubylon.photochaos.rest.IPhotoChaosHandler;
import com.dubylon.photochaos.rest.PCHandlerError;

import javax.servlet.http.HttpServletRequest;

public class PcoClientSettingsGetHandler implements IPhotoChaosHandler {

  public PcoClientSettingsGetHandler() {
  }

  @Override
  public PcoClientSettingsServletGetData handleRequest(HttpServletRequest request) throws PCHandlerError {
    PcoClientSettingsServletGetData response = new PcoClientSettingsServletGetData();
    ConfigClientData cd = new ConfigClientData();
    cd.setUserName(System.getProperty("user.name"));
    cd.setPort(PhotoChaosOrganizerApplication.getAppConfig().getRealPort());
    response.setConfigData(cd);
    return response;
  }
}