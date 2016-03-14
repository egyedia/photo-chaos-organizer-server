package com.dubylon.photochaos.rest.generic;

import com.dubylon.photochaos.rest.PCHandlerError;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class AbstractPCHandlerPath extends AbstractPCHandler {

  protected final static String PATH_INFO_PREFIX = "/" + PATH_PREFIX;

  protected static void handlePath(HttpServletRequest request, AbstractRequestedPathData response) throws
      PCHandlerError {
    // Get path from request and normalize
    String path = request.getPathInfo();
    if (path != null && path.indexOf(PATH_INFO_PREFIX) == 0) {
      path = path.substring(PATH_INFO_PREFIX.length());
    }
    if (path == null || path.length() == 0) {
      throw new PCHandlerError("MISSING_PATH", "Path should be passed in the form of file://...");
    }

    // Check path existence
    Path requestedPath = null;
    try {
      requestedPath = Paths.get(path);
    } catch (InvalidPathException ex) {
      throw new PCHandlerError("INVALID_PATH", ex);
    }
    if (requestedPath == null || requestedPath.toString().length() == 0) {
      throw new PCHandlerError("ERRONEOUS_PATH", "Invalid path requested:" + path);
    }
    response.setRequestedPath(requestedPath);
  }

}
