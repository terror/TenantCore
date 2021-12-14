package com.example.tenantcore.ui.shared;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.tenantcore.databinding.BottomSheetRequestInfoBinding;
import com.example.tenantcore.db.DatabaseException;
import com.example.tenantcore.model.Priority;
import com.example.tenantcore.model.Request;
import com.example.tenantcore.model.Status;
import com.example.tenantcore.model.Tenant;
import com.example.tenantcore.notification.AlarmManager;
import com.example.tenantcore.ui.TenantCoreActivity;
import com.example.tenantcore.viewmodel.TenantCoreViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
/**
 * {@link BottomSheetDialog} showing details about the given {@link Request}.
 * This Dialog also gives the options to Accept/Refuse the given {@link Request}
 * if showLandlordView is true
 */
public class RequestListBottomSheet extends BottomSheetDialog {
  private final Request request;    // Request to show details
  private final boolean showLandlordView;   // Whether landlord functionality is enabled
  private BottomSheetRequestInfoBinding binding;
  private final TenantCoreViewModel viewModel;  // The view model
  private final TenantCoreActivity activity;

  /**
   * Creates a Bottom Sheet Dialog for the given request
   * @param activity the activity where this bottom sheet will be displayed
   * @param request the request to show details about
   * @param showLandlordView whether the landlord functionality should be enabled
   */
  public RequestListBottomSheet(@NonNull TenantCoreActivity activity, Request request, boolean showLandlordView) {
    super(activity);
    this.activity = activity;
    this.request = request;
    this.showLandlordView = showLandlordView;
    this.viewModel = activity.getTenantViewModel();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = BottomSheetRequestInfoBinding.inflate(getLayoutInflater());
    final DateFormat DATE_FORMAT = new SimpleDateFormat("EEE MMMM dd, yyyy", Locale.US);

    // Set the title
    binding.requestSheetTitleTextView.setText(request.getTitle());

    // Set the description
    binding.requestSheetDescriptionTextView.setText(request.getDescription());

    // Set the due date
    if (request.getDueDate() != null)
      binding.requestSheetDueDateTextView.setText(DATE_FORMAT.format(request.getDueDate()));

    // Set the priority and color
    if (request.getPriority() != null) {
      binding.requestSheetPriorityTextView.setText(request.getPriority().name());
      int priorityColor = Color.parseColor(getPriorityColor(request.getPriority()));
      binding.requestSheetPriorityTextView.setTextColor(priorityColor);
      binding.requestSheetPriorityImageView.setColorFilter(priorityColor);
    }

    setStatusInfo();

    // Make landlord function visible if signed in as landlord
    int landlordViewVisibility = showLandlordView ? View.VISIBLE : View.GONE;
    binding.requestSheetApproveBtnImageButton.setVisibility(landlordViewVisibility);
    binding.requestSheetRefuseBtnImageButton.setVisibility(landlordViewVisibility);
    binding.approveTextTextView.setVisibility(landlordViewVisibility);
    binding.refuseTextTextView.setVisibility(landlordViewVisibility);

    // Clicking on "ADD TO CALENDAR" btn adds that event to the user's calendar
    // This works for both the landlord & the tenant.
    binding.addToCalendarButton.setOnClickListener(view -> addRequestToCalendar());

    if (showLandlordView) {

      /*
        Clicking the "APPROVE" btn changes request status to ACCEPTED.
        It also updates the request in the db and dismisses the bottom sheet
       */
      binding.requestSheetApproveBtnImageButton.setOnClickListener(view -> {
        // Change the request status
        request.setStatus(Status.ACCEPTED);

        // Add a notification for due date, @ 12:00PM
        AlarmManager.set(activity, request);

        // Disabling the "Approve" btn and enabling the "Refuse" btn
        // (Mainly to prevent the notification to be set twice)
        binding.requestSheetRefuseBtnImageButton.setEnabled(true);
        binding.requestSheetApproveBtnImageButton.setEnabled(false);
        onStatusUpdate();
      });

      /*
        Clicking the "REFUSE" btn changes request status to REFUSED.
        It also updates the request in the db and dismisses the bottom sheet
       */
      binding.requestSheetRefuseBtnImageButton.setOnClickListener(view -> {
        // Change the request status
        request.setStatus(Status.REFUSED);

        // Cancel any set alarms
        AlarmManager.cancel(activity, request);

        // Disabling the "Refuse" btn and enabling the "Approve" btn
        binding.requestSheetRefuseBtnImageButton.setEnabled(false);
        binding.requestSheetApproveBtnImageButton.setEnabled(true);
        onStatusUpdate();
      });

      /*
       * Clicking the "MARK AS DONE" button changes the request status to DONE.
       * It also dismisses the bottom sheet and deletes the request in the db
       * since it has been accomplished and storing it is no longer required.
       */
      binding.markDoneButton.setOnClickListener(view -> {
        // Change the request status
        request.setStatus(Status.DONE);

        onStatusUpdate();
      });
    }

    setContentView(binding.getRoot());
  }

