package com.dubylon.photochaos.handler;

import com.dubylon.photochaos.handler.iface.IPhotoChaosHandler;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;

public abstract class AsbtractPCHandler implements IPhotoChaosHandler {

  protected static PCResponseObject handlePath(HttpServletRequest request) {
    PCResponseObject response = PCResponseObject.ok();

    // Get path from request and normalize
    String path = request.getPathInfo();
    if (path != null && path.indexOf("/") == 0) {
      path = path.substring(1);
    }
    if (path == null || path.length() == 0) {
      return PCResponseObject.error("MISSING_PATH", "Path should be passed as Base64 encoded string.");
    }

    // Decode
    String decodedPath = StringUtils.newStringUtf8(Base64.decodeBase64(path));

    // Check path existence
    Path requestedPath = null;
    try {
      requestedPath = Paths.get(decodedPath);
    } catch (InvalidPathException ex) {
      return PCResponseObject.error("INVALID_PATH", ex);
    }
    if (requestedPath == null || requestedPath.toString().length() == 0) {
      return PCResponseObject.error("ERRONOUS_PATH", "Invalid path requested:" + path);
    }
    response.setData("requestedPath", requestedPath);
    return response;
  }

}
