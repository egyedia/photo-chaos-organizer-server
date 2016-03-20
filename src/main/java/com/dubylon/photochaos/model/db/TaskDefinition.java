package com.dubylon.photochaos.model.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.IOException;
import java.util.Map;

@Entity
@Table()
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

  private String parametersString;

  @Transient
  @JsonIgnore
  private Map<String, String> parametersObj;

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

  public void setParameters(Map<String, String> parametersObj) {
    this.parametersObj = parametersObj;
    try {
      this.parametersString = new ObjectMapper().writeValueAsString(parametersObj);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }

  @JsonProperty("parameters")
  public Map<String, String> getParameters() {
    return this.parametersObj;
  }

  public void setParametersString(String parameters){
    this.parametersString = parameters;
    try {
      this.parametersObj = new ObjectMapper().readValue(parameters, Map.class);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public String getParametersString() {
    return parametersString;
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

}