  private void addRequestToCalendar(){
    if(request.getDueDate() == null)
      return;

    Tenant tenant = viewModel.getTenant();

    Calendar calendar = Calendar.getInstance();
    calendar.setTime(request.getDueDate());

    Intent intent = new Intent(Intent.ACTION_INSERT)
      .setData(CalendarContract.Events.CONTENT_URI)
      .putExtra(CalendarContract.Events.TITLE, tenant.getName() + " - " + request.getTitle())
      .putExtra(CalendarContract.Events.DESCRIPTION, request.getDescription())
      .putExtra(CalendarContract.Events.EVENT_LOCATION, "At " + tenant.getName() + "'s place")
      .putExtra(CalendarContract.Events.ALL_DAY, true)
      .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, calendar.getTimeInMillis())
      .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, calendar.getTimeInMillis());

    activity.startActivity(intent);

  }

  /**
   * Updates the request status in the db & dismisses the bottom sheet.
   * Also updates the the status info in the bottom sheet
   */
  private void onStatusUpdate(){
    setStatusInfo();
    try {
      if(!request.getStatus().equals(Status.DONE)) {
        viewModel.updateTenantRequest(request);
      }
      else {
        viewModel.remoteTenantRequest(request);
        dismiss();
      }

    } catch (DatabaseException e) {
      e.printStackTrace();
    }
    //dismiss();
  }

  /**
   * Updates the status info in the bottom sheet
   */
  private void setStatusInfo(){
    binding.requestSheetStatusTextView.setText(request.getStatus().name());
    binding.requestSheetStatusTextView.setTextColor(Color.parseColor(getStatusTextColor(request.getStatus())));
    binding.markDoneButton.setVisibility(showLandlordView && request.getStatus().equals(Status.ACCEPTED) ? View.VISIBLE : View.GONE);
  }

  /**
   * Gets the Hex color code to be used for the given Priority.
   * The Hex color code indicates the text color of the priority.
   * @param priority The priority for which to get the Hex text color code
   * @return String containing the Hex color code indicating the given priority's text color
   */
  private String getPriorityColor(Priority priority){
    switch (priority){
      case LOW: return Request.Color.TEXT_LOW_PRIORITY_REQUEST;
      case MEDIUM: return Request.Color.MEDIUM_PRIORITY_REQUEST;
      default: return Request.Color.HIGH_PRIORITY_REQUEST;
    }
  }

  /**
   * Gets the Hex color code to be used for the given Status.
   * The Hex color code indicates the text color of the status.
   *
   * @param status Status for which to get the Hex color code
   * @return String containing the Hex color code indicating the given status' text color
   */
  private String getStatusTextColor(Status status){
    switch (status){
      case REFUSED: return Request.Color.REFUSED_REQUEST;
      case PENDING: return Request.Color.PENDING_REQUEST;
      default: return Request.Color.APPROVED_REQUEST;
    }
  }

}
