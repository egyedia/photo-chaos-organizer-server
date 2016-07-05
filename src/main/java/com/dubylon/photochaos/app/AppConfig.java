package com.dubylon.photochaos.app;

public class AppConfig {

  private AppConfigMain main;
  private AppConfigVideo video;
  private AppConfigDateFormats dateFormats;
  private AppConfigFileTypes fileTypes;
  private int realPort;

  public AppConfig() {
  }

  public AppConfigMain getMain() {
    return main;
  }

  public void setMain(AppConfigMain main) {
    this.main = main;
  }

  public AppConfigVideo getVideo() {
    return video;
  }

  public void setVideo(AppConfigVideo video) {
    this.video = video;
  }

  public int getRealPort() {
    return realPort;
  }

  public void setRealPort(int realPort) {
    this.realPort = realPort;
  }

  public AppConfigDateFormats getDateFormats() {
    return dateFormats;
  }

  public void setDateFormats(AppConfigDateFormats dateFormats) {
    this.dateFormats = dateFormats;
  }

  public AppConfigFileTypes getFileTypes() {
    return fileTypes;
  }

  public void setFileTypes(AppConfigFileTypes fileTypes) {
    this.fileTypes = fileTypes;
  }
}
