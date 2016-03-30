package com.dubylon.photochaos.task;

import com.dubylon.photochaos.model.operation.FilesystemOperationStatus;
import com.dubylon.photochaos.model.operation.IFilesystemOperation;

public final class FilesystemOperationPerformer {

  private FilesystemOperationPerformer() {
  }

  public static void perform(IFilesystemOperation op, boolean performOperations) {
    if (!performOperations) {
      op.setStatus(FilesystemOperationStatus.SUCCESS);
    } else {
      op.perform();
    }
  }
}
