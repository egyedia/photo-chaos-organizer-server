package com.dubylon.photochaos.servlet;

import com.dubylon.photochaos.util.PhotoChaosUtil;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FilesystemRootsServlet extends AbstractPhotoChaosServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    Iterable<Path> rootDirectories = FileSystems.getDefault().getRootDirectories();
    List<Object> roots = new ArrayList<>();
    for (Path rootDirectory : rootDirectories) {
      Map<String, Object> root = new HashMap<>();
      String rootPath = rootDirectory.toString();
      rootPath = PhotoChaosUtil.getNormalPath(rootPath);
      root.put("path", rootPath);
      roots.add(root);
    }
    ok(response, roots);
  }
}
