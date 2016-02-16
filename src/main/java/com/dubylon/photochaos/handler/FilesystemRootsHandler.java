package com.dubylon.photochaos.handler;

import com.dubylon.photochaos.util.PhotoChaosUtil;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

public class FilesystemRootsHandler extends AsbtractPCHandler {

  @Override
  public PCResponseObject doGet(HttpServletRequest request) {
    PCResponseObject response = PCResponseObject.ok();
    Iterable<Path> rootDirectories = FileSystems.getDefault().getRootDirectories();
    List<Object> roots = new ArrayList<>();
    for (Path rootDirectory : rootDirectories) {
      Map<String, Object> root = new HashMap<>();
      String rootPath = rootDirectory.toString();
      rootPath = PhotoChaosUtil.getNormalPath(rootPath);
      root.put("path", rootPath);
      roots.add(root);
    }
    response.setData("roots", roots);
    return response;
  }

  @Override
  public PCResponseObject doPost(HttpServletRequest request) {
    return PCResponseObject.methodNotAllowed();
  }

  @Override
  public PCResponseObject doPut(HttpServletRequest request) {
    return null;
  }

  @Override
  public PCResponseObject doDelete(HttpServletRequest request) {
    return PCResponseObject.methodNotAllowed();
  }

}
