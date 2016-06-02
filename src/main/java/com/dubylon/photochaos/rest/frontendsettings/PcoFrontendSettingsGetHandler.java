package com.dubylon.photochaos.rest.frontendsettings;

import com.dubylon.photochaos.app.PhotoChaosOrganizerApplication;
import com.dubylon.photochaos.model.config.ConfigFrontendData;
import com.dubylon.photochaos.rest.IPhotoChaosHandler;
import com.dubylon.photochaos.rest.PCHandlerError;

import javax.servlet.http.HttpServletRequest;

public class PcoFrontendSettingsGetHandler implements IPhotoChaosHandler {

  public PcoFrontendSettingsGetHandler() {
  }

  @Override
  public PcoFrontendSettingsServletGetData handleRequest(HttpServletRequest request) throws PCHandlerError {
    PcoFrontendSettingsServletGetData response = new PcoFrontendSettingsServletGetData();
    ConfigFrontendData cd = new ConfigFrontendData();
    cd.setDateFormats(PhotoChaosOrganizerApplication.getAppConfig().getDateFormats());
    response.setConfigData(cd);
    return response;
  }
}
