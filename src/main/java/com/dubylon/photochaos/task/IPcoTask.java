package com.dubylon.photochaos.task;

public interface IPcoTask {

  void execute(boolean performOperations);

  TaskStatus getStatus();
}
