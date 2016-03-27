package com.dubylon.photochaos.rest.task;

import com.dubylon.photochaos.rest.PCHandlerResponseData;
import com.dubylon.photochaos.task.ITaskReport;

public class TaskPreviewGetData extends PCHandlerResponseData {

  private ITaskReport report;

  public TaskPreviewGetData() {
  }

  public ITaskReport getReport() {
    return report;
  }

  public void setReport(ITaskReport report) {
    this.report = report;
  }
}
