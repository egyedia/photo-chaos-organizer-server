package com.dubylon.photochaos.model.operation;

import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.file.Path;

public class CreateFolder extends AbstractFilesystemOperation {

  private Path parent;
  private Path name;

  public CreateFolder(Path parent, Path name) {
    this.parent = parent;
    this.name = name;
  }

  @Override
  public PcoOperationType getType() {
    return PcoOperationType.CREATEFOLDER;
  }

  @Override
  public Path getSource() {
    return null;
  }

  @Override
  public Path getSourceName() {
    return null;
  }

  @Override
  public Path getDestination() {
    return parent;
  }

  @Override
  public Path getDestinationName() {
    return name;
  }

  @Override
  public void perform() {
    try {
      FileUtils.forceMkdir(parent.resolve(name).toFile());
      setStatus(PcoOperationStatus.SUCCESS);
    } catch (IOException e) {
      e.printStackTrace();
      setStatus(PcoOperationStatus.ERROR);
      errorMessage = e.getMessage();
    }
  }

}
