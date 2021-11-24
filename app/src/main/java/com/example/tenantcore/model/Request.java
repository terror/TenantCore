package com.example.tenantcore.model;

import java.util.Date;

public class Request {

  private String title;
  private String description;
  private Date dueDate;
  private Status status;
  private Priority priority;

  public Request(String title, String description, Date dueDate){
    this.title = title;
    this.description = description;
    this.dueDate = dueDate;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public Priority getPriority() {
    return priority;
  }

  public void setPriority(Priority priority) {
    this.priority = priority;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Date getDueDate() {
    return dueDate;
  }

  public void setDueDate(Date dueDate) {
    this.dueDate = dueDate;
  }






}
