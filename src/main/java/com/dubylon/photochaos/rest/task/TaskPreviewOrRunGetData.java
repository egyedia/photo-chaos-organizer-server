package com.dubylon.photochaos.rest.task;

import com.dubylon.photochaos.rest.PCHandlerResponseData;
import com.dubylon.photochaos.report.ITaskReport;

import java.util.ArrayList;
import java.util.List;

public class TaskPreviewOrRunGetData extends PCHandlerResponseData {

  private List<ITaskReport> reports;

  public TaskPreviewOrRunGetData() {
    reports = new ArrayList<>();
  }

  public List<ITaskReport> getReports() {
    return reports;
  }

  public void setReports(List<ITaskReport> reports) {
    this.reports = reports;
  }
}
