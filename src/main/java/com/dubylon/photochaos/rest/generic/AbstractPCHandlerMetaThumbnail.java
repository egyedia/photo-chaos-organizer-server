package com.dubylon.photochaos.rest.generic;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.ExifThumbnailDirectory;
import com.drew.metadata.jpeg.JpegDirectory;
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

    /*
    System.out.println("-------------------------------------");
    System.out.println("-------------------------------------");
    System.out.println("-------------------------------------");
    System.out.println(response.getRequestedPath());
    for (Directory directory : metadata.getDirectories()) {
      for (Tag tag : directory.getTags()) {
        System.out.println(tag);
      }
      if (directory.hasErrors()) {
        for (String error : directory.getErrors()) {
          System.err.println("ERROR: " + error);
        }
      }
    }
    System.out.println("***************************");
    System.out.println("***************************");
    System.out.println("***************************");
    */

    // Handle the big image

    // Read directories
    final JpegDirectory jpegDirectory = metadata.getFirstDirectoryOfType(JpegDirectory.class);
    final Directory exifSubIFDDirectory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
    final ExifIFD0Directory exifIFD0Directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);

    // Extract date from [Exif SubIFD]
    if (exifSubIFDDirectory != null) {
      if (exifSubIFDDirectory.containsTag(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL)) {
        Date dto = exifSubIFDDirectory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
        if (dto != null) {
          bim.setDateTimeOriginalRead(true);
          bim.setDateTimeOriginal(dto.getTime());
        }
      }
    }

    // Extract date from [Exif IFD0] - if needed
    if (!bim.isDateTimeOriginalRead()) {
      if (exifIFD0Directory != null) {
        if (exifIFD0Directory.containsTag(ExifIFD0Directory.TAG_DATETIME)) {
          Date dto = exifIFD0Directory.getDate(ExifIFD0Directory.TAG_DATETIME);
          if (dto != null) {
            bim.setDateTimeOriginalRead(true);
            bim.setDateTimeOriginal(dto.getTime());
          }
        }
      }
    }

    // Extract width and height from [JPEG]
    if (jpegDirectory != null) {
      if (jpegDirectory.containsTag(JpegDirectory.TAG_IMAGE_WIDTH)) {
        try {
          bim.setWidth(jpegDirectory.getInt(JpegDirectory.TAG_IMAGE_WIDTH));
          bim.setWidthRead(true);
        } catch (MetadataException e) {
          //Do not log
        }
      }
      if (jpegDirectory.containsTag(JpegDirectory.TAG_IMAGE_HEIGHT)) {
        try {
          bim.setHeight(jpegDirectory.getInt(JpegDirectory.TAG_IMAGE_HEIGHT));
          bim.setHeightRead(true);
        } catch (MetadataException e) {
          //Do not log
        }
      }
    }

    // Extract width from [Exif IFD0] - if needed
    if (!bim.isWidthRead()) {
      if (exifIFD0Directory != null) {
        if (exifIFD0Directory.containsTag(ExifIFD0Directory.TAG_IMAGE_WIDTH)) {
          try {
            bim.setWidth(exifIFD0Directory.getInt(ExifIFD0Directory.TAG_IMAGE_WIDTH));
            bim.setWidthRead(true);
          } catch (MetadataException ex) {
            //Do not log
          }
        }
      }
    }

    // Extract height from [Exif IFD0] - if needed
    if (!bim.isHeightRead()) {
      if (exifIFD0Directory != null) {
        if (exifIFD0Directory.containsTag(ExifIFD0Directory.TAG_IMAGE_HEIGHT)) {
          try {
            bim.setHeight(exifIFD0Directory.getInt(ExifIFD0Directory.TAG_IMAGE_HEIGHT));
            bim.setHeightRead(true);
          } catch (MetadataException ex) {
            //Do not log
          }
        }
      }
    }

    // Extract orientation from [Exif IFD0]
    if (exifIFD0Directory != null) {
      if (exifIFD0Directory.containsTag(ExifIFD0Directory.TAG_ORIENTATION)) {
        try {
          bim.setOrientation(exifIFD0Directory.getInt(ExifIFD0Directory.TAG_ORIENTATION));
          bim.setOrientationRead(true);
        } catch (MetadataException ex) {
          //Do not log
        }
      }
    }

    // Handle the thumbnail

    ExifThumbnailDirectory thumbDir = metadata.getFirstDirectoryOfType(ExifThumbnailDirectory.class);
    if (thumbDir != null) {
      // Check thumbnail data
      if (thumbDir.hasThumbnailData()) {
        thm.setExifThumbReadable(true);
      }
      // Extract orientation
      if (thumbDir.containsTag(ExifThumbnailDirectory.TAG_ORIENTATION)) {
        try {
          thm.setOrientation(thumbDir.getInt(ExifThumbnailDirectory.TAG_ORIENTATION));
          thm.setOrientationRead(true);
        } catch (MetadataException ex) {
          //Do not log
        }
      }
      // Extract width
      if (thumbDir.containsTag(ExifThumbnailDirectory.TAG_IMAGE_WIDTH)) {
        try {
          thm.setWidth(thumbDir.getLong(ExifThumbnailDirectory.TAG_IMAGE_WIDTH));
          thm.setWidthRead(true);
        } catch (MetadataException ex) {
          //Do not log
        }
      }
      // Extract height
      if (thumbDir.containsTag(ExifThumbnailDirectory.TAG_IMAGE_HEIGHT)) {
        try {
          thm.setHeight(thumbDir.getLong(ExifThumbnailDirectory.TAG_IMAGE_HEIGHT));
          thm.setHeightRead(true);
        } catch (MetadataException ex) {
          //Do not log
        }
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
