package com.dubylon.photochaos.model.db;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

@Entity
@Table
public class RepoFile {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String name;
  private long size;
  private long creationTime;
  private String nameSizeHash;

  public RepoFile() {
  }

  public RepoFile(String name, long size, long creationTime) {
    this.name = name;
    this.size = size;
    this.creationTime = creationTime;
    updateNameSizeHash();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
    updateNameSizeHash();
  }

  public long getSize() {
    return size;
  }

  public void setSize(long size) {
    this.size = size;
    updateNameSizeHash();
  }

  public long getCreationTime() {
    return creationTime;
  }

  public void setCreationTime(long creationTime) {
    this.creationTime = creationTime;
  }

  public String getNameSizeHash() {
    return nameSizeHash;
  }

  private void updateNameSizeHash() {
    StringBuilder sb = new StringBuilder();
    if (name != null) {
      sb.append(name);
    }
    sb.append(":");
    sb.append(size);
    this.nameSizeHash = DigestUtils.md5Hex(sb.toString());
  }

  public static RepoFile buildFrom(Path namePath, Path parentPath) {
    long size = 0;
    long creationTime = 0;
    File file = parentPath.resolve(namePath).toFile();
    if (file.exists()) {
      size = file.length();
      BasicFileAttributes attr = null;
      try {
        attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        FileTime fileTime = attr.creationTime();
        creationTime = fileTime.toMillis();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return new RepoFile(StringUtils.lowerCase(namePath.toString()), size, creationTime);
  }
}
