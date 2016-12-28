package com.dubylon.photochaos.report;

import com.dubylon.photochaos.model.operation.PcoOperationStatus;

import java.util.HashMap;

public class TableReportRow extends HashMap<String, Object> {

  private PcoOperationStatus status;

  public TableReportRow() {
    setStatus(PcoOperationStatus.SUCCESS);
  }

  public void setStatus(PcoOperationStatus status) {
    this.status = status;
    set("STATUS", status.getValue());
  }

  public void set(String name, Object value) {
    put(name, value);
  }
}
