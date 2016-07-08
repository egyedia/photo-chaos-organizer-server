package com.dubylon.photochaos.task;

import com.dubylon.photochaos.model.operation.IFilesystemOperation;
import com.dubylon.photochaos.report.TableReport;
import com.dubylon.photochaos.report.TableReportRow;

import java.nio.file.Path;
import java.util.List;

import static com.dubylon.photochaos.report.TableReport.*;

public abstract class AbstractFileSystemTask extends AbstractPcoTask {

  protected void executeOperations(List<IFilesystemOperation> fsOpList, TableReport opReport, Path sourcePath, Path
      destinationPath, boolean performOperations) {
    fsOpList.forEach(op -> {
      PcoOperationPerformer.perform(op, performOperations);

      TableReportRow row = opReport.createRow();
      row.set(FSOP_OPERATION, op.getType());
      row.set(FSOP_SOURCE, op.getSource() == null ? null : sourcePath.relativize(op.getSource()));
      row.set(FSOP_SOURCE_NAME, op.getSourceName());
      row.set(FSOP_DESTINATION, op.getDestination() == null ? null : destinationPath.relativize(op.getDestination()));
      row.set(FSOP_DESTINATION_NAME, op.getDestinationName());
      row.set(FSOP_STATUS, op.getStatus());
    });
  }


}
