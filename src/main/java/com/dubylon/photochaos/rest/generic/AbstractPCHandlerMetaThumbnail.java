package com.dubylon.photochaos.rest.generic;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.ExifThumbnailDirectory;
import com.dubylon.photochaos.model.meta.meta.ThumbnailMeta;
import com.dubylon.photochaos.rest.PCHandlerError;
import com.dubylon.photochaos.rest.PCHandlerResponse;
import com.dubylon.photochaos.rest.thumbmeta.FilesystemMetaThumbnailMetaData;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Date;

public abstract class AbstractPCHandlerMetaThumbnail extends AbstractPCHandlerPath {

  protected static void handleFile(HttpServletRequest request, FilesystemMetaThumbnailMetaData response) throws
      PCHandlerError {
    Path requestedPath = response.getRequestedPath();
    File imageFile = requestedPath.toFile();
    if (!imageFile.exists()) {
      throw new PCHandlerError(PCHandlerResponse.NOT_FOUND, "NO_SUCH_FILE");
    }
    if (!imageFile.isFile()) {
      throw new PCHandlerError("NOT_FILE");
    }
    response.setImage(imageFile);
  }

  protected static void handleMetadataObject(HttpServletRequest request, FilesystemMetaThumbnailMetaData response)
      throws
      PCHandlerError {
    File imageFile = response.getImage();
    Metadata metadata = null;
    try {
      metadata = ImageMetadataReader.readMetadata(imageFile);
    } catch (ImageProcessingException ex) {
      throw new PCHandlerError("IMAGE_PROCESSING_EXCEPTION", ex);
    } catch (IOException ex) {
      throw new PCHandlerError("IO_EXCEPTION", ex);
    }
    response.setMetadata(metadata);
  }

  protected static void handleMetadata(HttpServletRequest request, FilesystemMetaThumbnailMetaData response, boolean
      extractThumbnailData) throws PCHandlerError {
    Metadata metadata = response.getMetadata();
    long w = -1;
    long h = -1;
    int o = -1;
    Date dto = null;
    byte[] thumbnailData = null;

    Directory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
    if (directory != null) {
      dto = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
    }


    ExifThumbnailDirectory thumbDir = metadata.getFirstDirectoryOfType(ExifThumbnailDirectory.class);
    if (thumbDir != null) {
      if (extractThumbnailData && thumbDir.hasThumbnailData()) {
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
        throw new PCHandlerError("METADATA_EXCEPTION", ex);
      }
    }

    if (o == -1) {
      final ExifIFD0Directory exifIFD0Directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
      if (exifIFD0Directory != null) {
        if (exifIFD0Directory.containsTag(ExifIFD0Directory.TAG_ORIENTATION)) {
          try {
            o = exifIFD0Directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);
          } catch (MetadataException ex) {
            throw new PCHandlerError("METADATA_EXCEPTION", ex);
          }
        }
      }
    }

    ThumbnailMeta tm = new ThumbnailMeta();
    response.setExtractedMeta(tm);
    tm.setWidth(w);
    tm.setHeight(h);
    tm.setOrientation(o);
    tm.setDateTimeOriginal(dto == null ? -1 : dto.getTime());
    if (extractThumbnailData) {
      response.setThumbnailData(thumbnailData);
    }

  }

}
