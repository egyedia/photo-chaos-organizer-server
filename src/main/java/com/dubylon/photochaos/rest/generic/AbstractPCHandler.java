package com.dubylon.photochaos.rest.generic;

import com.dubylon.photochaos.rest.PhotoChaosHandler;
import com.dubylon.photochaos.rest.PCHandlerError;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public abstract class AbstractPCHandler implements PhotoChaosHandler {

  public final static String PATH_PREFIX = "file://";
  public final static String HEADER_AUTHORIZATION = "Authorization";
  public final static String HEADER_PREFIX_AUTHORIZATION = "pco ";

  protected static long getUserId(HttpServletRequest request) throws PCHandlerError {
    String authHeader = request.getHeader(HEADER_AUTHORIZATION);
    if (authHeader != null) {
      if (authHeader.startsWith(HEADER_PREFIX_AUTHORIZATION)) {
        try {
          return Long.parseLong(authHeader.substring(HEADER_PREFIX_AUTHORIZATION.length()));
        } catch (Exception e) {
          throw new PCHandlerError("AUTH_ERROR_PARSING", "There was an error parsing the userId");
        }
      } else {
        throw new PCHandlerError("AUTH_ERROR_FORMAT", "The format of the Authorization header should be:" +
            HEADER_PREFIX_AUTHORIZATION + "userId");
      }
    }
    throw new PCHandlerError("AUTH_ERROR_MISSING", "The Authorization header is missing");
  }

  protected static String readNonEmptyContent(HttpServletRequest request, String emptyMessage) throws PCHandlerError {
    String content = null;
    try {
      content = IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new PCHandlerError("ERROR_READING_REQUEST_CONTENT", e);
    }
    if (content == null || content.length() == 0) {
      throw new PCHandlerError("MISSING_CONTENT", emptyMessage);
    }
    return content;
  }


  protected long extractIdFromPathInfo(HttpServletRequest request, String errorMessagePrefix) throws PCHandlerError {
    String pathInfo = request.getPathInfo();
    if (pathInfo == null || pathInfo.length() <= 1) {
      throw new PCHandlerError("MISSING_ID", errorMessagePrefix + " should be specified in the path.");
    }
    pathInfo = pathInfo.substring(1);
    long id = 0;
    try {
      id = Long.parseLong(pathInfo);
    } catch (Exception ex) {
      throw new PCHandlerError("INVALID_ID", ex);
    }
    return id;
  }

}
