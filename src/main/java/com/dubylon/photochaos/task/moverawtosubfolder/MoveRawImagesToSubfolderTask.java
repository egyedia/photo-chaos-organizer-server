package com.dubylon.photochaos.task.moverawtosubfolder;

import com.dubylon.photochaos.model.tasktemplate.TaskTemplateParameterType;
import com.dubylon.photochaos.task.IPcoTask;
import com.dubylon.photochaos.task.PcoTaskTemplate;
import com.dubylon.photochaos.task.PcoTaskTemplateParameter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@PcoTaskTemplate(languageKeyPrefix = "task.moveRawImagesToSubfolders.")
public class MoveRawImagesToSubfolderTask implements IPcoTask {

  @PcoTaskTemplateParameter(
      type = TaskTemplateParameterType.PATH,
      mandatory = true,
      defaultValue = ""
  )
  private String workingFolder;

  @PcoTaskTemplateParameter(
      type = TaskTemplateParameterType.FOLDERNAME,
      mandatory = true,
      defaultValue = "raw"
  )
  private String rawFolder;

  private MoveRawImagesToSubfolderGetData response;
  private boolean performOperations;

  private List<Path> pathList;

  public MoveRawImagesToSubfolderTask(MoveRawImagesToSubfolderGetData response, boolean performOperations) {
    this.response = response;
    this.performOperations = performOperations;
  }

  public void setWorkingFolder(String workingFolder) {
    this.workingFolder = workingFolder;
  }

  public void setRawFolder(String rawFolder) {
    this.rawFolder = rawFolder;
  }

  private void detectPaths(Path currentPath) {
    try (final Stream<Path> stream = Files.list(currentPath)) {
      stream
          .filter(path -> path.toFile().isDirectory() && !rawFolder.equals(path.getFileName().toString()))
          .forEach(path -> {
            pathList.add(path);
            detectPaths(path);
          });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void moveFiles(Path currentPath) {
    Path rawPath = currentPath.resolve(rawFolder);
    if (rawPath.toFile().exists()) {
      if (rawPath.toFile().isDirectory()) {
        System.out.println(rawFolder + " already present, no need to create it:" + rawPath);
      } else {
        System.out.println(rawFolder + " already present, but not a directory. Unable to perform operation, skipping " +
            "folder:" + currentPath);
        return;
      }
    } else {
      System.out.println(rawFolder + " is not present, need to create it:" + rawPath);
    }

    //TODO build the extension list from real data
    final PathMatcher filter = currentPath.getFileSystem().getPathMatcher("glob:*.{cr2,nef}");
    try (final Stream<Path> stream = Files.list(currentPath)) {
      stream
          .filter(path -> path.toFile().isFile() && filter.matches(path.getFileName()))
          .forEach(path -> {
            System.out.println("Move file:" + path.getFileName() + " from:" + currentPath + " to:" + rawPath);
          });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  public void execute() {
    pathList = new ArrayList<>();
    //TODO set reports
    Path workingFolderPath = Paths.get(workingFolder);
    File workingFolderFile = workingFolderPath.toFile();
    boolean workingFolderOk = workingFolderFile.exists()
        && workingFolderFile.isDirectory()
        && workingFolderFile.canRead();

    if (workingFolderOk) {
      detectPaths(workingFolderPath);

    }
    pathList.forEach(this::moveFiles);
  }

  public static void main(String[] args) {
    MoveRawImagesToSubfolderTask t = new MoveRawImagesToSubfolderTask(null, false);
    t.setWorkingFolder("/atti/development/photo-chaos-organizer-images/moverawimagestosubfolder/");
    t.setRawFolder("raw");
    t.execute();
  }
}
