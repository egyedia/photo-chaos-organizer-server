package com.dubylon.photochaos.model.operation;

import java.nio.file.Path;

public interface IFilesystemOperation extends IPcoOperation {

  Path getSource();

  Path getSourceName();

  Path getDestination();

  Path getDestinationName();
}
