package com.dubylon.photochaos.rest.generic;

import com.dubylon.photochaos.rest.PCHandlerResponseData;

import java.nio.file.Path;

public class AbstractRequestedPathData extends PCHandlerResponseData {

  private Path requestedPath;

  public Path getRequestedPath() {
    return requestedPath;
  }

  public void setRequestedPath(Path requestedPath) {
    this.requestedPath = requestedPath;
  }
}
