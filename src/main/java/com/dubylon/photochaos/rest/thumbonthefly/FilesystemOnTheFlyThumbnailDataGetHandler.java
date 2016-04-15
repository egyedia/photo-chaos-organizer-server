package com.dubylon.photochaos.rest.thumbonthefly;

import com.dubylon.photochaos.rest.PCHandlerError;
import com.dubylon.photochaos.rest.generic.AbstractPCHandlerFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class FilesystemOnTheFlyThumbnailDataGetHandler extends AbstractPCHandlerFile {

  @Override
  public FilesystemOnTheFlyThumbnailDataGetData handleRequest(HttpServletRequest request) throws PCHandlerError {
    FilesystemOnTheFlyThumbnailDataGetData response = new FilesystemOnTheFlyThumbnailDataGetData();
    handlePath(request, response);
    handleFile(request, response);
    String width = request.getParameter("width");
    String height = request.getParameter("height");
    int w = 0;
    if (width != null) {
      try {
        w = Integer.parseInt(width);
      } catch (NumberFormatException e) {
      }
    }
    int h = 0;
    if (height != null) {
      try {
        h = Integer.parseInt(height);
      } catch (NumberFormatException e) {
      }
    }
    if (w < 32 || w > 500) {
      w = 200;
    }
    if (h < 32 || h > 500) {
      h = 200;
    }

    try {
      BufferedImage in = ImageIO.read(response.getFile());
      double outputAspect = 1.0 * w / h;
      double inputAspect = 1.0 * in.getWidth() / in.getHeight();
      if (outputAspect < inputAspect) {
        h = (int) (w / inputAspect);
      } else {
        w = (int) (h * inputAspect);
      }
      BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
      Graphics2D g2 = bi.createGraphics();
      g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
      g2.drawImage(in, 0, 0, w, h, null);
      g2.dispose();
      response.setThumbnail(bi);
    } catch (IOException e) {
      e.printStackTrace();
    }

    return response;
  }

}
