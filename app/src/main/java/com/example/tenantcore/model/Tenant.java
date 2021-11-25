package com.example.tenantcore.model;

import java.util.ArrayList;
import java.util.List;

public class Tenant {

  private String firstName;
  private String lastName;
  private List<Request> requests;

  public Tenant(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.requests = new ArrayList<Request>();
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public List<Request> getRequests() {
    return requests;
  }

  public void addRequest(Request newRequest) {
    this.requests.add(newRequest);
  }

}
