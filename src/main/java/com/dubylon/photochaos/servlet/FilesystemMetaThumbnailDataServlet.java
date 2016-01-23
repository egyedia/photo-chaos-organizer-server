package com.dubylon.photochaos.servlet;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.ExifThumbnailDirectory;
import com.dubylon.photochaos.util.PhotoChaosUtil;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.NoSuchFileException;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;

public class FilesystemMetaThumbnailDataServlet extends AbstractPhotoChaosServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    // Get path from request and normalize
    String path = request.getPathInfo();
    if (path != null && path.indexOf("/") == 0) {
      path = path.substring(1);
    }
    if (path == null || path.length() == 0) {
      error(response, "MISSING_PATH", "Path should be passed as Base64 encoded string.");
      return;
    }

    // Decode
    String decodedPath = StringUtils.newStringUtf8(Base64.decodeBase64(path));

    // Check path existence
    Path requestedPath = null;
    try {
      requestedPath = Paths.get(decodedPath);
    } catch (InvalidPathException ex) {
      error(response, "INVALID_PATH", ex.toString());
      return;
    }
    if (requestedPath == null || requestedPath.toString().length() == 0) {
      error(response, "ERRONOUS_PATH", "Invalid path requested:" + path);
      return;
    }

    // Code above this line is copied from PathContents servlet
    // TODO: needs refactoring
    File imageFile = requestedPath.toFile();
    if (!imageFile.exists()) {
      notfound(response, "NO_SUCH_FILE", null);
      return;
    }
    if (!imageFile.isFile()) {
      error(response, "NOT_FILE", null);
      return;
    }
    System.out.println("requestedPath:" + requestedPath);

    Metadata metadata = null;
    try {
      metadata = ImageMetadataReader.readMetadata(imageFile);
    } catch (ImageProcessingException ex) {
      error(response, "IMAGE_PROCESSING_EXCEPTION", ex.toString());
      return;
    } catch (IOException ex) {
      error(response, "IO_EXCEPTION", ex.toString());
      return;
    }

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
      thumbnailData = thumbDir.getThumbnailData();
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
        error(response, "METADATA_EXCEPTION", ex.toString());
        return;
      }
    }

    Map<String, String> headers = new HashMap<>();
    headers.put("PCO-height", String.valueOf(h));
    headers.put("PCO-width", String.valueOf(w));
    headers.put("PCO-orientation", String.valueOf(o));
    headers.put("PCO-dateTimeOriginal", String.valueOf(dto == null ? "" : String.valueOf(dto.getTime())));

    ok(response, "image/jpg", headers, thumbnailData);

  }
}
