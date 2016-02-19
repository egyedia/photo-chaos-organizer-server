package com.dubylon.photochaos.rest.fsroot;

import com.dubylon.photochaos.rest.PCHandlerResponseData;

import java.util.List;
import java.util.Map;

public class FilesystemRootsData extends PCHandlerResponseData {

  private List<Map<String, Object>> roots;

  public FilesystemRootsData() {
  }

  public List<Map<String, Object>> getRoots() {
    return roots;
  }

  public void setRoots(List<Map<String, Object>> roots) {
    this.roots = roots;
  }
}
