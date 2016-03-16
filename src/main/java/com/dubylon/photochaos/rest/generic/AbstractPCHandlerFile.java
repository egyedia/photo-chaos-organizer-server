package com.dubylon.photochaos.rest.generic;

import com.dubylon.photochaos.rest.PCHandlerError;
import com.dubylon.photochaos.rest.PCHandlerResponse;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.file.Path;

public abstract class AbstractPCHandlerFile extends AbstractPCHandlerPath {

  protected static void handleFile(HttpServletRequest request, AbstractRequestedFileData response) throws
      PCHandlerError {
    Path requestedPath = response.getRequestedPath();
    File file = requestedPath.toFile();
    if (!file.exists()) {
      throw new PCHandlerError(PCHandlerResponse.NOT_FOUND, "NO_SUCH_FILE");
    }
    if (!file.isFile()) {
      throw new PCHandlerError("NOT_FILE");
    }
    response.setFile(file);
  }

}
