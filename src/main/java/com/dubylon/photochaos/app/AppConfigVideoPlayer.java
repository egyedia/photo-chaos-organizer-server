package com.dubylon.photochaos.app;

import java.util.List;

public class AppConfigVideoPlayer {
  private String executable;
  private List<String> arguments;

  public AppConfigVideoPlayer() {
  }

  public String getExecutable() {
    return executable;
  }

  public void setExecutable(String executable) {
    this.executable = executable;
  }

  public List<String> getArguments() {
    return arguments;
  }

  public void setArguments(List<String> arguments) {
    this.arguments = arguments;
  }
}
