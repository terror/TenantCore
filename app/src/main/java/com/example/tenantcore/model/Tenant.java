package com.example.tenantcore.model;

public class Tenant extends User {

  private Long landlordId;

  public Long getLandlordId() {
    return landlordId;
  }

  public Tenant setLandlordId(Long landlordId) {
    this.landlordId = landlordId;
    return this;
  }
}
