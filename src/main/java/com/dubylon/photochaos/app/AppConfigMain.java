package com.dubylon.photochaos.app;

public class AppConfigMain {

  private boolean openBrowser = true;
  private int defaultPort = 2120;

  public AppConfigMain() {
  }

  public boolean isOpenBrowser() {
    return openBrowser;
  }

  public void setOpenBrowser(boolean openBrowser) {
    this.openBrowser = openBrowser;
  }

  public int getDefaultPort() {
    return defaultPort;
  }

  public void setDefaultPort(int defaultPort) {
    this.defaultPort = defaultPort;
  }
}
