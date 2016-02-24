package com.dubylon.photochaos.rest.favorite;

import com.dubylon.photochaos.model.db.FavoritePath;
import com.dubylon.photochaos.rest.PCHandlerResponseData;

import java.util.List;

public class FilesystemFavoritesGetData extends PCHandlerResponseData {

  private List<FavoritePath> favorites;

  public FilesystemFavoritesGetData() {
  }

  public List<FavoritePath> getFavorites() {
    return favorites;
  }

  public void setFavorites(List<FavoritePath> favorites) {
    this.favorites = favorites;
  }
}
