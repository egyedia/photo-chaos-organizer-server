package com.dubylon.photochaos.task;

import com.dubylon.photochaos.rest.task.TaskPreviewOrRunGetData;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class TaskCentralizer {

  private final static TaskCentralizer instance;
  private final static Map<Long, TaskRunInstance> runningInstances;

  static {
    instance = new TaskCentralizer();
    runningInstances = new IdentityHashMap<>();
  }

  private TaskCentralizer() {
  }

  public static TaskCentralizer getInstance() {
    return instance;
  }

  public TaskPreviewOrRunGetData launchTask(IPcoTask task, long taskDefinitionId, boolean performOperations, long userId) {
    TaskPreviewOrRunGetData response = new TaskPreviewOrRunGetData();
    TaskRunInstance tri = runningInstances.get(taskDefinitionId);
    if (tri != null) {
      if (tri.getInstance().isRunning()) {
        response.setStarted(false);
        response.setAlreadyRunning(true);
        return response;
      } else {
        tri = null;
      }
    }
    if (tri == null) {
      tri = new TaskRunInstance(task);
    }
    runningInstances.put(taskDefinitionId, tri);
    ExecutorService executor = Executors.newSingleThreadExecutor();
    final TaskRunInstance runningInstance = tri;
    executor.submit(() -> {
      runningInstance.start(userId);
      task.execute(performOperations);
      runningInstance.finish();
    });
    response.setStarted(true);
    response.setAlreadyRunning(false);
    return response;
  }

  public TaskRunInstance getTaskInstance(long taskDefinitionId) {
    return runningInstances.get(taskDefinitionId);
  }
}
