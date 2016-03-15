package com.dubylon.photochaos.rest.fspath;

import com.dubylon.photochaos.model.response.fs.FsEntry;
import com.dubylon.photochaos.model.response.fs.NamedPath;
import com.dubylon.photochaos.model.response.fs.PathInfo;
import com.dubylon.photochaos.rest.PCHandlerError;
import com.dubylon.photochaos.rest.PCHandlerResponse;
import com.dubylon.photochaos.rest.generic.AbstractPCHandlerPath;
import com.dubylon.photochaos.util.FileTypeUtil;
import com.dubylon.photochaos.util.PhotoChaosUtil;
import org.apache.commons.io.FilenameUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FilesystemPathContentsGetHandler extends AbstractPCHandlerPath {

  public FilesystemPathContentsGetHandler() {
  }

  @Override
  public FilesystemPathContentsGetData handleRequest(HttpServletRequest request) throws PCHandlerError {
    FilesystemPathContentsGetData response = new FilesystemPathContentsGetData();
    handlePath(request, response);
    handleContentList(request, response);
    handlePathInfo(request, response);
    handleParentInfo(request, response);
    return response;
  }

  private void handleContentList(HttpServletRequest request, FilesystemPathContentsGetData response) throws
      PCHandlerError {
    Path requestedPath = response.getRequestedPath();
    List<FsEntry> contentList = new ArrayList<>();
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(requestedPath)) {
      for (Path file : stream) {
        FsEntry entry = new FsEntry();
        String fn = file.getFileName().toString();
        String extension = FilenameUtils.getExtension(fn);
        entry.setName(fn);
        entry.setFileType(FileTypeUtil.getPhotoChaosFileTypeDescriptor(extension));
        try {
          BasicFileAttributes attrs = Files.readAttributes(file, BasicFileAttributes.class);
          entry.setEntryType(FileTypeUtil.getFileSystemFileTypeDescriptor(attrs));
          entry.setAttributes(FileTypeUtil.getFileSystemAttributes(attrs, Files.isHidden(file)));
        } catch (Exception e) {
          entry.setReadError(true);
          System.out.println("There was an error reading:" + file.toString() + ". Exception:" + e.getMessage());
        }
        contentList.add(entry);
        Collections.sort(contentList);
      }
    } catch (NoSuchFileException ex) {
      throw new PCHandlerError(PCHandlerResponse.NOT_FOUND, "NO_SUCH_FILE", ex);
    } catch (NotDirectoryException ex) {
      throw new PCHandlerError("NOT_DIRECTORY", ex);
    } catch (IOException ex) {
      throw new PCHandlerError("IO_ERROR", ex);
    }
    response.setEntryList(contentList);
  }

  private void handlePathInfo(HttpServletRequest request, FilesystemPathContentsGetData response) {
    Path requestedPath = response.getRequestedPath();
    // Is requested path root?
    boolean isRoot = requestedPath.getRoot().equals(requestedPath);
    PathInfo pathInfo  = new PathInfo();
    pathInfo.setPath(PhotoChaosUtil.getNormalPath(requestedPath));
    pathInfo.setRoot(isRoot);
    pathInfo.setParentPath(isRoot ? null : PhotoChaosUtil.getNormalPath(requestedPath.getParent()));
    response.setPathInfo(pathInfo);
  }

  private void handleParentInfo(HttpServletRequest request, FilesystemPathContentsGetData response) {
    Path requestedPath = response.getRequestedPath();
    boolean isRoot = response.getPathInfo().isRoot();
    // Add parent list
    List<NamedPath> parentList = new ArrayList<>();
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
        NamedPath parent = new NamedPath();
        parentList.add(0, parent);
        parent.setPath(normalPath);
        parent.setName(currentPath.getFileName() == null ? currentPath.toString() : currentPath.getFileName()
            .toString());
        // go up one level
        currentPath = currentPath.getParent();
        normalPath = PhotoChaosUtil.getNormalPath(currentPath);
        if (normalPath != null && normalPath.equals(requestedPathRoot)) {
          rootReached = true;
        }
      } while (!rootReached || addCurrentPath);
    } else {
      NamedPath parent = new NamedPath();
      parentList.add(0, parent);
      String normalPath = PhotoChaosUtil.getNormalPath(requestedPath);
      parent.setPath(normalPath);
      parent.setName(normalPath);
    }
    response.setParentList(parentList);
  }

}
