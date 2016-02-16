package com.dubylon.photochaos.model;

import javax.persistence.*;

@Entity
@Table
public class FavoritePath {

  @Id
  @GeneratedValue
  private Long id;

  private String path;
  
  private String title;
}
