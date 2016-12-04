package com.dubylon.photochaos.task;

import com.dubylon.photochaos.model.operation.PcoOperation;
import com.dubylon.photochaos.model.operation.PcoOperationStatus;

public final class PcoOperationPerformer {

  private PcoOperationPerformer() {
  }

  public static void perform(PcoOperation op, PreviewOrRun previewOrRun) {
    if (previewOrRun == previewOrRun.PREVIEW) {
      if (op.getException() == null) {
        op.setStatus(PcoOperationStatus.SUCCESS);
      } else {
        op.setStatus(PcoOperationStatus.ERROR);
      }
    } else {
      op.perform();
    }
  }
}
