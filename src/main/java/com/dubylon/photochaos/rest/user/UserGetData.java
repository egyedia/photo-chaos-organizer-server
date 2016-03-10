package com.dubylon.photochaos.rest.user;

import com.dubylon.photochaos.model.db.User;
import com.dubylon.photochaos.rest.PCHandlerResponseData;

import java.util.List;

public class UserGetData extends PCHandlerResponseData {

  private User user;

  public UserGetData() {
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
