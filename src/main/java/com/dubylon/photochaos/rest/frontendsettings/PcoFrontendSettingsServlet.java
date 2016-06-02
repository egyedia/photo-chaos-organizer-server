package com.dubylon.photochaos.rest.frontendsettings;

import com.dubylon.photochaos.handler.PCResponseWriter;
import com.dubylon.photochaos.rest.PCHandlerError;
import com.dubylon.photochaos.rest.clientsettings.PcoClientSettingsGetHandler;
import com.dubylon.photochaos.rest.clientsettings.PcoClientSettingsServletGetData;
import com.dubylon.photochaos.servlet.AbstractPhotoChaosServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PcoFrontendSettingsServlet extends AbstractPhotoChaosServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    PcoFrontendSettingsGetHandler h = new PcoFrontendSettingsGetHandler();
    try {
      PcoFrontendSettingsServletGetData pcResponse = h.handleRequest(request);
      PCResponseWriter.writeSuccess(response, pcResponse, pcResponse.getConfigData());
    } catch (PCHandlerError err) {
      PCResponseWriter.writeError(response, err);
    }
  }

}
