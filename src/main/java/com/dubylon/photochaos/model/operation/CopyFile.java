package com.dubylon.photochaos.model.operation;

import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.file.Path;

public class CopyFile extends AbstractFilesystemOperation {

  private Path fileName;
  private Path sourcePath;
  private Path destinationPath;

  public CopyFile(Path fileName, Path sourcePath, Path destinationPath) {
    this.fileName = fileName;
    this.sourcePath = sourcePath;
    this.destinationPath = destinationPath;
  }

  @Override
  public PcoOperationType getType() {
    return PcoOperationType.COPYFILE;
  }

  @Override
  public Path getSource() {
    return sourcePath;
  }

  @Override
  public Path getSourceName() {
    return fileName;
  }

  @Override
  public Path getDestination() {
    return destinationPath;
  }

  @Override
  public Path getDestinationName() {
    return fileName;
  }

  @Override
  public void perform() {
    if (exception != null) {
      setStatus(PcoOperationStatus.ERROR);
    } else {
      try {
        FileUtils.copyFile(sourcePath.resolve(fileName).toFile(), destinationPath.resolve(fileName).toFile());
        setStatus(PcoOperationStatus.SUCCESS);
      } catch (IOException e) {
        e.printStackTrace();
        setStatus(PcoOperationStatus.ERROR);
        errorMessage = e.getMessage();
      }
    }
  }

}
