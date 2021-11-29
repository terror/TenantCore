package com.example.tenantcore.model;

import com.example.tenantcore.db.Identifiable;

import java.util.Date;

public class InviteCode implements Identifiable<Long> {

  private Long id;
  private Long landlordId;
  private int code;
  private Date expiry;

  @Override
  public Long getId() {
    return this.id;
  }

  @Override
  public InviteCode setId(Long id) {
    this.id = id;
    return this;
  }

  public Long getLandlordId() {
    return landlordId;
  }

  public InviteCode setLandlordId(Long landlordId) {
    this.landlordId = landlordId;
    return this;
  }

  public int getCode() {
    return this.code;
  }

  public InviteCode setCode(int code) {
    this.code = code;
    return this;
  }

  public Date getExpiry() {
    return expiry;
  }

  public InviteCode setExpiry(Date expiry) {
    this.expiry = expiry;
    return this;
  }
}
