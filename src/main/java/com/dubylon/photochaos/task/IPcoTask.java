package com.dubylon.photochaos.task;

import com.dubylon.photochaos.rest.task.TaskPreviewOrRunGetData;

public interface IPcoTask {

  void execute(TaskPreviewOrRunGetData response, boolean performOperations);
}
