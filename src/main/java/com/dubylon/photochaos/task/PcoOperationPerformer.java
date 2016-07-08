package com.dubylon.photochaos.task;

import com.dubylon.photochaos.model.operation.IPcoOperation;
import com.dubylon.photochaos.model.operation.PcoOperationStatus;

public final class PcoOperationPerformer {

  private PcoOperationPerformer() {
  }

  public static void perform(IPcoOperation op, boolean performOperations) {
    if (!performOperations) {
      op.setStatus(PcoOperationStatus.SUCCESS);
    } else {
      op.perform();
    }
  }
}
