package com.example.tenantcore.model;

import com.example.tenantcore.db.Identifiable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

public class InviteCode implements Identifiable<Long> {

  private static int MIN_CODE = 10000;
  private static int MAX_CODE = 99999;
  public static Date DEFAULT_EXPIRY = new Date(
    LocalDateTime.now().getYear(),
    LocalDateTime.now().getMonthValue() - 1,
    LocalDateTime.now().plusDays(1).getDayOfMonth(),
    LocalDateTime.now().getHour(),
    LocalDateTime.now().getMinute(),
    LocalDateTime.now().getSecond()
    );


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
