package com.dubylon.photochaos.util;

import com.dubylon.photochaos.report.TableReport;

import static com.dubylon.photochaos.report.TableReport.*;

public class ReportUtil {

  private ReportUtil() {

  }

  public static TableReport buildOperationReport() {
    TableReport opReport = new TableReport();
    opReport.addHeader(FSOP_OPERATION);
    opReport.addHeader(FSOP_SOURCE);
    opReport.addHeader(FSOP_SOURCE_NAME);
    opReport.addHeader(FSOP_DESTINATION);
    opReport.addHeader(FSOP_DESTINATION_NAME);
    opReport.addHeader(FSOP_STATUS);
    return opReport;
  }
}
