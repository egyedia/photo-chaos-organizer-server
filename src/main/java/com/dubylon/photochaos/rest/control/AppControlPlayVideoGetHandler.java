package com.dubylon.photochaos.rest.control;

import com.dubylon.photochaos.app.AppConfigVideo;
import com.dubylon.photochaos.app.AppConfigVideoPlayer;
import com.dubylon.photochaos.app.PhotoChaosOrganizerApplication;
import com.dubylon.photochaos.rest.PCHandlerError;
import com.dubylon.photochaos.rest.generic.AbstractPCHandlerPath;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class AppControlPlayVideoGetHandler extends AbstractPCHandlerPath {


  public AppControlPlayVideoGetHandler() {
  }

  @Override
  public AppControlPlayVideoGetData handleRequest(HttpServletRequest request) throws PCHandlerError {
    AppControlPlayVideoGetData response = new AppControlPlayVideoGetData();

    handlePath(request, response);
    Path requestedPath = response.getRequestedPath();

    //TODO do some error checking and reporting here

    AppConfigVideo videoConfig = PhotoChaosOrganizerApplication.getAppConfig().getVideo();
    String defaultPlayerName = videoConfig.getDefaultPlayer();
    AppConfigVideoPlayer defaultPlayer = videoConfig.getPlayers().get(defaultPlayerName);

    List<String> cmd = new ArrayList<>();
    cmd.add(defaultPlayer.getExecutable());
    defaultPlayer.getArguments().forEach(s -> cmd.add(s.replace("${videoFile}", requestedPath.toString())));

    //System.out.println(cmd);
    boolean started = false;
    int exitValue = 0;
    try {
      Process p = Runtime.getRuntime().exec(cmd.toArray(new String[0]));
    } catch (IOException e) {
      e.printStackTrace();
    }

    response.getPlayVideoInfo().put("playbackStarted", started);
    response.getPlayVideoInfo().put("command", cmd);
    response.getPlayVideoInfo().put("exitValue", exitValue);
    return response;
  }
}
