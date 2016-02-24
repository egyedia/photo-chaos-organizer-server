package com.dubylon.photochaos.rest.fspath;

import com.dubylon.photochaos.rest.PCHandlerResponseData;
import com.dubylon.photochaos.rest.generic.AbstractRequestedPathData;

import java.util.List;
import java.util.Map;

public class FilesystemPathContentsGetData extends AbstractRequestedPathData {

  private List<Object> contentList;
  private List<Map<String, Object>> parentList;
  private Map<String, Object> pathInfo;
  private boolean isRoot;

  public List<Object> getContentList() {
    return contentList;
  }

  public void setContentList(List<Object> contentList) {
    this.contentList = contentList;
  }

  public List<Map<String, Object>> getParentList() {
    return parentList;
  }

  public void setParentList(List<Map<String, Object>> parentList) {
    this.parentList = parentList;
  }

  public Map<String, Object> getPathInfo() {
    return pathInfo;
  }

  public void setPathInfo(Map<String, Object> pathInfo) {
    this.pathInfo = pathInfo;
  }

  public boolean isRoot() {
    return isRoot;
  }

  public void setIsRoot(boolean isRoot) {
    this.isRoot = isRoot;
  }
}
