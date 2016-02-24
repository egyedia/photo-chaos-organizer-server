package com.dubylon.photochaos.rest.fsroot;

import com.dubylon.photochaos.rest.PCHandlerError;
import com.dubylon.photochaos.rest.generic.AbstractPCHandler;
import com.dubylon.photochaos.util.PhotoChaosUtil;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilesystemRootsGetHandler extends AbstractPCHandler {

  public FilesystemRootsGetHandler() {
  }

  @Override
  public FilesystemRootsGetData handleRequest(HttpServletRequest request) throws PCHandlerError {
    FilesystemRootsGetData response = new FilesystemRootsGetData();
    Iterable<Path> rootDirectories = FileSystems.getDefault().getRootDirectories();
    List<Map<String, Object>> roots = new ArrayList<>();
    for (Path rootDirectory : rootDirectories) {
      Map<String, Object> root = new HashMap<>();
      String rootPath = rootDirectory.toString();
      rootPath = PhotoChaosUtil.getNormalPath(rootPath);
      root.put("path", rootPath);
      roots.add(root);
    }
    response.setRoots(roots);
    return response;
  }

}
