package com.dubylon.photochaos.report;

import java.util.Map;
import java.util.HashMap;

public class TableReportRow {
  private Map<String, Object> cols;

  public TableReportRow() {
    cols = new HashMap<>();
  }

  public void set(String name, Object value) {
    cols.put(name, value);
  }

  public Map<String, Object> getCols() {
    return cols;
  }
}
