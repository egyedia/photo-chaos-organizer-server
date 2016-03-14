package com.dubylon.photochaos.task.copytodatedfolder;

import com.dubylon.photochaos.PhotoChaosOrganizerLauncher;
import com.dubylon.photochaos.app.CopyDatedFolderTaskConfig;
import com.dubylon.photochaos.app.PhotoChaosOrganizerApplication;
import com.dubylon.photochaos.app.TaskConfig;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CopyFilesToFoldersByCaptureDateFromFileNameTask {

  private String sourceFolder;
  private String destinationFolder;
  private String creatorDeviceSuffix;
  private String targetFolderNameFormatter;
  private String fullDateTimePatternString;
  private String justDatePatternString;
  private Pattern fullDateTimePattern;
  private Pattern justDatePattern;

  private CopyToDatedFolderGetData response;
  private boolean performOperations;

  public CopyFilesToFoldersByCaptureDateFromFileNameTask(CopyToDatedFolderGetData response, boolean performOperations) {

    CopyDatedFolderTaskConfig copyDatedFolder = (CopyDatedFolderTaskConfig) PhotoChaosOrganizerApplication
        .getAppConfig().getTasks().get("copyDatedFolder");

    this.response = response;
    this.performOperations = performOperations;
    this.sourceFolder = copyDatedFolder.getSourceFolder();
    this.destinationFolder = copyDatedFolder.getDestinationFolder();
    this.creatorDeviceSuffix = copyDatedFolder.getDestinationFolderSuffix();
    this.targetFolderNameFormatter = "%1$04d%2$02d%3$02d%4$s";
    this.fullDateTimePatternString = "([^\\d]*)([\\d]{2,4})([^\\d]*)([\\d]{1,2})([^\\d]*)([\\d]{1,2})" +
        "([^\\d]*)([\\d]{1,2})([^\\d]*)([\\d]{1,2})([^\\d]*)([\\d]{1,2})([^\\d]*)";
    this.justDatePatternString = "([^\\d]*)([\\d]{2,4})([^\\d]*)([\\d]{1,2})([^\\d]*)([\\d]{1,2})([^\\d]*)";
    this.fullDateTimePattern = Pattern.compile(fullDateTimePatternString);
    this.justDatePattern = Pattern.compile(justDatePatternString);
  }

  public void execute() {

    response.setSourcePath(sourceFolder);
    response.setDestinationPath(destinationFolder);
    response.setCopiedCount(0);
    response.setSkippedCount(0);

    File destinationFolderFile = new File(destinationFolder);
    response.setDestinationOk(destinationFolderFile.exists() && destinationFolderFile.isDirectory());

    File sourceFolderFile = new File(sourceFolder);
    response.setSourceOk(sourceFolderFile.exists() && sourceFolderFile.isDirectory());

    File[] listOfFiles = sourceFolderFile.listFiles();

    for (int i = 0; i < listOfFiles.length; i++) {
      if (listOfFiles[i].isFile()) {
        String name = listOfFiles[i].getName();
        String fileName = FilenameUtils.getBaseName(name);
        CopyFileOperation cfo = new CopyFileOperation();
        response.getOperations().add(cfo);
        cfo.setFileName(fileName);
        DateTimeBean dateTime = extractDateAndTime(fileName);
        String targetDateFolderName = null;
        if (dateTime == null) {
          dateTime = extractDate(fileName);
        }
        if (dateTime != null) {
          targetDateFolderName = String.format(targetFolderNameFormatter, dateTime.getYear(), dateTime.getMonth(),
              dateTime.getDay(), creatorDeviceSuffix);
          String folderToCopy = destinationFolder + targetDateFolderName;
          cfo.setDestinationFolderName(targetDateFolderName);
          File folderToCopyFile = new File(folderToCopy);
          boolean targetDirExists = false;
          if (folderToCopyFile.exists()) {
            targetDirExists = true;
          } else {
            boolean retVal;
            if (performOperations) {
              retVal = folderToCopyFile.mkdirs();
            } else {
              retVal = true;
            }
            if (retVal) {
              targetDirExists = true;
            }
          }

          if (targetDirExists) {
            if (performOperations) {
              String fp = sourceFolder + name;
              File src = new File(fp);
              try {
                FileUtils.copyFileToDirectory(src, folderToCopyFile);
                cfo.setCopied(true);
                response.setCopiedCount(response.getCopiedCount() + 1);
              } catch (IOException ex) {
                ex.printStackTrace();
                response.setSkippedCount(response.getSkippedCount() + 1);
                cfo.setReason("ERROR_WHILE_COPYING");
                cfo.setCopied(false);
              }
            } else {
              cfo.setCopied(true);
              response.setCopiedCount(response.getCopiedCount() + 1);
            }
          } else {
            cfo.setReason("TARGET_DIR_DOES_NOT_EXIST");
            cfo.setCopied(false);
            response.setSkippedCount(response.getSkippedCount() + 1);
          }

        }
      }
    }

  }

  private DateTimeBean extractDateAndTime(String fileName) {
    DateTimeBean dtb = null;
    Matcher m = fullDateTimePattern.matcher(fileName);
    if (m.find()) {
      dtb = new DateTimeBean();
      fillDateTimeBean(dtb, m);
      //System.out.println("datetime:" + dtb);
    }
    return dtb;
  }

  private DateTimeBean extractDate(String fileName) {
    DateTimeBean dtb = null;
    Matcher m = justDatePattern.matcher(fileName);
    if (m.find()) {
      dtb = new DateTimeBean();
      fillDate(dtb, m);
      //System.out.println("datetime:" + dtb);
    }
    return dtb;
  }

  private static void fillDateTimeBean(DateTimeBean dtb, Matcher m) {
    dtb.setYear(getSafeIntValue(m.group(2)));
    dtb.setMonth(getSafeIntValue(m.group(4)));
    dtb.setDay(getSafeIntValue(m.group(6)));
    dtb.setHour(getSafeIntValue(m.group(8)));
    dtb.setMinute(getSafeIntValue(m.group(10)));
    dtb.setSecond(getSafeIntValue(m.group(12)));
  }

  private static void fillDate(DateTimeBean dtb, Matcher m) {
    dtb.setYear(getSafeIntValue(m.group(2)));
    dtb.setMonth(getSafeIntValue(m.group(4)));
    dtb.setDay(getSafeIntValue(m.group(6)));
  }

  private static int getSafeIntValue(String s) {
    try {
      return Integer.parseInt(s);
    } catch (NumberFormatException ex) {
      return 0;
    }
  }
}