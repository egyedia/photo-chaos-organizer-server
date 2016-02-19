package com.dubylon.photochaos.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import com.dubylon.photochaos.rest.PCHandlerError;
import com.dubylon.photochaos.rest.PCHandlerResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class PCResponseWriter {

  private static ObjectMapper mapper = new ObjectMapper();

  public static void writeError(HttpServletResponse response, PCHandlerError err) throws IOException {
    response.setContentType("application/json");
    response.setHeader("Access-Control-Allow-Origin", "*");
    response.setStatus(err.getResponseCode());
    Map<String, Object> rm = new HashMap<>();
    rm.put("errorKey", err.getErrorKey());
    rm.put("errorText", err.getErrorText());
    Throwable t = err.getCause();
    if (t != null) {
      List<String> stackTrace = new ArrayList<>();
      for (StackTraceElement ste : t.getStackTrace()) {
        stackTrace.add(ste.toString());
      }
      rm.put("stackTrace", stackTrace);
    }
    response.getWriter().println(mapper.writeValueAsString(rm));
  }

  public static void writeSuccess(HttpServletResponse response, String contentType, Map<String, String> headers,
                                  byte[] data) throws IOException {
    response.setContentType(contentType);
    if (headers != null) {
      for (String key : headers.keySet()) {
        response.addHeader(key, headers.get(key));
      }
    }
    response.setContentLength(data == null ? 0 : data.length);
    if (data != null) {
      response.getOutputStream().write(data);
    }
  }

  public static void writeSuccess(HttpServletResponse response, PCHandlerResponse pcResponse, Object responseObject)
      throws IOException {
    writeSuccess(response, pcResponse, null, responseObject);
  }

  public static void writeSuccess(HttpServletResponse response, PCHandlerResponse pcResponse, Map<String, String>
      headers, Object responseObject) throws IOException {
    response.setContentType("application/json");
    response.setHeader("Access-Control-Allow-Origin", "*");
    response.setStatus(pcResponse.getResponseCode());
    if (headers != null) {
      for (String key : headers.keySet()) {
        response.addHeader(key, headers.get(key));
      }
    }
    response.getWriter().println(mapper.writeValueAsString(responseObject));
  }

  public static void writeSuccess(HttpServletResponse response, PCResponseObject pcResponse) throws IOException {
    response.setContentType("application/json");
    response.setHeader("Access-Control-Allow-Origin", "*");
    response.setStatus(pcResponse.getResponseCode());
  }

}
