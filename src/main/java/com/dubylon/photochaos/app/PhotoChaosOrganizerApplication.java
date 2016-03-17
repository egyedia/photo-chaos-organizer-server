package com.dubylon.photochaos.app;

import com.dubylon.photochaos.resource.ClasspathResourceHandler;
import com.dubylon.photochaos.rest.config.ConfigServlet;
import com.dubylon.photochaos.rest.favorite.FilesystemFavoritesServlet;
import com.dubylon.photochaos.rest.fspath.FilesystemPathContentsServlet;
import com.dubylon.photochaos.rest.fsraw.FilesystemRawServlet;
import com.dubylon.photochaos.rest.fsroot.FilesystemRootsServlet;
import com.dubylon.photochaos.rest.tasktemplate.TaskTemplatesServlet;
import com.dubylon.photochaos.rest.thumbdata.FilesystemMetaThumbnailDataServlet;
import com.dubylon.photochaos.rest.thumbmeta.FilesystemMetaThumbnailMetaServlet;
import com.dubylon.photochaos.rest.user.UserServlet;
import com.dubylon.photochaos.rest.users.UsersServlet;
import com.dubylon.photochaos.servlet.RemainderServlet;
import com.dubylon.photochaos.task.copytodatedfolder.CopyToDatedFolderServlet;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletHandler;

import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

public class PhotoChaosOrganizerApplication {

  private Path basePath;
  private static AppConfig appConfig;
  private Server jettyServer;
  private org.h2.tools.Server h2Server;

  public static AppConfig getAppConfig() {
    return appConfig;
  }

  private void openBrowser(URI uri) {
    Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
      try {
        desktop.browse(uri);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private void readConfig() {
    try {
      Path jarPath = Paths.get(PhotoChaosOrganizerApplication.class.getProtectionDomain().getCodeSource().getLocation
          ().toURI());
      basePath = jarPath.getParent();
      Path configFilePath = basePath.resolve("pco.config.json");
      ObjectMapper mapper = new ObjectMapper();
      mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
      byte[] configBytes = Files.readAllBytes(configFilePath);
      appConfig = mapper.readValue(configBytes, AppConfig.class);
    } catch (URISyntaxException e) {
      e.printStackTrace();
    } catch (JsonMappingException e) {
      e.printStackTrace();
    } catch (JsonParseException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private boolean isPortAvailable(int port) {
    ServerSocket ss = null;
    try {
      ss = new ServerSocket(port);
      ss.setReuseAddress(true);
      return true;
    } catch (IOException e) {
      //it is ok, maybe it is occupied
    } finally {
      if (ss != null) {
        try {
          ss.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return false;
  }

  private void buildJetty(int port) {
    jettyServer = new Server(port);
    jettyServer.setStopAtShutdown(true);

    // create the handlers
    RequestLogHandler requestLog = new RequestLogHandler();

    // configure request logging
    /*File requestLogFile = null;
    try {
      Path logFolder = basePath.resolve("log");
      File logFolderFile = logFolder.toFile();
      logFolderFile.mkdirs();
      requestLogFile = File.createTempFile("pco-", ".log", logFolderFile);
    } catch (IOException e) {
      e.printStackTrace();
    }
    NCSARequestLog ncsaLog = new NCSARequestLog(requestLogFile.getAbsolutePath());
    requestLog.setRequestLog(ncsaLog);
    */

    ResourceHandler resourceHandler = new ClasspathResourceHandler();

    ServletHandler servletHandler = new ServletHandler();
    servletHandler.addServletWithMapping(FilesystemRootsServlet.class, "/filesystem-roots");
    servletHandler.addServletWithMapping(FilesystemPathContentsServlet.class, "/filesystem-path-contents/*");
    servletHandler.addServletWithMapping(FilesystemMetaThumbnailDataServlet.class, "/filesystem-meta-thumbnail-data/*");
    servletHandler.addServletWithMapping(FilesystemMetaThumbnailMetaServlet.class, "/filesystem-meta-thumbnail-meta/*");
    servletHandler.addServletWithMapping(FilesystemRawServlet.class, "/filesystem-raw/*");
    servletHandler.addServletWithMapping(FilesystemFavoritesServlet.class, "/filesystem-favorites/*");
    servletHandler.addServletWithMapping(UsersServlet.class, "/users");
    servletHandler.addServletWithMapping(UserServlet.class, "/users/*");
    servletHandler.addServletWithMapping(ConfigServlet.class, "/config");
    servletHandler.addServletWithMapping(TaskTemplatesServlet.class, "/task-templates");

    servletHandler.addServletWithMapping(CopyToDatedFolderServlet.class, "/tasks/copy-to-dated-folder");

    servletHandler.addServletWithMapping(RemainderServlet.class, "/*");

    // create the handler collections
    HandlerCollection handlers = new HandlerCollection();
    HandlerList list = new HandlerList();

    // link them all together
    list.setHandlers(new Handler[]{resourceHandler, servletHandler});
    handlers.setHandlers(new Handler[]{list, requestLog});

    jettyServer.setHandler(handlers);
  }


  private void startH2Server() {
    try {
      h2Server = org.h2.tools.Server.createTcpServer().start();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private void launchBrowser(int port) {
    URI uri = null;
    try {
      uri = new URI("http://localhost:" + port + "/");
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    if (appConfig.isOpenBrowser()) {
      System.out.println("Opening default browser for URI: " + uri);
      openBrowser(uri);
    } else {
      System.out.println("To access the application, please open your browser, and navigate to: " + uri);
    }
  }

  public void init() {

    // Read config
    readConfig();

    // Find an open ports
    int defaultPort = appConfig.getDefaultPort();
    int MAX_TRIES = 20;
    int currentPort = defaultPort;
    boolean foundOpenPort = false;
    while (!foundOpenPort && currentPort < defaultPort + MAX_TRIES) {
      if (isPortAvailable(currentPort)) {
        foundOpenPort = true;
      } else {
        currentPort++;
      }
    }

    // Show error and exit if no open port is available
    if (!foundOpenPort) {
      System.out.println("Unable to find open port starting from:" + defaultPort + ".");
      System.out.println("Ports were checked up to: " + currentPort);
      System.out.println("Please modify the pco.config.json file, and specify a different defaultPort value!");
      System.exit(1);
    }

    appConfig.setRealPort(currentPort);
    buildJetty(currentPort);

    startH2Server();

    try {
      jettyServer.start();
    } catch (Exception e) {
      e.printStackTrace();
    }

    launchBrowser(currentPort);

    System.out.println("Press CTRL + C to stop the application");

    try {
      jettyServer.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    h2Server.stop();

  }

}
