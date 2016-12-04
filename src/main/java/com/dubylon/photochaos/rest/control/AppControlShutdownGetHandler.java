package com.dubylon.photochaos.rest.control;

import com.dubylon.photochaos.app.PhotoChaosOrganizerApplication;
import com.dubylon.photochaos.rest.PhotoChaosHandler;
import com.dubylon.photochaos.rest.PCHandlerError;
import org.eclipse.jetty.server.Server;

import javax.servlet.http.HttpServletRequest;

public class AppControlShutdownGetHandler implements PhotoChaosHandler {

  private class JettyStopper implements Runnable {

    @Override
    public void run() {
      Server jettyServer = PhotoChaosOrganizerApplication.getJettyServer();
      try {
        Thread.sleep(1000);
        jettyServer.stop();
      } catch (InterruptedException e) {
        e.printStackTrace();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

  }

  public AppControlShutdownGetHandler() {
  }

  @Override
  public AppControlShutdownGetData handleRequest(HttpServletRequest request) throws PCHandlerError {
    AppControlShutdownGetData response = new AppControlShutdownGetData();
    JettyStopper js = new JettyStopper();
    Thread t = new Thread(js);
    t.start();
    response.getShutdownInfo().put("stopScheduled", true);
    return response;
  }
}
