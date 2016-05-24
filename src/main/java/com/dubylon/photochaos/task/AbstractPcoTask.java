package com.dubylon.photochaos.task;

import com.dubylon.photochaos.Defaults;

public abstract class AbstractPcoTask implements IPcoTask {

  protected TaskStatus status;

  public AbstractPcoTask() {
    this.status = new TaskStatus();
  }

  @Override
  public TaskStatus getStatus() {
    return status;
  }

  public void setStatus(TaskStatus status) {
    this.status = status;
  }

  protected String buildKnownGlobFilter() {
    // Build the glob for matching known files
    StringBuilder sb = new StringBuilder();
    sb.append("regex:");
    sb.append("([^\\s]+(\\.(?i)(");
    final StringBuilder separator = new StringBuilder();
    Defaults.FILE_EXTENSIONS.forEach((ext, desc) -> {
      sb.append(separator);
      sb.append(ext);
      if (separator.length() == 0) {
        separator.append("|");
      }
    });
    sb.append("))$)");
    return sb.toString();
  }
}
