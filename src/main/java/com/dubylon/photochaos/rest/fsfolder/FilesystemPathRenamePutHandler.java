package com.dubylon.photochaos.rest.fsfolder;

import com.dubylon.photochaos.model.request.FilesystemPathRename;
import com.dubylon.photochaos.rest.PCHandlerError;
import com.dubylon.photochaos.rest.generic.AbstractPCHandlerPath;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FilesystemPathRenamePutHandler extends AbstractPCHandlerPath {

  @Override
  public FilesystemPathRenamePutData handleRequest(HttpServletRequest request) throws PCHandlerError {
    FilesystemPathRenamePutData response = new FilesystemPathRenamePutData();
    handlePath(request, response);
    Path requestedPath = response.getRequestedPath();
    Path parentPath = requestedPath.getParent();
    if (parentPath != null) {

      String content = readNonEmptyContent(request, "New path name should be passed in a json object in request body.");
      ObjectMapper mapper = new ObjectMapper();
      FilesystemPathRename rr = null;
      try {
        rr = mapper.readValue(content, FilesystemPathRename.class);
      } catch (IOException e) {
        e.printStackTrace();
      }
      if (rr != null) {
        Path newPath = parentPath.resolve(rr.getName());

        try {
          Files.move(requestedPath, newPath);
          response.setRenamed(true);
          response.setPath(newPath.toString());
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return response;
  }
}
