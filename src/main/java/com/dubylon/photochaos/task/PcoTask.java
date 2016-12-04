package com.dubylon.photochaos.task;

public interface PcoTask {

  void execute(PreviewOrRun previewOrRun);

  TaskStatus getStatus();
}
