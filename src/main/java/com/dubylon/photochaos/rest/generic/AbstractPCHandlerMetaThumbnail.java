package com.dubylon.photochaos.rest.generic;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.ExifThumbnailDirectory;
import com.dubylon.photochaos.model.response.meta.ThumbnailMeta;
import com.dubylon.photochaos.rest.PCHandlerError;
import com.dubylon.photochaos.rest.PCHandlerResponse;
import com.dubylon.photochaos.rest.thumbmeta.FilesystemMetaThumbnailMetaGetData;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Date;

public abstract class AbstractPCHandlerMetaThumbnail extends AbstractPCHandlerPath {

  protected static void handleFile(HttpServletRequest request, FilesystemMetaThumbnailMetaGetData response) throws
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

  protected static void handleMetadataObject(HttpServletRequest request, FilesystemMetaThumbnailMetaGetData response)
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

  protected static void handleMetadata(HttpServletRequest request, FilesystemMetaThumbnailMetaGetData response, boolean
      extractThumbnailData) throws PCHandlerError {
    Metadata metadata = response.getMetadata();
    byte[] thumbnailData = null;

    ThumbnailMeta tm = new ThumbnailMeta();

    Directory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
    if (directory != null) {
      if (directory.hasTagName(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL)) {
        Date dto = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
        if (dto != null) {
          tm.setDateTimeOriginalRead(true);
        }
        tm.setDateTimeOriginal(dto == null ? -1 : dto.getTime());
      }
    }

    ExifThumbnailDirectory thumbDir = metadata.getFirstDirectoryOfType(ExifThumbnailDirectory.class);
    if (thumbDir != null) {
      if (thumbDir.hasThumbnailData()) {
        tm.setExifThumbReadable(true);
        if (extractThumbnailData) {
          thumbnailData = thumbDir.getThumbnailData();
        }
      }
      try {
        if (thumbDir.containsTag(ExifThumbnailDirectory.TAG_IMAGE_WIDTH)) {
          tm.setWidth(thumbDir.getLong(ExifThumbnailDirectory.TAG_IMAGE_WIDTH));
          tm.setWidthRead(true);
        }
        if (thumbDir.containsTag(ExifThumbnailDirectory.TAG_IMAGE_HEIGHT)) {
          tm.setHeight(thumbDir.getLong(ExifThumbnailDirectory.TAG_IMAGE_HEIGHT));
          tm.setHeightRead(true);
        }
        if (thumbDir.containsTag(ExifThumbnailDirectory.TAG_ORIENTATION)) {
          tm.setOrientation(thumbDir.getInt(ExifThumbnailDirectory.TAG_ORIENTATION));
          tm.setOrientationRead(true);
        }
      } catch (MetadataException ex) {
        throw new PCHandlerError("METADATA_EXCEPTION", ex);
      }
    }

    if (!tm.isOrientationRead()) {
      final ExifIFD0Directory exifIFD0Directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
      if (exifIFD0Directory != null) {
        if (exifIFD0Directory.containsTag(ExifIFD0Directory.TAG_ORIENTATION)) {
          try {
            tm.setOrientation(exifIFD0Directory.getInt(ExifIFD0Directory.TAG_ORIENTATION));
            tm.setOrientationRead(true);
          } catch (MetadataException ex) {
            throw new PCHandlerError("METADATA_EXCEPTION", ex);
          }
        }
      }
    }

    response.setExtractedMeta(tm);
    if (extractThumbnailData) {
      response.setThumbnailData(thumbnailData);
    }
  }
}
