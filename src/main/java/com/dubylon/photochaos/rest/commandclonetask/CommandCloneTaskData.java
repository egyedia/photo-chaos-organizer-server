package com.dubylon.photochaos.rest.commandclonetask;

import com.dubylon.photochaos.rest.PCHandlerResponseData;

public class CommandCloneTaskData extends PCHandlerResponseData {

  private boolean cloned;

  public CommandCloneTaskData() {
  }

  public boolean isCloned() {
    return cloned;
  }

  public void setCloned(boolean cloned) {
    this.cloned = cloned;
  }
}
