package com.dubylon.photochaos.task;

import com.dubylon.photochaos.model.operation.FilesystemOperation;
import com.dubylon.photochaos.model.operation.PcoOperationType;
import com.dubylon.photochaos.report.TableReport;
import com.dubylon.photochaos.report.TableReportRow;

import java.nio.file.Path;
import java.util.List;

import static com.dubylon.photochaos.report.TableReport.*;

public abstract class AbstractFileSystemTask extends AbstractPcoTask {

  protected void executeOperations(List<FilesystemOperation> fsOpList, TableReport opReport, Path sourcePath, Path
      destinationPath, PreviewOrRun previewOrRun) {
    fsOpList.forEach(op -> {
      /*try {
        System.out.println("Execute operation");
        Thread.sleep(200);
      } catch (Exception e) {
        System.out.println("Not able to sleep");
      }*/

      PcoOperationPerformer.perform(op, previewOrRun);
      if (op.getType() != PcoOperationType.DONOTHING) {
        TableReportRow row = opReport.createRow();
        row.set(FSOP_OPERATION, op.getType());
        row.set(FSOP_SOURCE, op.getSource() == null ? null : sourcePath.relativize(op.getSource()).toString());
        row.set(FSOP_SOURCE_NAME, op.getSourceName() == null ? null : op.getSourceName().toString());
        row.set(FSOP_DESTINATION, op.getDestination() == null ? null : destinationPath.relativize(op.getDestination()
        ).toString());
        row.set(FSOP_DESTINATION_NAME, op.getDestinationName() == null ? null : op.getDestinationName().toString());
        row.set(FSOP_STATUS, op.getStatus());
        row.set(FSOP_ERROR, op.getErrorMessage());
      }
    });
  }

}
