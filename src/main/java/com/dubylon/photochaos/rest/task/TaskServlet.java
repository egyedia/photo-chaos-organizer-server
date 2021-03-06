package com.dubylon.photochaos.rest.task;

import com.dubylon.photochaos.handler.PCResponseWriter;
import com.dubylon.photochaos.rest.PCHandlerError;
import com.dubylon.photochaos.rest.PCHandlerResponse;
import com.dubylon.photochaos.servlet.AbstractPhotoChaosServlet;
import com.dubylon.photochaos.task.PreviewOrRun;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TaskServlet extends AbstractPhotoChaosServlet {

  public static final String PREVIEW = "preview";
  public static final String RUN = "run";

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String action = request.getParameter("action");
    Boolean launchTaskAtAll = false;
    PreviewOrRun previewOrRun = PreviewOrRun.PREVIEW;
    if (PREVIEW.equals(action)) {
      launchTaskAtAll = Boolean.TRUE;
    } else if (RUN.equals(action)) {
      launchTaskAtAll = Boolean.TRUE;
      previewOrRun = PreviewOrRun.RUN;
    }

    if (launchTaskAtAll) {
      TaskPreviewOrRunGetHandler h = new TaskPreviewOrRunGetHandler(previewOrRun);
      try {
        TaskPreviewOrRunGetData pcResponse = h.handleRequest(request);
        PCResponseWriter.writeSuccess(response, pcResponse, pcResponse);
      } catch (PCHandlerError err) {
        PCResponseWriter.writeError(response, err);
      }
    } else {
      TaskGetHandler h = new TaskGetHandler();
      try {
        TaskGetData pcResponse = h.handleRequest(request);
        PCResponseWriter.writeSuccess(response, pcResponse, pcResponse.getTask());
      } catch (PCHandlerError err) {
        PCResponseWriter.writeError(response, err);
      }
    }
  }

  @Override
  protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException,
      IOException {
    TaskDeleteHandler h = new TaskDeleteHandler();
    try {
      TaskDeleteData pcResponse = h.handleRequest(request);
      if (pcResponse.isDeleted()) {
        pcResponse.setResponseCode(PCHandlerResponse.NO_CONTENT);
        PCResponseWriter.writeSuccess(response, pcResponse);
      } else {
        pcResponse.setResponseCode(PCHandlerResponse.ERROR);
        PCResponseWriter.writeSuccess(response, pcResponse, pcResponse);
      }
    } catch (PCHandlerError err) {
      PCResponseWriter.writeError(response, err);
    }
  }

  @Override
  protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    TaskPutHandler h = new TaskPutHandler();
    try {
      TaskPutData pcResponse = h.handleRequest(request);
      Map<String, String> headers = new HashMap<>();
      PCResponseWriter.writeSuccess(response, pcResponse, headers, pcResponse.getUpdatedObject());
    } catch (PCHandlerError err) {
      PCResponseWriter.writeError(response, err);
    }
  }
}
