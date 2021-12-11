package com.example.tenantcore.ui.tenant.home;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.example.tenantcore.R;
import com.example.tenantcore.databinding.FragmentTenantHomeBinding;
import com.example.tenantcore.model.Priority;
import com.example.tenantcore.model.Request;
import com.example.tenantcore.model.Tenant;
import com.example.tenantcore.ui.TenantCoreActivity;
import com.example.tenantcore.ui.util.DatePickerDialogFragment;
import com.example.tenantcore.viewmodel.TenantCoreViewModel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TenantHomeFragment extends Fragment {
  public static String TAG_NAME = "tenant_home";
  private FragmentTenantHomeBinding binding;
  private Calendar requestDate;
  private SimpleDateFormat formatter;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    if (getActivity().getSupportFragmentManager().findFragmentByTag(TAG_NAME) == null)
      getActivity().getSupportFragmentManager().beginTransaction().add(this, TAG_NAME).commit();

    binding = FragmentTenantHomeBinding.inflate(inflater, container, false);
    formatter = new SimpleDateFormat("EEEE, MMMM d");
    setListeners();

    return binding.getRoot();
  }

  public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }

  /**
   * Clears the data from all of the request form fields.
   */
  private void resetForm() {
    binding.requestTitleEditText.getText().clear();
    binding.requestDescEditText.getText().clear();
    resetDate();
    resetUrgency();
  }

  /**
   * Clears the request forms urgency field.
   */
  private void resetUrgency() {
    binding.urgencyLowRadioBtn.setChecked(false);
    binding.urgencyMedRadioBtn.setChecked(false);
    binding.urgencyHighRadioBtn.setChecked(false);
  }

  /**
   * Clears the request forms date field.
   */
  private void resetDate() {
    binding.requestDateBtn.setText("");
    requestDate = null;
  }

  /**
   * Opens a date picker to allow for a request date to be selected.
   */
  private void chooseDate() {
    // Create a date picker fragment and pass in the default date.
    DatePickerDialogFragment datePicker = DatePickerDialogFragment.create(
      getDefaultDueDate(), new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int date) {
          setRequestDate(year, month, date);
        }
      });

    datePicker.show(getFragmentManager(), "datePicker");
  }

  /**
   * Sets the request date using the provided year, month, and date.
   * @param year The year to set the request date to.
   * @param month The month to set the request date to.
   * @param date The date to set the request date to.
   */
  private void setRequestDate(int year, int month, int date) {
    if (requestDate == null)
      requestDate = Calendar.getInstance();

    requestDate.set(Calendar.YEAR, year);
    requestDate.set(Calendar.MONTH, month);
    requestDate.set(Calendar.DATE, date);

    validateDate();
  }

  /**
   * Validates the current request date.
   */
  private void validateDate() {
    Calendar today = Calendar.getInstance();

    /*
     Display an alert if the chosen date is invalid (before today).
     Otherwise, set the date button text to request date.
     */
    if (requestDate.before(today)) {
      requestDate = null;

      new AlertDialog.Builder(getContext())
        .setTitle("Invalid Date.")
        .setMessage("Please select a date in the future.")
        .setIcon(android.R.drawable.ic_dialog_alert)
        .setPositiveButton("OK", null)
        .show();
    }
    else
      binding.requestDateBtn.setText(formatter.format(requestDate.getTime()));
  }

  /**
   * Submits a new request using the data provided in the request form.
   */
  private void submitRequest() {
    if (!validRequest())
      return;

    // Create a new request
    Request request = new Request(
      binding.requestTitleEditText.getText().toString(),
      binding.requestDescEditText.getText().toString(),
      requestDate != null ? requestDate.getTime() : null);

    // Set the requests urgency
    if (binding.urgencyLowRadioBtn.isChecked())
      request.setPriority(Priority.LOW);
    else if (binding.urgencyMedRadioBtn.isChecked())
      request.setPriority(Priority.MEDIUM);
    else if (binding.urgencyHighRadioBtn.isChecked())
      request.setPriority(Priority.HIGH);

    // Grab the view model
    TenantCoreActivity activity = (TenantCoreActivity) getActivity();
    TenantCoreViewModel viewModel = activity.getTenantViewModel();

    // Set the tenant id
    Tenant tenant = viewModel.findTenant(viewModel.getSignedInUser());
    request.setTenantId(tenant.getId());

    // Add the request to the database
    activity.getTenantViewModel().addRequest(request);

    // Reset the form
    resetForm();
  }

  /**
   * Validates the request by ensuring that the
   * title and description fields are filled.
   * @return True if the request is valid, false otherwise.
   */
  private boolean validRequest() {
    // If both the request title and description are not empty, return true.
    if (binding.requestTitleEditText.length() != 0 && binding.requestDescEditText.length() != 0)
      return true;

    // Display an alert if the either of the fields are empty.
    new AlertDialog.Builder(getContext())
      .setTitle("Invalid request.")
      .setMessage("A request must have a title and description.")
      .setIcon(android.R.drawable.ic_dialog_alert)
      .setPositiveButton("OK", null)
      .show();

    return false;
  }

  /**
   * Gets the default date for a request.
   * By default this is a week after today's date.
   * @return The default request date.
   */
  private Date getDefaultDueDate() {
    Date defaultDate = new Date();
    defaultDate.setDate(defaultDate.getDate() + 7); // Advance today's date by 7 days

    return defaultDate;
  }

  /**
   * Sets all of the listeners for this fragment.
   */
  private void setListeners() {
    // Navigate to the request list fragment when the view request button is clicked.
    binding.viewRequestsBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // Get a place holder tenant and set it in the view-model.
        TenantCoreViewModel viewModel = ((TenantCoreActivity)getActivity()).getTenantViewModel();
        viewModel.setTenant(viewModel.findTenant(viewModel.getSignedInUser()));

        NavHostFragment.findNavController(TenantHomeFragment.this)
          .navigate(R.id.action_TenantHomeFragment_to_RequestListFragment);
      }
    });

    // Clear all request form fields when the reset button is clicked.
    binding.formResetBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        resetForm();
      }
    });

    // Clear the request form urgency field when the clear urgency button is clicked.
    binding.urgencyClearBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        resetUrgency();
      }
    });

    // Clear the request form date field when the clear date button is clicked.
    binding.dateClearBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        resetDate();
      }
    });

    // Open the date picker fragment when the date button is clicked.
    binding.requestDateBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        chooseDate();
      }
    });

    // Submit the request when the submit button is clicked.
    binding.formSubmitBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        submitRequest();
      }
    });
  }
}
