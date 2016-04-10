package com.dubylon.photochaos.app;

import java.util.Map;

public class AppConfigVideo {

  private Map<String, AppConfigVideoPlayer> players;
  private String defaultPlayer;

  public AppConfigVideo() {
  }

  public Map<String, AppConfigVideoPlayer> getPlayers() {
    return players;
  }

  public void setPlayers(Map<String, AppConfigVideoPlayer> players) {
    this.players = players;
  }

  public String getDefaultPlayer() {
    return defaultPlayer;
  }

  public void setDefaultPlayer(String defaultPlayer) {
    this.defaultPlayer = defaultPlayer;
  }
}
