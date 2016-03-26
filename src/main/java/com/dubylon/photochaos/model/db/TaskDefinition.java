package com.dubylon.photochaos.model.db;

import com.dubylon.photochaos.util.JSONUserType;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Map;

@Entity
@Table()
@TypeDefs({@TypeDef(name = "JSONUserType", typeClass = JSONUserType.class)})
public class TaskDefinition {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @Fetch(FetchMode.JOIN)
  @JoinColumn(name = "owner_user_id",
      referencedColumnName = "id",
      unique = false,
      nullable = true,
      insertable = true,
      updatable = true
  )
  private User owner;

  private String name;

  @Type(type = "JSONUserType", parameters = {@Parameter(name = "classType", value = "java.util.Map")})
  private Map<String, String> parameters;

  private String className;

  public TaskDefinition() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public User getOwner() {
    return owner;
  }

  public void setOwner(User owner) {
    this.owner = owner;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Map<String, String> getParameters() {
    return parameters;
  }

  public void setParameters(Map<String, String> parameters) {
    this.parameters = parameters;
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

}
