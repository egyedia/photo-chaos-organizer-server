package com.dubylon.photochaos.rest.favorite;

import com.dubylon.photochaos.rest.PCHandlerError;
import com.dubylon.photochaos.rest.favorite.FilesystemFavoritesHandler;
import com.dubylon.photochaos.handler.PCResponseObject;
import com.dubylon.photochaos.handler.PCResponseWriter;
import com.dubylon.photochaos.rest.fsroot.FilesystemRootsData;
import com.dubylon.photochaos.rest.fsroot.FilesystemRootsHandler;
import com.dubylon.photochaos.servlet.AbstractPhotoChaosServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FilesystemFavoritesServlet extends AbstractPhotoChaosServlet {

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    FilesystemFavoritesHandler h = new FilesystemFavoritesHandler();
    try {
      FilesystemFavoritesCreateData pcResponse = h.doPost(request);
      Map<String, String> headers = new HashMap<>();
      String newLocation = request.getRequestURL().append("/").append(pcResponse.getId()).toString();
      headers.put("Location", newLocation);
      PCResponseWriter.writeSuccess(response, pcResponse, headers, pcResponse.getCreatedObject());
    } catch (PCHandlerError err) {
      PCResponseWriter.writeError(response, err);
    }
  }
}
