package com.dubylon.photochaos.handler;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.ExifThumbnailDirectory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;

public abstract class AsbtractMetaThumbnailHandler extends AsbtractPCHandler {

  protected static PCResponseObject handleFile(HttpServletRequest request, PCResponseObject response) {
    Path requestedPath = (Path) response.getData("requestedPath");
    File imageFile = requestedPath.toFile();
    if (!imageFile.exists()) {
      return PCResponseObject.notFound("NO_SUCH_FILE");
    }
    if (!imageFile.isFile()) {
      return PCResponseObject.error("NOT_FILE");
    }
    return new PCResponseObject(response).setData("imageFile", imageFile);
  }

  protected static PCResponseObject handleMetadataObject(HttpServletRequest request, PCResponseObject response) {
    File imageFile = (File) response.getData("imageFile");
    Metadata metadata = null;
    try {
      metadata = ImageMetadataReader.readMetadata(imageFile);
    } catch (ImageProcessingException ex) {
      return PCResponseObject.error("IMAGE_PROCESSING_EXCEPTION", ex);
    } catch (IOException ex) {
      return PCResponseObject.error("IO_EXCEPTION", ex);
    }
    return new PCResponseObject(response).setData("metadata", metadata);
  }

  protected static PCResponseObject handleMetadata(HttpServletRequest request, PCResponseObject response, boolean extractThumbnailData) {
    Metadata metadata = (Metadata) response.getData("metadata");
    long w = 0;
    long h = 0;
    int o = 0;
    Date dto = null;
    byte[] thumbnailData = null;

    Directory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
    if (directory != null) {
      dto = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
    }

    ExifThumbnailDirectory thumbDir = metadata.getFirstDirectoryOfType(ExifThumbnailDirectory.class);
    if (thumbDir != null) {
      if (extractThumbnailData) {
        thumbnailData = thumbDir.getThumbnailData();
      }
      try {
        if (thumbDir.containsTag(ExifThumbnailDirectory.TAG_IMAGE_WIDTH)) {
          w = thumbDir.getLong(ExifThumbnailDirectory.TAG_IMAGE_WIDTH);
        }
        if (thumbDir.containsTag(ExifThumbnailDirectory.TAG_IMAGE_HEIGHT)) {
          h = thumbDir.getLong(ExifThumbnailDirectory.TAG_IMAGE_HEIGHT);
        }
        if (thumbDir.containsTag(ExifThumbnailDirectory.TAG_ORIENTATION)) {
          o = thumbDir.getInt(ExifThumbnailDirectory.TAG_ORIENTATION);
        }
      } catch (MetadataException ex) {
        return PCResponseObject.error("METADATA_EXCEPTION", ex);
      }
    }
    PCResponseObject r2 = new PCResponseObject(response);
    r2.setData("height", String.valueOf(h));
    r2.setData("width", String.valueOf(w));
    r2.setData("orientation", String.valueOf(o));
    r2.setData("dateTimeOriginal", String.valueOf(dto == null ? "" : String.valueOf(dto.getTime())));
    if (extractThumbnailData) {
      r2.setData("thumbnailData", thumbnailData);
    }

    return r2;
  }

}
