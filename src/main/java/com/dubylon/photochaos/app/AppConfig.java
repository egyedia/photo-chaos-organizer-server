package com.dubylon.photochaos.app;

import java.util.HashMap;
import java.util.Map;

public class AppConfig {

  private boolean openBrowser = true;
  private int defaultPort = 2120;
  private int realPort;
  private Map<String, TaskConfig> tasks;

  public AppConfig() {
    tasks = new HashMap<>();
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

  public Map<String, TaskConfig> getTasks() {
    return tasks;
  }

  public void setTasks(Map<String, TaskConfig> tasks) {
    this.tasks = tasks;
  }

  public int getRealPort() {
    return realPort;
  }

  public void setRealPort(int realPort) {
    this.realPort = realPort;
  }
}
