package com.example.tenantcore.model;

import com.example.tenantcore.db.Identifiable;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Random;

public class InviteCode implements Identifiable<Long> {

  private static int MIN_CODE = 10000;
  private static int MAX_CODE = 99999;
  public static Date DEFAULT_EXPIRY = Date.from(LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault()).toInstant());

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

  public static int generateCode() {
    return new Random().nextInt(InviteCode.MAX_CODE + 1 - InviteCode.MIN_CODE) + InviteCode.MIN_CODE;
  }
}
