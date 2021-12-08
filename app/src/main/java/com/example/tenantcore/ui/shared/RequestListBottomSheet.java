package com.example.tenantcore.ui.shared;

import android.content.Context;
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

public class RequestListBottomSheet extends BottomSheetDialog {

  private final Request request;
  private final boolean showLandlordView;
  private BottomSheetRequestInfoBinding binding;
  private final TenantCoreViewModel viewModel;

  public RequestListBottomSheet(@NonNull TenantCoreActivity activity, Request request, boolean showLandlordView) {
    super(activity);
    this.request = request;
    this.showLandlordView = showLandlordView;

    // The view model
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
        Clicking the "APPROVE" btn changes request status to ACCEPTED.
        It also updates the request in the db and dismisses the bottom sheet
       */
      binding.requestSheetApproveBtnImageButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          request.setStatus(Status.ACCEPTED);
          onStatusUpdate();
        }
      });

      /*
        Clicking the "REFUSE" btn changes request status to REFUSED.
        It also updates the request in the db and dismisses the bottom sheet
       */
      binding.requestSheetRefuseBtnImageButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          request.setStatus(Status.REFUSED);
          onStatusUpdate();
        }
      });
    }

    setContentView(binding.getRoot());
  }

  private void onStatusUpdate(){
    setStatusInfo();
    try {
      viewModel.updateTenantRequest(request);
    } catch (DatabaseException e) {
      e.printStackTrace();
    }
    dismiss();
  }

  private void setStatusInfo(){
    binding.requestSheetStatusTextView.setText(request.getStatus().name());
    binding.requestSheetStatusTextView.setTextColor(Color.parseColor(getStatusColor(request.getStatus())));
  }

  private String getPriorityColor(Priority priority){
    switch (priority){
      case LOW: return Request.Color.TEXT_LOW_PRIORITY_REQUEST;
      case MEDIUM: return Request.Color.MEDIUM_PRIORITY_REQUEST;
      default: return Request.Color.HIGH_PRIORITY_REQUEST;
    }
  }

  private String getStatusColor(Status status){
    switch (status){
      case REFUSED: return Request.Color.REFUSED_REQUEST;
      case ACCEPTED: return Request.Color.APPROVED_REQUEST;
      default: return Request.Color.PENDING_REQUEST;
    }
  }

}
