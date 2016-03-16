package com.dubylon.photochaos.rest.generic;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.ExifThumbnailDirectory;
import com.dubylon.photochaos.model.response.meta.BigImageMeta;
import com.dubylon.photochaos.model.response.meta.ImageExtractedMeta;
import com.dubylon.photochaos.model.response.meta.ThumbnailMeta;
import com.dubylon.photochaos.rest.PCHandlerError;
import com.dubylon.photochaos.rest.thumbdata.FilesystemMetaThumbnailDataGetData;
import com.dubylon.photochaos.rest.thumbmeta.FilesystemMetaThumbnailMetaGetData;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Date;

public abstract class AbstractPCHandlerMetaThumbnail extends AbstractPCHandlerFile {

  protected static void handleMetadataObject(HttpServletRequest request, AbstractFilesystemMetadataData response)
      throws
      PCHandlerError {
    File imageFile = response.getFile();
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

  protected static void handleMetadata(HttpServletRequest request, FilesystemMetaThumbnailMetaGetData response)
      throws PCHandlerError {
    Metadata metadata = response.getMetadata();

    ThumbnailMeta thm = new ThumbnailMeta();
    BigImageMeta bim = new BigImageMeta();
    ImageExtractedMeta em = new ImageExtractedMeta();
    em.setImage(bim);
    em.setThumbnail(thm);

    final Directory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
    if (directory != null) {
      if (directory.hasTagName(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL)) {
        Date dto = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
        if (dto != null) {
          bim.setDateTimeOriginalRead(true);
          bim.setDateTimeOriginal(dto.getTime());
        }
      }
    }
    final ExifIFD0Directory exifIFD0Directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
    if (exifIFD0Directory != null) {
      try {
        if (exifIFD0Directory.containsTag(ExifIFD0Directory.TAG_ORIENTATION)) {
          bim.setOrientation(exifIFD0Directory.getInt(ExifIFD0Directory.TAG_ORIENTATION));
          bim.setOrientationRead(true);
        }
      } catch (MetadataException ex) {
        //Do not log
      }
      try {
        if (exifIFD0Directory.containsTag(ExifIFD0Directory.TAG_IMAGE_WIDTH)) {
          bim.setWidth(exifIFD0Directory.getInt(ExifIFD0Directory.TAG_IMAGE_WIDTH));
          bim.setWidthRead(true);
        }
      } catch (MetadataException ex) {
        //Do not log
      }
      try {
        if (exifIFD0Directory.containsTag(ExifIFD0Directory.TAG_IMAGE_HEIGHT)) {
          bim.setHeight(exifIFD0Directory.getInt(ExifIFD0Directory.TAG_IMAGE_HEIGHT));
          bim.setHeightRead(true);
        }
      } catch (MetadataException ex) {
        //Do not log
      }
    }

    ExifThumbnailDirectory thumbDir = metadata.getFirstDirectoryOfType(ExifThumbnailDirectory.class);
    if (thumbDir != null) {
      if (thumbDir.hasThumbnailData()) {
        thm.setExifThumbReadable(true);
      }
      try {
        if (thumbDir.containsTag(ExifThumbnailDirectory.TAG_ORIENTATION)) {
          thm.setOrientation(thumbDir.getInt(ExifThumbnailDirectory.TAG_ORIENTATION));
          thm.setOrientationRead(true);

        }
      } catch (MetadataException ex) {
        //Do not log
      }
      try {
        if (thumbDir.containsTag(ExifThumbnailDirectory.TAG_IMAGE_WIDTH)) {
          thm.setWidth(thumbDir.getLong(ExifThumbnailDirectory.TAG_IMAGE_WIDTH));
          thm.setWidthRead(true);
        }
      } catch (MetadataException ex) {
        //Do not log
      }
      try {
        if (thumbDir.containsTag(ExifThumbnailDirectory.TAG_IMAGE_HEIGHT)) {
          thm.setHeight(thumbDir.getLong(ExifThumbnailDirectory.TAG_IMAGE_HEIGHT));
          thm.setHeightRead(true);
        }
      } catch (MetadataException ex) {
        //Do not log
      }
    }

    response.setExtractedMeta(em);
  }


  protected void handleMetadataThumbnail(HttpServletRequest request, FilesystemMetaThumbnailDataGetData response) {
    byte[] thumbnailData = null;
    Metadata metadata = response.getMetadata();

    ExifThumbnailDirectory thumbDir = metadata.getFirstDirectoryOfType(ExifThumbnailDirectory.class);
    if (thumbDir != null) {
      if (thumbDir.hasThumbnailData()) {
        thumbnailData = thumbDir.getThumbnailData();
      }
    }
    response.setThumbnailData(thumbnailData);
  }
}
