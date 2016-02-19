package com.dubylon.photochaos.rest.favorite;

import com.dubylon.photochaos.model.db.FavoritePath;
import com.dubylon.photochaos.rest.PCHandlerResponseData;

public class FilesystemFavoritesCreateData extends PCHandlerResponseData {

  private FavoritePath createdObject;
  private Long id;

  public FilesystemFavoritesCreateData() {
  }

  public FavoritePath getCreatedObject() {
    return createdObject;
  }

  public void setCreatedObject(FavoritePath createdObject) {
    this.createdObject = createdObject;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
