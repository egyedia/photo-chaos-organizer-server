package com.dubylon.photochaos.model.operation;

import java.nio.file.Path;

public interface FilesystemOperation extends PcoOperation {

  Path getSource();

  Path getSourceName();

  Path getDestination();

  Path getDestinationName();
}
