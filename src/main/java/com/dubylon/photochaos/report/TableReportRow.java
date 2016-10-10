package com.dubylon.photochaos.report;

import java.util.HashMap;

public class TableReportRow extends HashMap<String, Object> {

  public TableReportRow() {
  }

  public void set(String name, Object value) {
    put(name, value);
  }
}
