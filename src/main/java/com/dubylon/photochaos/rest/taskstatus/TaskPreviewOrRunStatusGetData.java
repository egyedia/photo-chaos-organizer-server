package com.dubylon.photochaos.rest.taskstatus;

import com.dubylon.photochaos.rest.PCHandlerResponseData;
import com.dubylon.photochaos.report.ITaskReport;
import com.dubylon.photochaos.task.RunningInstanceInfo;

import java.util.ArrayList;
import java.util.List;

public class TaskPreviewOrRunStatusGetData extends PCHandlerResponseData {

  private List<ITaskReport> reports;
  private RunningInstanceInfo info;

  public TaskPreviewOrRunStatusGetData() {
    reports = new ArrayList<>();
  }

  public List<ITaskReport> getReports() {
    return reports;
  }

  public void setReports(List<ITaskReport> reports) {
    this.reports = reports;
  }

  public RunningInstanceInfo getInfo() {
    return info;
  }

  public void setInfo(RunningInstanceInfo info) {
    this.info = info;
  }
}
