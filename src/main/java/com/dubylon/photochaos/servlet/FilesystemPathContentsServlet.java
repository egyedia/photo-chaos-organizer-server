package com.dubylon.photochaos.servlet;

import com.dubylon.photochaos.util.PhotoChaosUtil;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.NoSuchFileException;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;

public class FilesystemPathContentsServlet extends AbstractPhotoChaosServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    Map<String, Object> rm = new HashMap<>();

    // Get path from request and normalize
    String path = request.getPathInfo();
    if (path != null && path.indexOf("/") == 0) {
      path = path.substring(1);
    }
    if (path == null || path.length() == 0) {
      error(response, "MISSING_PATH", "Path should be passed as Base64 encoded string.");
      return;
    }

    // Decode
    String decodedPath = StringUtils.newStringUtf8(Base64.decodeBase64(path));

    // Check path existence
    Path requestedPath = null;
    try {
      requestedPath = Paths.get(decodedPath);
    } catch (InvalidPathException ex) {
      error(response, "INVALID_PATH", ex.toString());
      return;
    }
    if (requestedPath == null || requestedPath.toString().length() == 0) {
      error(response, "ERRONOUS_PATH", "Invalid path requested:" + path);
      return;
    }

    // Add content list
    List<Object> contentList = new ArrayList<>();
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(requestedPath)) {
      for (Path file : stream) {
        Map<String, Object> entry = new HashMap<>();
        BasicFileAttributes attrs = Files.readAttributes(file, BasicFileAttributes.class);
        entry.put("name", file.getFileName());
        Map<String, Object> attributes = new HashMap<>();
        entry.put("attributes", attributes);
        attributes.put("size", attrs.size());
        attributes.put("lastModTime", attrs.lastModifiedTime());
        attributes.put("isDir", attrs.isDirectory());
        attributes.put("isSymlink", attrs.isSymbolicLink());
        attributes.put("isRegular", attrs.isRegularFile());
        contentList.add(entry);
      }
    } catch (NoSuchFileException ex) {
      notfound(response, "NO_SUCH_FILE", ex.toString());
      return;
    } catch (NotDirectoryException ex) {
      error(response, "NOT_DIRECTORY", ex.toString());
      return;
    } catch (IOException ex) {
      error(response, "IO_ERROR", ex.toString());
      return;
    }
    rm.put("contentList", contentList);

    // Is requested path root?
    boolean isRoot = requestedPath.getRoot().equals(requestedPath);

    // Add path info
    Map<String, Object> pathInfo = new HashMap<>();
    rm.put("pathInfo", pathInfo);
    pathInfo.put("path", PhotoChaosUtil.getNormalPath(requestedPath));
    pathInfo.put("isRoot", isRoot);
    pathInfo.put("parentPath", isRoot ? null : PhotoChaosUtil.getNormalPath(requestedPath.getParent()));

    // Add parent list
    List<Map<String, Object>> parentList = new ArrayList<>();
    if (!isRoot) {
      String requestedPathRoot = PhotoChaosUtil.getNormalPath(requestedPath.getRoot());
      boolean rootReached = false;
      boolean addCurrentPath = true;
      Path currentPath = requestedPath;
      String normalPath = PhotoChaosUtil.getNormalPath(currentPath);
      do {
        if (rootReached && addCurrentPath) {
          addCurrentPath = false;
        }
        Map<String, Object> parent = new HashMap<>();
        parentList.add(0, parent);
        parent.put("path", normalPath);
        parent.put("name", currentPath.getFileName());
        // go up one level
        currentPath = currentPath.getParent();
        normalPath = PhotoChaosUtil.getNormalPath(currentPath);
        if (normalPath != null && normalPath.equals(requestedPathRoot)) {
          rootReached = true;
        }
      } while (!rootReached || addCurrentPath);
    }
    rm.put("parentList", parentList);

    ok(response, rm);
  }
}
