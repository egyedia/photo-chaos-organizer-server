package com.dubylon.photochaos.task;

import com.dubylon.photochaos.report.TaskReport;

import java.util.ArrayList;
import java.util.List;

public class TaskStatus {

  private List<TaskReport> reports;

  public TaskStatus() {
    reports = new ArrayList<>();
  }

  public List<TaskReport> getReports() {
    return reports;
  }

}
