package com.dubylon.photochaos.rest.users;

import com.dubylon.photochaos.model.db.User;
import com.dubylon.photochaos.rest.PCHandlerResponseData;

import java.util.List;

public class UsersGetData extends PCHandlerResponseData {

  private List<User> users;

  public UsersGetData() {
  }

  public List<User> getUsers() {
    return users;
  }

  public void setUsers(List<User> users) {
    this.users = users;
  }
}
