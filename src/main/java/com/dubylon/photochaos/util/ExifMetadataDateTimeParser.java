package com.dubylon.photochaos.util;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Date;

public final class ExifMetadataDateTimeParser {

  private ExifMetadataDateTimeParser() {
  }

  public static Pair<Instant, Exception> parse(Path path) {
    Instant instant = null;
    File imageFile = path.toFile();
    Metadata metadata;
    //read metadata
    try {
      metadata = ImageMetadataReader.readMetadata(imageFile);
    } catch (IOException | ImageProcessingException e) {
      return new Pair(null, e);
    }

    if (metadata != null) {
      final Directory exifSubIFDDirectory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
      final ExifIFD0Directory exifIFD0Directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);

      // read ExifIFD0Directory/ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL
      if (exifSubIFDDirectory != null) {
        if (exifSubIFDDirectory.containsTag(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL)) {
          Date dto = exifSubIFDDirectory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
          if (dto != null) {
            instant = dto.toInstant();
          }
        }
      }

      // read ExifIFD0Directory/ExifIFD0Directory.TAG_DATETIME
      if (instant == null) {
        if (exifIFD0Directory != null) {
          if (exifIFD0Directory.containsTag(ExifIFD0Directory.TAG_DATETIME)) {
            Date dto = exifIFD0Directory.getDate(ExifIFD0Directory.TAG_DATETIME);
            if (dto != null) {
              instant = dto.toInstant();
            }
          }
        }
      }
    }

    return new Pair(instant, null);
  }
}
