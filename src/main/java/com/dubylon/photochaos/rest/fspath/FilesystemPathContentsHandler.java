package com.dubylon.photochaos.rest.fspath;

import com.dubylon.photochaos.rest.PCHandlerError;
import com.dubylon.photochaos.rest.PCHandlerResponse;
import com.dubylon.photochaos.rest.PCHandlerResponseError;
import com.dubylon.photochaos.rest.generic.AbstractPCHandlerPath;
import com.dubylon.photochaos.util.FileTypeUtil;
import com.dubylon.photochaos.util.PhotoChaosUtil;
import org.apache.commons.io.FilenameUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilesystemPathContentsHandler extends AbstractPCHandlerPath {

  public FilesystemPathContentsHandler() {
  }

  @Override
  public FilesystemPathContentsData doGet(HttpServletRequest request) throws PCHandlerError {
    FilesystemPathContentsData response = new FilesystemPathContentsData();
    handlePath(request, response);
    handleContentList(request, response);
    handlePathInfo(request, response);
    handleParentInfo(request, response);
    return response;
  }

  @Override
  public PCHandlerResponse doPost(HttpServletRequest request) throws PCHandlerError {
    return PCHandlerResponseError.methodNotAllowed();
  }

  @Override
  public PCHandlerResponse doPut(HttpServletRequest request) throws PCHandlerError {
    return PCHandlerResponseError.methodNotAllowed();
  }

  @Override
  public PCHandlerResponse doDelete(HttpServletRequest request) throws PCHandlerError {
    return PCHandlerResponseError.methodNotAllowed();
  }

  private void handleContentList(HttpServletRequest request, FilesystemPathContentsData response) throws
      PCHandlerError {
    Path requestedPath = response.getRequestedPath();
    List<Object> contentList = new ArrayList<>();
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(requestedPath)) {
      for (Path file : stream) {
        Map<String, Object> entry = new HashMap<>();
        BasicFileAttributes attrs = Files.readAttributes(file, BasicFileAttributes.class);
        entry.put("name", file.getFileName().toString());
        Map<String, Object> attributes = new HashMap<>();
        entry.put("attributes", attributes);
        attributes.put("size", attrs.size());
        attributes.put("lastModTime", attrs.lastModifiedTime().toMillis());
        attributes.put("isDir", attrs.isDirectory());
        attributes.put("isSymlink", attrs.isSymbolicLink());
        attributes.put("isRegular", attrs.isRegularFile());
        String extension = FilenameUtils.getExtension(file.getFileName().toString());
        entry.put("canHaveMeta", FileTypeUtil.canHaveThumbnail(extension));
        contentList.add(entry);
      }
    } catch (NoSuchFileException ex) {
      throw new PCHandlerError(PCHandlerResponse.NOT_FOUND, "NO_SUCH_FILE", ex);
    } catch (NotDirectoryException ex) {
      throw new PCHandlerError("NOT_DIRECTORY", ex);
    } catch (IOException ex) {
      throw new PCHandlerError("IO_ERROR", ex);
    }
    response.setContentList(contentList);
  }

  private void handlePathInfo(HttpServletRequest request, FilesystemPathContentsData response) {
    Path requestedPath = response.getRequestedPath();
    // Is requested path root?
    boolean isRoot = requestedPath.getRoot().equals(requestedPath);
    // Add path info
    Map<String, Object> pathInfo = new HashMap<>();
    pathInfo.put("path", PhotoChaosUtil.getNormalPath(requestedPath));
    pathInfo.put("isRoot", isRoot);
    pathInfo.put("parentPath", isRoot ? null : PhotoChaosUtil.getNormalPath(requestedPath.getParent()));

    response.setPathInfo(pathInfo);
    response.setIsRoot(isRoot);
  }

  private void handleParentInfo(HttpServletRequest request, FilesystemPathContentsData response) {
    Path requestedPath = response.getRequestedPath();
    boolean isRoot = response.isRoot();
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
        parent.put("name", currentPath.getFileName() == null? currentPath.toString() : currentPath.getFileName().toString());
        // go up one level
        currentPath = currentPath.getParent();
        normalPath = PhotoChaosUtil.getNormalPath(currentPath);
        if (normalPath != null && normalPath.equals(requestedPathRoot)) {
          rootReached = true;
        }
      } while (!rootReached || addCurrentPath);
    } else {
      Map<String, Object> parent = new HashMap<>();
      parentList.add(0, parent);
      String normalPath = PhotoChaosUtil.getNormalPath(requestedPath);
      parent.put("path", normalPath);
      parent.put("name", normalPath);
    }
    response.setParentList(parentList);
  }

}
