package com.dubylon.photochaos.rest.users;

import com.dubylon.photochaos.model.db.User;
import com.dubylon.photochaos.rest.PCHandlerResponseData;

public class UsersPostData extends PCHandlerResponseData {

  private User createdObject;
  private Long id;

  public UsersPostData() {
  }

  public User getCreatedObject() {
    return createdObject;
  }

  public void setCreatedObject(User createdObject) {
    this.createdObject = createdObject;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
