package com.dubylon.photochaos.app;

public class AppConfig {

  private boolean openBrowser = true;
  private int defaultPort = 2120;
  private int realPort;

  public AppConfig() {
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

  public int getRealPort() {
    return realPort;
  }

  public void setRealPort(int realPort) {
    this.realPort = realPort;
  }
}
