package com.dubylon.photochaos.model.operation;

import java.nio.file.Path;

public interface FilesystemOperation {

  FilesystemOperationType getType();

  Path getSource();

  Path getSourceName();

  Path getDestination();

  Path getDestinationName();

  FilesystemOperationStatus getStatus();
}
