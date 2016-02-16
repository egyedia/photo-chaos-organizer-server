package com.dubylon.photochaos.handler;

import com.dubylon.photochaos.util.PhotoChaosUtil;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

public class FilesystemPathContentsHandler extends AsbtractPCHandler {

  @Override
  public PCResponseObject doGet(HttpServletRequest request) {
    PCResponseObject response = handlePath(request);
    if (response.isSuccess()) {
      response = handleContentList(request, response);
      if (response.isSuccess()) {
        response = handlePathInfo(request, response);
        response = handleParentInfo(request, response);
      }
    }
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

  private PCResponseObject handleContentList(HttpServletRequest request, PCResponseObject response) {
    Path requestedPath = (Path) response.getData("requestedPath");
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
      return PCResponseObject.notFound("NO_SUCH_FILE", ex);
    } catch (NotDirectoryException ex) {
      return PCResponseObject.error("NOT_DIRECTORY", ex);
    } catch (IOException ex) {
      return PCResponseObject.error("IO_ERROR", ex);
    }
    return new PCResponseObject(response).setData("contentList", contentList);
  }

  private PCResponseObject handlePathInfo(HttpServletRequest request, PCResponseObject response) {
    Path requestedPath = (Path) response.getData("requestedPath");
    // Is requested path root?
    boolean isRoot = requestedPath.getRoot().equals(requestedPath);
    // Add path info
    Map<String, Object> pathInfo = new HashMap<>();
    pathInfo.put("path", PhotoChaosUtil.getNormalPath(requestedPath));
    pathInfo.put("isRoot", isRoot);
    pathInfo.put("parentPath", isRoot ? null : PhotoChaosUtil.getNormalPath(requestedPath.getParent()));

    return new PCResponseObject(response).setData("pathInfo", pathInfo).setData("isRoot", isRoot);
  }

  private PCResponseObject handleParentInfo(HttpServletRequest request, PCResponseObject response) {
    Path requestedPath = (Path) response.getData("requestedPath");
    boolean isRoot = (Boolean) response.getData("isRoot");
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
    return new PCResponseObject(response).setData("parentList", parentList);
  }

}
