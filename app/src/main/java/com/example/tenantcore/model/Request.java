package com.example.tenantcore.model;

import java.util.Date;

/**
 * Represents a request made by someone or something.
 */
public class Request {

  private String title;
  private String description;
  private Date dueDate;
  private Status status;
  private Priority priority;

  /**
   * Creates a new request
   *
   * @param title       The title of the request
   * @param description The description of the request
   * @param dueDate     The due date of the request
   */
  public Request(String title, String description, Date dueDate) {
    this.title = title;
    this.description = description;
    this.dueDate = dueDate;
    this.priority = Priority.LOW;
    this.status = Status.PENDING;
  }

  /**
   * Gets request status
   *
   * @return Status of request
   */
  public Status getStatus() {
    return status;
  }

  /**
   * Sets request status
   *
   * @param status Status to set for the request
   */
  public void setStatus(Status status) {
    this.status = status;
  }

  /**
   * Gets request priority
   *
   * @return the request priority
   */
  public Priority getPriority() {
    return priority;
  }

  /**
   * Sets the request priority
   *
   * @param priority Priority to set for the request
   */
  public void setPriority(Priority priority) {
    this.priority = priority;
  }

  /**
   * Gets the request title
   *
   * @return the request title
   */
  public String getTitle() {
    return title;
  }

  /**
   * Sets the title of the request
   *
   * @param title the title to set for the request
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Gets the request description
   *
   * @return thw request description
   */
  public String getDescription() {
    return description;
  }

  /**
   * Sets the description of the request
   *
   * @param description the description to set for the request
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Gets the request due date
   *
   * @return the due date of the request
   */
  public Date getDueDate() {
    return dueDate;
  }

  /**
   * Sets the due date of the request
   *
   * @param dueDate the due date to set for the request
   */
  public void setDueDate(Date dueDate) {
    this.dueDate = dueDate;
  }

  /**
   * static Task Color class containing hex color codes for different task aspects.
   */
  public static class Color {
    public static final String HIGH_PRIORITY_TASK = "#e75480"; // Dark pink
    public static final String MEDIUM_PRIORITY_TASK = "#F28500"; // Tangerine
    public static final String LOW_PRIORITY_TASK = "#90EE90"; // Light green
  }
}
