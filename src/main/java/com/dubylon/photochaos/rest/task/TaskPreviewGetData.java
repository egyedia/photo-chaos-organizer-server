package com.dubylon.photochaos.rest.task;

import com.dubylon.photochaos.rest.PCHandlerResponseData;
import com.dubylon.photochaos.report.ITaskReport;

import java.util.ArrayList;
import java.util.List;

public class TaskPreviewGetData extends PCHandlerResponseData {

  private List<ITaskReport> reports;

  public TaskPreviewGetData() {
    reports = new ArrayList<>();
  }

  public List<ITaskReport> getReports() {
    return reports;
  }

  public void setReports(List<ITaskReport> reports) {
    this.reports = reports;
  }
}
