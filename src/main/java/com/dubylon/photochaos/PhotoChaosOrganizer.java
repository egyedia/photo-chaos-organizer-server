package com.dubylon.photochaos;

import com.dubylon.photochaos.servlet.FilesystemMetaThumbnailDataServlet;
import com.dubylon.photochaos.servlet.FilesystemMetaThumbnailMetaServlet;
import com.dubylon.photochaos.servlet.FilesystemPathContentsServlet;
import com.dubylon.photochaos.servlet.FilesystemRootsServlet;
import com.dubylon.photochaos.servlet.RemainderServlet;
import java.io.File;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.NCSARequestLog;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletHandler;

public class PhotoChaosOrganizer {

  public static void main(String[] args) throws Exception {

    Server server = new Server(8080);
    server.setStopAtShutdown(true);

    // create the handlers
    RequestLogHandler requestLog = new RequestLogHandler();

    // configure request logging
    File requestLogFile = File.createTempFile("demo", "log");
    NCSARequestLog ncsaLog = new NCSARequestLog(requestLogFile.getAbsolutePath());
    requestLog.setRequestLog(ncsaLog);

    ResourceHandler resourceHandler = new ResourceHandler();
    resourceHandler.setDirectoriesListed(true);
    resourceHandler.setWelcomeFiles(new String[]{"index.html"});
    resourceHandler.setResourceBase("./src/main/resources/");

    ServletHandler servletHandler = new ServletHandler();
    servletHandler.addServletWithMapping(FilesystemRootsServlet.class, "/filesystem-roots");
    servletHandler.addServletWithMapping(FilesystemPathContentsServlet.class, "/filesystem-path-contents/*");
    servletHandler.addServletWithMapping(FilesystemMetaThumbnailDataServlet.class, "/filesystem-meta-thumbnail-data/*");
    servletHandler.addServletWithMapping(FilesystemMetaThumbnailMetaServlet.class, "/filesystem-meta-thumbnail-meta/*");
    
    servletHandler.addServletWithMapping(RemainderServlet.class, "/*");

    // create the handler collections
    HandlerCollection handlers = new HandlerCollection();
    HandlerList list = new HandlerList();

    // link them all together
    list.setHandlers(new Handler[]{resourceHandler, servletHandler});
    handlers.setHandlers(new Handler[]{list, requestLog});

    server.setHandler(handlers);

    server.start();
    server.join();
  }
}
