package com.dubylon.photochaos.util;

import com.dubylon.photochaos.report.TableReportRow;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
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

  public static List<Path> getAllSubfoldersIncluding(Path sourcePath, TableReportRow row, Consumer<TableReportRow>
      action) {
    List<Path> pathList = new ArrayList<>();
    pathList.add(sourcePath);
    action.accept(row);
    detectPaths(sourcePath, pathList, row, action);
    return pathList;
  }

  private static void detectPaths(Path currentPath, List<Path> pathList, TableReportRow row, Consumer<TableReportRow>
      action) {
    try (final Stream<Path> stream = Files.list(currentPath)) {
      stream
          .filter(path -> path.toFile().isDirectory())
          .forEach(path -> {

            /*try {
              System.out.println("Detect paths");
              Thread.sleep(200);
            } catch (Exception e) {
              System.out.println("Not able to sleep");
            }*/

            pathList.add(path);
            action.accept(row);
            detectPaths(path, pathList, row, action);
          });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static List<Path> getAllSubfoldersIncludingExcluding(Path sourcePath, String excludingFolder, TableReportRow
      row, Consumer<TableReportRow> action) {
    List<Path> pathList = new ArrayList<>();
    pathList.add(sourcePath);
    action.accept(row);
    detectPathsExcluding(sourcePath, pathList, excludingFolder, row, action);
    return pathList;
  }

  private static void detectPathsExcluding(Path currentPath, List<Path> pathList, String excludingFolder, TableReportRow
      row, Consumer<TableReportRow> action) {
    try (final Stream<Path> stream = Files.list(currentPath)) {
      stream
          .filter(path -> path.toFile().isDirectory() && !excludingFolder.equalsIgnoreCase(path.getFileName()
              .toString()))
          .forEach(path -> {
            pathList.add(path);
            action.accept(row);
            detectPathsExcluding(path, pathList, excludingFolder, row, action);
          });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
