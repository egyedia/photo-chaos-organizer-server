package com.dubylon.photochaos.util;

import com.dubylon.photochaos.task.copytodatedfolderbyname.DateTimeBean;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileNameDateUtil {

  private static String fullDateTimePatternString;
  private static String justDatePatternString;
  private static Pattern fullDateTimePattern;
  private static Pattern justDatePattern;

  static {
    fullDateTimePatternString = "([^\\d]*)([\\d]{2,4})([^\\d]*)([\\d]{1,2})([^\\d]*)([\\d]{1,2})" +
        "([^\\d]*)([\\d]{1,2})([^\\d]*)([\\d]{1,2})([^\\d]*)([\\d]{1,2})([^\\d]*)";
    justDatePatternString = "([^\\d]*)([\\d]{2,4})([^\\d]*)([\\d]{1,2})([^\\d]*)([\\d]{1,2})([^\\d]*)";
    fullDateTimePattern = Pattern.compile(fullDateTimePatternString);
    justDatePattern = Pattern.compile(justDatePatternString);
  }

  private FileNameDateUtil() {
  }

  public static DateTimeBean extractDateAndTime(String fileName) {
    DateTimeBean dtb = null;
    Matcher m = fullDateTimePattern.matcher(fileName);
    if (m.find()) {
      dtb = new DateTimeBean();
      fillDateTimeBean(dtb, m);
    }
    return dtb;
  }

  public static DateTimeBean extractDate(String fileName) {
    DateTimeBean dtb = null;
    Matcher m = justDatePattern.matcher(fileName);
    if (m.find()) {
      dtb = new DateTimeBean();
      fillDate(dtb, m);
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
