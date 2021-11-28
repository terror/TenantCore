package com.example.tenantcore.model;

import com.example.tenantcore.db.Identifiable;

import java.util.Date;

public class Tenant implements Identifiable<Long> {

  private Long id;
  private String username;
  private String name;
  private Date lastLogin;
  private Long landlordId;

  public Long getLandlordId() {
    return landlordId;
  }

  public Tenant setLandlordId(Long landlordId) {
    this.landlordId = landlordId;
    return this;
  }

  public String getUsername() {
    return username;
  }

  public Tenant setUsername(String username) {
    this.username = username.toLowerCase();
    return this;
  }

  public String getName() {
    return name;
  }

  public Tenant setName(String name) {
    this.name = name;
    return this;
  }

  public Date getLastLogin() {
    return lastLogin;
  }

  public Tenant setLastLogin(Date lastLogin) {
    this.lastLogin = lastLogin;
    return this;
  }

  @Override
  public Long getId() {
    return this.id;
  }

  @Override
  public Tenant setId(Long id) {
    this.id = id;
    return this;
  }
}
