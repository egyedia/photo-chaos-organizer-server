package com.dubylon.photochaos.rest.stream;

import com.dubylon.photochaos.rest.PCHandlerError;
import com.dubylon.photochaos.rest.generic.AbstractPCHandlerMetaThumbnail;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FilesystemStreamGetHandler extends AbstractPCHandlerMetaThumbnail {

  private static final int BUFFER_LENGTH = 1024 * 16;
  private static final long EXPIRE_TIME = 1000 * 60 * 60 * 24;
  private static final Pattern RANGE_PATTERN = Pattern.compile("bytes=(?<start>\\d*)-(?<end>\\d*)");
  private static final int DEFAULT_CHUNK_SIZE = 10 * 1024 * 1024;
  private HttpServletResponse httpServletResponse;

  // http://www.adrianwalker.org/2012/06/html5-video-pseudosteaming-with-java-7.html

  @Override
  public FilesystemStreamGetData handleRequest(HttpServletRequest request) throws PCHandlerError {
    FilesystemStreamGetData response = new FilesystemStreamGetData();
    handlePath(request, response);
    Path video = response.getRequestedPath();

    int length = 0;
    try {
      length = (int) Files.size(video);
      int start = 0;
      int end = start + DEFAULT_CHUNK_SIZE;
      //int end = length - 1;

      String range = request.getHeader("Range");
      Matcher matcher = null;
      if (range != null) {
        matcher = RANGE_PATTERN.matcher(range);
      }

      if (matcher != null && matcher.matches()) {
        String startGroup = matcher.group("start");
        start = startGroup.isEmpty() ? start : Integer.valueOf(startGroup);
        start = start < 0 ? 0 : start;
        end = start + DEFAULT_CHUNK_SIZE;

        String endGroup = matcher.group("end");
        end = endGroup.isEmpty() ? end : Integer.valueOf(endGroup);
        end = end > length - 1 ? length - 1 : end;
      }

      int contentLength = end - start + 1;

      String contentType = Files.probeContentType(video);
      // TODO detect mimetype based on the extension
      if (contentType == null || "".equals(contentType)) {
        contentType = "video/mp4";
      }

      String contentDisposition = String.format("inline;filename=\"%s\"", video.getFileName());

      httpServletResponse.reset();
      httpServletResponse.setBufferSize(BUFFER_LENGTH);
      httpServletResponse.setHeader("Content-Disposition", contentDisposition);
      httpServletResponse.setHeader("Accept-Ranges", "bytes");
      httpServletResponse.setDateHeader("Last-Modified", Files.getLastModifiedTime(video).toMillis());
      httpServletResponse.setDateHeader("Expires", System.currentTimeMillis() + EXPIRE_TIME);
      httpServletResponse.setContentType(contentType);
      httpServletResponse.setHeader("Content-Range", String.format("bytes %s-%s/%s", start, end, length));
      httpServletResponse.setHeader("Content-Length", String.format("%s", contentLength));
      httpServletResponse.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);

      int bytesRead;
      int bytesLeft = contentLength;
      ByteBuffer buffer = ByteBuffer.allocate(BUFFER_LENGTH);

      try (SeekableByteChannel input = Files.newByteChannel(video, StandardOpenOption.READ);
           OutputStream output = httpServletResponse.getOutputStream()) {

        input.position(start);

        while ((bytesRead = input.read(buffer)) != -1 && bytesLeft > 0) {
          buffer.clear();
          try {
            output.write(buffer.array(), 0, bytesLeft < bytesRead ? bytesLeft : bytesRead);
          } catch (IOException e) {
            System.out.println("Execption while writing:" + e.toString());
          }
          bytesLeft -= bytesRead;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return response;
  }

  public void setHttpServletResponse(HttpServletResponse httpServletResponse) {
    this.httpServletResponse = httpServletResponse;
  }
}
