package com.dubylon.photochaos.report;

import java.util.ArrayList;
import java.util.List;

public class TableReport implements ITaskReport {

  public static final String FSOP_OPERATION = "OPERATION";
  public static final String FSOP_SOURCE = "SOURCE";
  public static final String FSOP_SOURCE_NAME = "SOURCE_NAME";
  public static final String FSOP_DESTINATION = "DESTINATION";
  public static final String FSOP_DESTINATION_NAME = "DESTINATION_NAME";
  public static final String FSOP_STATUS = "STATUS";

  private List<String> headers;
  private List<TableReportRow> rows;

  public TableReport() {
    this.headers = new ArrayList<>();
    this.rows = new ArrayList<>();
  }

  public void addHeader(String header) {
    headers.add(header);
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
}
