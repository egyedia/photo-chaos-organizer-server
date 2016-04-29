package com.dubylon.photochaos.task;

import com.dubylon.photochaos.report.ITaskReport;

import java.util.ArrayList;
import java.util.List;

public class TaskStatus {

  private List<ITaskReport> reports;

  public TaskStatus() {
    reports = new ArrayList<>();
  }

  public List<ITaskReport> getReports() {
    return reports;
  }

  public void setReports(List<ITaskReport> reports) {
    this.reports = reports;
  }

}
