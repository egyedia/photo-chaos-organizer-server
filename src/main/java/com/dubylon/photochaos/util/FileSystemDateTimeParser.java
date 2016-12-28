package com.dubylon.photochaos.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.*;

public final class FileSystemDateTimeParser {

  private FileSystemDateTimeParser() {
  }

  public static Pair<Instant, Exception> parse(Path path) {
    Instant instant = null;
    try {
      BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
      FileTime fileTime = attr.creationTime();
      instant = fileTime.toInstant();
      ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
      LocalDateTime localDateTime = zonedDateTime.toLocalDateTime();
      instant = localDateTime.toInstant(ZoneOffset.UTC);
      return new Pair(instant, null);
    } catch (IOException e) {
      return new Pair(null, e);
    }
  }
}
