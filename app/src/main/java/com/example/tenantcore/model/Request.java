package com.example.tenantcore.model;

import android.net.Uri;
import android.os.Bundle;

import com.example.tenantcore.db.Identifiable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * Represents a request made by someone or something.
 */
public class Request implements Identifiable<Long> {
  private Long id;
  private Long tenantId;
  private String title;
  private String description;
  private Date dueDate;
  private Status status;
  private Priority priority;
  private Uri imageUri;

  public Request() {}

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
   * Build a new request from a Bundle
   * @param bundle Bundle that contains the request data
   * @return A newly instantiated `Request` instance
   */
  public static Request fromBundle(Bundle bundle) {
    Request request = new Request();

    request.setId(bundle.getLong("id"));
    request.setTenantId(bundle.getLong("tenantId"));
    request.setTitle(bundle.getString("title"));
    request.setDescription(bundle.getString("description"));
    request.setStatus(Status.values()[bundle.getInt("status")]);
    request.setPriority(Priority.values()[bundle.getInt("priority")]);

    Date ret = null;
    try {
      if (bundle.containsKey("date"))
        ret = new SimpleDateFormat("dd MM yyyy HH:mm:ss").parse(bundle.getString("date"));
    } catch (ParseException e) {
      e.printStackTrace();
    }

    request.setDueDate(ret);
    return request;
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
  public Request setStatus(Status status) {
    this.status = status;
    return this;
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
  public Request setPriority(Priority priority) {
    this.priority = priority;
    return this;
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
  public Request setTitle(String title) {
    this.title = title;
    return this;
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
  public Request setDescription(String description) {
    this.description = description;
    return this;
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
  public Request setDueDate(Date dueDate) {
    this.dueDate = dueDate;
    return this;
  }

  /**
   * Gets the request image uri
   *
   * @return the image uri of the request
   */
  public Uri getImageUri() {
    return imageUri;
  }

  /**
   * Sets the image uri of the request
   *
   * @param imageUri the image uri to set for the request
   */
  public Request setImageUri(Uri imageUri) {
    this.imageUri = imageUri;
    return this;
  }

  @Override
  public Long getId() {
    return this.id;
  }

  @Override
  public Request setId(Long id) {
    this.id = id;
    return this;
  }

  public Long getTenantId() {
    return tenantId;
  }

  public Request setTenantId(Long tenantId) {
    this.tenantId = tenantId;
    return this;
  }

  /**
   * Check if the task is overdue.
   */
  public boolean isOverdue() {
    Calendar curr = Calendar.getInstance();
    curr.setTime(new Date());
    if (getDueDate() != null)
      return curr.getTime().after(getDueDate());
    return false;
  }

  /**
   * Convert a `Request` to a `Bundle`
   * @return A bundle containing important `Request` data
   */
  public Bundle toBundle() {
    Bundle bundle = new Bundle();

    bundle.putLong("id", getId());
    bundle.putLong("tenantId", getTenantId());
    bundle.putString("title", getTitle());
    bundle.putString("description", getDescription());
    bundle.putInt("status", getStatus().ordinal());
    bundle.putInt("priority", getPriority().ordinal());

    if (getDueDate() != null)
      bundle.putString("date", new SimpleDateFormat("dd MM yyyy HH:mm:ss").format(getDueDate()));

    return bundle;
  }

  /**
   * static Task Color class containing hex color codes for different task aspects.
   */
  public static class Color {
    public static final String HIGH_PRIORITY_REQUEST = "#e75480"; // Dark pink
    public static final String MEDIUM_PRIORITY_REQUEST = "#F28500"; // Tangerine
    public static final String LOW_PRIORITY_REQUEST = "#90EE90"; // Light green
    public static final String PENDING_REQUEST_DARK = "#696969"; // Dim gray
    public static final String PENDING_REQUEST = "#a9a9a9"; // Gray
    public static final String APPROVED_REQUEST = "#6aa84f"; // Green
    public static final String REFUSED_REQUEST = "#FF0000"; // Red
    public static final String TEXT_LOW_PRIORITY_REQUEST = "#6aa84f"; // Green
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, description, title, description, dueDate, status, priority);
  }
}
