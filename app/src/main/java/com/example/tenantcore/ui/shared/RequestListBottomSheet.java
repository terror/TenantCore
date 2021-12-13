package com.example.tenantcore.ui.shared;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.tenantcore.databinding.BottomSheetRequestInfoBinding;
import com.example.tenantcore.db.DatabaseException;
import com.example.tenantcore.model.Priority;
import com.example.tenantcore.model.Request;
import com.example.tenantcore.model.Status;
import com.example.tenantcore.ui.TenantCoreActivity;
import com.example.tenantcore.viewmodel.TenantCoreViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
  private final TenantCoreViewModel viewModel;    // The view model

  /**
   * Creates a Bottom Sheet Dialog for the given request
   * @param activity the activity where this bottom sheet will be displayed
   * @param request the request to show details about
   * @param showLandlordView whether the landlord functionality should be enabled
   */
  public RequestListBottomSheet(@NonNull TenantCoreActivity activity, Request request, boolean showLandlordView) {
    super(activity);

    // Set fields
    this.request = request;
    this.showLandlordView = showLandlordView;
    this.viewModel = activity.getTenantViewModel();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = BottomSheetRequestInfoBinding.inflate(getLayoutInflater());
    final DateFormat DATE_FORMAT = new SimpleDateFormat("EEE MMMM dd, yyyy", Locale.US);

    // Setting text and color
    binding.requestSheetTitleTextView.setText(request.getTitle());
    binding.requestSheetDescriptionTextView.setText(request.getDescription());
    binding.requestSheetDueDateTextView.setText(DATE_FORMAT.format(request.getDueDate()));
    binding.requestSheetPriorityTextView.setText(request.getPriority().name());
    int priorityColor = Color.parseColor(getPriorityColor(request.getPriority()));
    binding.requestSheetPriorityTextView.setTextColor(priorityColor);
    binding.requestSheetPriorityImageView.setColorFilter(priorityColor);
    setStatusInfo();

    // Make landlord function visible if signed in as landlord
    int landlordViewVisibility = showLandlordView ? View.VISIBLE : View.GONE;
    binding.requestSheetApproveBtnImageButton.setVisibility(landlordViewVisibility);
    binding.requestSheetRefuseBtnImageButton.setVisibility(landlordViewVisibility);
    binding.approveTextTextView.setVisibility(landlordViewVisibility);
    binding.refuseTextTextView.setVisibility(landlordViewVisibility);

    if(showLandlordView){
      /*
       * Clicking the "APPROVE" btn changes request status to ACCEPTED.
       * It also updates the request in the db and dismisses the bottom sheet
       */
      binding.requestSheetApproveBtnImageButton.setOnClickListener(view -> {
        request.setStatus(Status.ACCEPTED);
        onStatusUpdate();
      });

      /*
       * Clicking the "REFUSE" btn changes request status to REFUSED.
       * It also updates the request in the db and dismisses the bottom sheet
       */
      binding.requestSheetRefuseBtnImageButton.setOnClickListener(view -> {
        request.setStatus(Status.REFUSED);
        onStatusUpdate();
      });
    }

    setContentView(binding.getRoot());
  }

  /**
   * Updates the request status in the db & dismisses the bottom sheet.
   * Also updates the the status info in the bottom sheet
   */
  private void onStatusUpdate(){
    setStatusInfo();
    try {
      viewModel.updateTenantRequest(request);
    } catch (DatabaseException e) {
      e.printStackTrace();
    }
    dismiss();
  }

  /**
   * Updates the status info in the bottom sheet
   */
  private void setStatusInfo(){
    binding.requestSheetStatusTextView.setText(request.getStatus().name());
    binding.requestSheetStatusTextView.setTextColor(Color.parseColor(getStatusTextColor(request.getStatus())));
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
      case ACCEPTED: return Request.Color.APPROVED_REQUEST;
      default: return Request.Color.PENDING_REQUEST;
    }
  }

}
