package com.dubylon.photochaos.app;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class ConfigReader {

  private AppConfig appConfig;
  private Path basePath;

  public ConfigReader() {
    appConfig = new AppConfig();
  }

  public AppConfig readConfig() {
    try {
      Path jarPath = Paths.get(PhotoChaosOrganizerApplication.class.getProtectionDomain().getCodeSource().getLocation
          ().toURI());
      basePath = jarPath.getParent();
      Path configFolder = findConfigFolder("config/", "classes/config");
      System.out.println("Config folder found: " + configFolder);

      try (final Stream<Path> stream = Files.list(configFolder)) {
        stream
            .filter(path -> path.toFile().isFile())
            .forEach(path -> {
              Path fileNamePath = path.getFileName();
              parseConfigFile(fileNamePath, path);
            });
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return appConfig;
  }

  private void parseConfigFile(Path fileName, Path path) {
    System.out.println("Reading config file: " + fileName + " from: " + path);
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    byte[] configBytes = new byte[0];
    try {
      configBytes = Files.readAllBytes(path);
      if ("main.json".equals(fileName.toString())) {
        AppConfigMain mainConfig = mapper.readValue(configBytes, AppConfigMain.class);
        appConfig.setMain(mainConfig);
      } else if ("video.json".equals(fileName.toString())) {
        AppConfigVideo videoConfig = mapper.readValue(configBytes, AppConfigVideo.class);
        appConfig.setVideo(videoConfig);
      } else if ("date-formats.json".equals(fileName.toString())) {
        AppConfigDateFormats dateFormatsConfig = mapper.readValue(configBytes, AppConfigDateFormats.class);
        appConfig.setDateFormats(dateFormatsConfig);
      } else if ("file-types.json".equals(fileName.toString())) {
        AppConfigFileTypes fileTypesConfig = mapper.readValue(configBytes, AppConfigFileTypes.class);
        appConfig.setFileTypes(fileTypesConfig);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private Path findConfigFolder(String... candidates) {
    for (String candidate : candidates) {
      Path candidatePath = basePath.resolve(candidate);
      File candidateFile = candidatePath.toFile();
      if (candidateFile.exists() && candidateFile.isDirectory()) {
        return candidatePath;
      }
    }
    return null;
  }
}
