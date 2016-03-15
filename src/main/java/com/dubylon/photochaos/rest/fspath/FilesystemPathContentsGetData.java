package com.dubylon.photochaos.rest.fspath;

import com.dubylon.photochaos.model.response.fs.FsEntry;
import com.dubylon.photochaos.model.response.fs.NamedPath;
import com.dubylon.photochaos.model.response.fs.PathInfo;
import com.dubylon.photochaos.rest.generic.AbstractRequestedPathData;

import java.util.List;

public class FilesystemPathContentsGetData extends AbstractRequestedPathData {

  private List<FsEntry> entryList;
  private List<NamedPath> parentList;
  private PathInfo pathInfo;

  public List<FsEntry> getEntryList() {
    return entryList;
  }

  public void setEntryList(List<FsEntry> entryList) {
    this.entryList = entryList;
  }

  public List<NamedPath> getParentList() {
    return parentList;
  }

  public void setParentList(List<NamedPath> parentList) {
    this.parentList = parentList;
  }

  public PathInfo getPathInfo() {
    return pathInfo;
  }

  public void setPathInfo(PathInfo pathInfo) {
    this.pathInfo = pathInfo;
  }
}
