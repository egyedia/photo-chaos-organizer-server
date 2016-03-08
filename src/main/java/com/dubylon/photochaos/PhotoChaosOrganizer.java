package com.dubylon.photochaos;

import com.dubylon.photochaos.rest.favorite.FilesystemFavoritesServlet;
import com.dubylon.photochaos.rest.fspath.FilesystemPathContentsServlet;
import com.dubylon.photochaos.rest.fsroot.FilesystemRootsServlet;
import com.dubylon.photochaos.rest.thumbdata.FilesystemMetaThumbnailDataServlet;
import com.dubylon.photochaos.rest.thumbmeta.FilesystemMetaThumbnailMetaServlet;
import com.dubylon.photochaos.rest.user.UsersServlet;
import com.dubylon.photochaos.servlet.*;

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
    servletHandler.addServletWithMapping(FilesystemFavoritesServlet.class, "/filesystem-favorites/*");
    servletHandler.addServletWithMapping(UsersServlet.class, "/users/*");

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
