package com.dubylon.photochaos.task;

import com.dubylon.photochaos.rest.task.TaskPreviewGetData;

public interface IPcoTask {

  void execute(TaskPreviewGetData response, boolean performOperations);
}
