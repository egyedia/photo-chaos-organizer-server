package com.dubylon.photochaos.app;

import com.dubylon.photochaos.Defaults;
import com.dubylon.photochaos.resource.ClasspathResourceHandler;
import com.dubylon.photochaos.rest.clientsettings.PcoClientSettingsServlet;
import com.dubylon.photochaos.rest.commandclonetask.CommandCloneTaskServlet;
import com.dubylon.photochaos.rest.control.AppControlPlayVideoServlet;
import com.dubylon.photochaos.rest.control.AppControlShutdownServlet;
import com.dubylon.photochaos.rest.favorite.FilesystemFavoritesServlet;
import com.dubylon.photochaos.rest.frontendsettings.PcoFrontendSettingsServlet;
import com.dubylon.photochaos.rest.fsfolder.FilesystemPathRenameServlet;
import com.dubylon.photochaos.rest.fspath.FilesystemPathContentsServlet;
import com.dubylon.photochaos.rest.fsraw.FilesystemRawServlet;
import com.dubylon.photochaos.rest.fsroot.FilesystemRootsServlet;
import com.dubylon.photochaos.rest.range.FilesystemRangeServlet;
import com.dubylon.photochaos.rest.task.TaskServlet;
import com.dubylon.photochaos.rest.tasks.TasksServlet;
import com.dubylon.photochaos.rest.taskstatus.TaskStatusServlet;
import com.dubylon.photochaos.rest.tasktemplate.TaskTemplateServlet;
import com.dubylon.photochaos.rest.tasktemplates.TaskTemplatesServlet;
import com.dubylon.photochaos.rest.thumbdata.FilesystemMetaThumbnailDataServlet;
import com.dubylon.photochaos.rest.thumbmeta.FilesystemMetaThumbnailMetaServlet;
import com.dubylon.photochaos.rest.thumbonthefly.FilesystemOnTheFlyThumbnailDataServlet;
import com.dubylon.photochaos.rest.user.UserServlet;
import com.dubylon.photochaos.rest.users.UsersServlet;
import com.dubylon.photochaos.servlet.RemainderServlet;
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
import java.sql.SQLException;

public class PhotoChaosOrganizerApplication {

  private static AppConfig appConfig;
  private static Server jettyServer;
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
    servletHandler.addServletWithMapping(FilesystemOnTheFlyThumbnailDataServlet.class, "/filesystem-onthefly-thumbnail-data/*");
    servletHandler.addServletWithMapping(FilesystemRawServlet.class, "/filesystem-raw/*");
    servletHandler.addServletWithMapping(FilesystemFavoritesServlet.class, "/filesystem-favorites/*");
    servletHandler.addServletWithMapping(UsersServlet.class, "/users");
    servletHandler.addServletWithMapping(UserServlet.class, "/users/*");
    servletHandler.addServletWithMapping(TaskTemplatesServlet.class, "/task-templates");
    servletHandler.addServletWithMapping(TaskTemplateServlet.class, "/task-templates/*");
    servletHandler.addServletWithMapping(TasksServlet.class, "/tasks");
    servletHandler.addServletWithMapping(TaskServlet.class, "/tasks/*");
    servletHandler.addServletWithMapping(CommandCloneTaskServlet.class, "/command-clone-task/*");
    servletHandler.addServletWithMapping(TaskStatusServlet.class, "/task-status/*");
    servletHandler.addServletWithMapping(AppControlShutdownServlet.class, "/app-control-shutdown");
    servletHandler.addServletWithMapping(AppControlPlayVideoServlet.class, "/app-control-play-video/*");
    servletHandler.addServletWithMapping(FilesystemPathRenameServlet.class, "/filesystem-path/*");
    servletHandler.addServletWithMapping(FilesystemRangeServlet.class, "/filesystem-range/*");
    servletHandler.addServletWithMapping(PcoClientSettingsServlet.class, "/pco-client-settings-dynamic");
    servletHandler.addServletWithMapping(PcoFrontendSettingsServlet.class, "/pco-frontend-settings");


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
    if (appConfig.getMain().isOpenBrowser()) {
      System.out.println("Opening default browser for URI: " + uri);
      openBrowser(uri);
    } else {
      System.out.println("To access the application, please open your browser, and navigate to: " + uri);
    }
  }

  public static Server getJettyServer() {
    return jettyServer;
  }

  public void run() {

    // Read config
    ConfigReader cr = new ConfigReader();
    appConfig = cr.readConfig();
    Defaults.initFrom(appConfig.getFileTypes());

    // Find an open ports
    int defaultPort = appConfig.getMain().getDefaultPort();
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
    System.out.println("Application stopped. You can safely close this terminal window.");
    System.exit(0);
  }

}
