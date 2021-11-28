package com.example.tenantcore.model;

import com.example.tenantcore.db.Identifiable;

import java.util.Date;
import java.util.Locale;

public class Landlord implements Identifiable<Long> {

  private Long id;
  private String username;
  private String name;
  private Date lastLogin;

  public String getUsername() {
    return username;
  }

  public Landlord setUsername(String username) {
    this.username = username.toLowerCase();
    return this;
  }

  public String getName() {
    return name;
  }

  public Landlord setName(String name) {
    this.name = name;
    return this;
  }

  public Date getLastLogin() {
    return lastLogin;
  }

  public Landlord setLastLogin(Date lastLogin) {
    this.lastLogin = lastLogin;
    return this;
  }

  @Override
  public Long getId() {
    return this.id;
  }

  @Override
  public Landlord setId(Long id) {
    this.id = id;
    return this;
  }
}
