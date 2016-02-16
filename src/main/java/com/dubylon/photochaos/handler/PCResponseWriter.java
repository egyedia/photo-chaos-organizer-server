package com.dubylon.photochaos.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.util.ajax.JSON;

public abstract class PCResponseWriter {

  public static void writeError(HttpServletResponse response, PCResponseObject pcResponse) throws IOException {
    response.setContentType("application/json");
    response.setHeader("Access-Control-Allow-Origin", "*");
    response.setStatus(pcResponse.getResponseCode());
    Map<String, Object> rm = new HashMap<>();
    rm.put("errorCode", pcResponse.getErrorCode());
    rm.put("errorDescription", pcResponse.getErrorDescription());
    Exception ex = pcResponse.getException();
    if (ex != null) {
      List<String> stackTrace = new ArrayList<>();
      for (StackTraceElement ste : ex.getStackTrace()) {
        stackTrace.add(ste.toString());
      }
      rm.put("stackTrace", stackTrace);
    }
    response.getWriter().println(JSON.toString(rm));
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

  public static void writeSuccess(HttpServletResponse response, PCResponseObject pcResponse, Object responseObject)
      throws IOException {
    response.setContentType("application/json");
    response.setHeader("Access-Control-Allow-Origin", "*");
    response.setStatus(pcResponse.getResponseCode());
    response.getWriter().println(JSON.toString(responseObject));
  }

  public static void writeSuccess(HttpServletResponse response, PCResponseObject pcResponse) throws IOException {
    response.setContentType("application/json");
    response.setHeader("Access-Control-Allow-Origin", "*");
    response.setStatus(pcResponse.getResponseCode());
  }

}
