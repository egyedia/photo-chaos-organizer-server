package com.dubylon.photochaos.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FileSystemUtil {

  private FileSystemUtil() {

  }

  public static boolean isDirectoryAndReadable(Path path) {
    File pathFile = path.toFile();
    return pathFile.exists() && pathFile.isDirectory() && pathFile.canRead();
  }

  public static boolean isDirectoryAndWritable(Path path) {
    File pathFile = path.toFile();
    return pathFile.exists() && pathFile.isDirectory() && pathFile.canWrite();
  }

  public static List<Path> getAllSubfoldersIncluding(Path sourcePath) {
    List<Path> pathList = new ArrayList<>();
    pathList.add(sourcePath);
    detectPaths(sourcePath, pathList);
    return pathList;
  }

  private static void detectPaths(Path currentPath, List<Path> pathList) {
    try (final Stream<Path> stream = Files.list(currentPath)) {
      stream
          .filter(path -> path.toFile().isDirectory())
          .forEach(path -> {
            pathList.add(path);
            detectPaths(path, pathList);
          });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static List<Path> getAllSubfoldersIncludingExcluding(Path sourcePath, String excludingFolder) {
    List<Path> pathList = new ArrayList<>();
    pathList.add(sourcePath);
    detectPathsExcluding(sourcePath, pathList, excludingFolder);
    return pathList;
  }

  private static void detectPathsExcluding(Path currentPath, List<Path> pathList, String excludingFolder) {
    try (final Stream<Path> stream = Files.list(currentPath)) {
      stream
          .filter(path -> path.toFile().isDirectory() && !excludingFolder.equalsIgnoreCase(path.getFileName()
              .toString()))
          .forEach(path -> {
            pathList.add(path);
            detectPathsExcluding(path, pathList, excludingFolder);
          });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
