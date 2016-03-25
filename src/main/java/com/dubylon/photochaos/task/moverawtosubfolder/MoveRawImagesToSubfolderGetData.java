package com.dubylon.photochaos.task.moverawtosubfolder;

import com.dubylon.photochaos.rest.PCHandlerResponseData;
import com.dubylon.photochaos.task.ITaskReport;

public class MoveRawImagesToSubfolderGetData extends PCHandlerResponseData {

  private ITaskReport headerReport;
  private ITaskReport detailsReport;

  public ITaskReport getHeaderReport() {
    return headerReport;
  }

  public void setHeaderReport(ITaskReport headerReport) {
    this.headerReport = headerReport;
  }

  public ITaskReport getDetailsReport() {
    return detailsReport;
  }

  public void setDetailsReport(ITaskReport detailsReport) {
    this.detailsReport = detailsReport;
  }
}
