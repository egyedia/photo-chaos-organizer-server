package com.dubylon.photochaos.task.copytodatedfolder;

public class DateTimeBean {

  private int year;
  private int month;
  private int day;
  private int hour;
  private int minute;
  private int second;

  public DateTimeBean() {
  }

  public DateTimeBean(int year, int month, int day) {
    this.year = year;
    this.month = month;
    this.day = day;
  }

  public DateTimeBean(int year, int month, int day, int hour, int minute, int second) {
    this.year = year;
    this.month = month;
    this.day = day;
    this.hour = hour;
    this.minute = minute;
    this.second = second;
  }

  public int getDay() {
    return day;
  }

  public void setDay(int day) {
    this.day = day;
  }

  public int getHour() {
    return hour;
  }

  public void setHour(int hour) {
    this.hour = hour;
  }

  public int getMinute() {
    return minute;
  }

  public void setMinute(int minute) {
    this.minute = minute;
  }

  public int getMonth() {
    return month;
  }

  public void setMonth(int month) {
    this.month = month;
  }

  public int getSecond() {
    return second;
  }

  public void setSecond(int second) {
    this.second = second;
  }

  public int getYear() {
    return year;
  }

  public void setYear(int year) {
    this.year = year;
  }

  @Override
  public String toString() {
    return "DateTimeBean{" + "year=" + year + ", month=" + month + ", day=" + day + ", hour=" + hour + ", minute=" +
        minute + ", second=" + second + '}';
  }
}
