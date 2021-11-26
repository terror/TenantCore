package com.example.tenantcore.model;

import com.example.tenantcore.db.Identifiable;

import java.util.Date;

public class User implements Identifiable<Long> {

  private Long id;
  private String username;
  private String name;
  private Date lastLogin;

  public String getUsername() {
    return username;
  }

  public User setUsername(String username) {
    this.username = username;
    return this;
  }

  public String getName() {
    return name;
  }

  public User setName(String name) {
    this.name = name;
    return this;
  }

  public Date getLastLogin() {
    return lastLogin;
  }

  public User setLastLogin(Date lastLogin) {
    this.lastLogin = lastLogin;
    return this;
  }

  @Override
  public Long getId() {
    return this.id;
  }

  @Override
  public User setId(Long id) {
    this.id = id;
    return this;
  }
}
