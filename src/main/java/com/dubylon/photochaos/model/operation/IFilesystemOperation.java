package com.dubylon.photochaos.model.operation;

import java.nio.file.Path;

public interface IFilesystemOperation {

  FilesystemOperationType getType();

  Path getSource();

  Path getSourceName();

  Path getDestination();

  Path getDestinationName();

  void setStatus(FilesystemOperationStatus status);

  FilesystemOperationStatus getStatus();

  void perform();

  String getErrorMessage();
}
