package com.dubylon.photochaos.rest.generic;

import com.dubylon.photochaos.rest.PCHandlerError;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class AbstractPCHandlerPath extends AbstractPCHandler {

  protected static void handlePath(HttpServletRequest request, AbstractRequestedPathData response) throws PCHandlerError {
    // Get path from request and normalize
    String path = request.getPathInfo();
    if (path != null && path.indexOf("/") == 0) {
      path = path.substring(1);
    }
    if (path == null || path.length() == 0) {
      throw new PCHandlerError("MISSING_PATH", "Path should be passed as Base64 encoded string.");
    }

    // Decode
    String decodedPath = StringUtils.newStringUtf8(Base64.decodeBase64(path));

    // Check path existence
    Path requestedPath = null;
    try {
      requestedPath = Paths.get(decodedPath);
    } catch (InvalidPathException ex) {
      throw new PCHandlerError("INVALID_PATH", ex);
    }
    if (requestedPath == null || requestedPath.toString().length() == 0) {
      throw new PCHandlerError("ERRONOUS_PATH", "Invalid path requested:" + path);
    }
    response.setRequestedPath(requestedPath);
  }

}
