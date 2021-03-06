package com.dubylon.photochaos.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableReport implements TaskReport {

  public static final String FSOP_OPERATION = "OPERATION";
  public static final String FSOP_SOURCE = "SOURCE";
  public static final String FSOP_SOURCE_NAME = "SOURCE_NAME";
  public static final String FSOP_DESTINATION = "DESTINATION";
  public static final String FSOP_DESTINATION_NAME = "DESTINATION_NAME";
  public static final String FSOP_ERROR = "ERROR";
  public static final String FSOP_PHASE = "PHASE";
  public static final String FSOP_PHASE_VALUE = "PHASE_VALUE";

  private List<String> headers;
  private Map<String, Integer> headerToPos;
  private List<TableReportRow> rows;

  public TableReport() {
    this.headers = new ArrayList<>();
    this.headerToPos = new HashMap<>();
    this.rows = new ArrayList<>();
  }

  public void addHeader(String headerKey) {
    headerToPos.put(headerKey, headers.size());
    headers.add(headerKey);
  }

  public TableReportRow createRow() {
    TableReportRow r = new TableReportRow();
    rows.add(r);
    return r;
  }

  public List<String> getHeaders() {
    return headers;
  }

  public List<TableReportRow> getRows() {
    return rows;
  }

  public int getColumnIndex(String columnName) {
    Integer pos = headerToPos.get(columnName);
    return pos == null ? -1 : pos;
  }
}
