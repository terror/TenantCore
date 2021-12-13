package com.example.tenantcore.ui.shared;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tenantcore.databinding.ListItemRequestBinding;
import com.example.tenantcore.db.DatabaseException;
import com.example.tenantcore.model.Priority;
import com.example.tenantcore.model.Request;
import com.example.tenantcore.model.Status;
import com.example.tenantcore.model.Tenant;
import com.example.tenantcore.ui.TenantCoreActivity;
import com.example.tenantcore.viewmodel.TenantCoreViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * {@link RecyclerView.Adapter} that can display a {@link List<Request>} of {@link Request}.
 */
public class RequestListRecyclerViewAdapter extends RecyclerView.Adapter<RequestListRecyclerViewAdapter.ViewHolder> {
  private final RequestListFragment requestListFragment;    // The fragment where this recycler view is displayed
  private final Tenant tenant;    // The tenant whose requests are being shown
  private List<Request> tenantRequests;   // The requests to show in this recycler view

  // Date format to use for date displaying
  private final DateFormat DATE_FORMAT = new SimpleDateFormat("EEE MMMM dd, yyyy", Locale.US);

  public RequestListRecyclerViewAdapter(RequestListFragment listFragment) {
    // Set fragment
    this.requestListFragment = listFragment;

    // Set tenant
    TenantCoreActivity activity = (TenantCoreActivity) this.requestListFragment.getActivity();
    this.tenant = activity.getTenantViewModel().getTenant();

    // Set requests
    try {
      this.tenantRequests = activity.getTenantViewModel().getRequestsByTenant(this.tenant);
    } catch (DatabaseException e) {
      e.printStackTrace();
    }
  }

  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    ListItemRequestBinding binding = ListItemRequestBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
    return new ViewHolder(binding);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    holder.bind(tenantRequests.get(position));
  }

  @Override
  public int getItemCount() {
    return tenantRequests.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {

    private ListItemRequestBinding binding;
    private Request request;

    public ViewHolder(ListItemRequestBinding binding) {
      super(binding.getRoot());
      this.binding = binding;

      TenantCoreActivity activity = (TenantCoreActivity) requestListFragment.getActivity();
      if (activity != null) {

        /*
         * The on update listener for the view model.
         * This listener sets the tenantRequests using the database
         */
        activity.getTenantViewModel().addOnUpdateListener(item -> {
          try {
            tenantRequests = item.getRequestsByTenant(tenant);
          } catch (DatabaseException e) {
            e.printStackTrace();
          }
        });
      }

      /*
       * On click listener for a request item in the recycler view
       *
       * Clicking on a request bring up the bottom sheet with more info on the request
       */
      this.binding.requestItemConstraintLayout.setOnClickListener(view -> {
        TenantCoreViewModel viewModel = ((TenantCoreActivity) requestListFragment.getActivity()).getTenantViewModel();

        // Check for landlord
        boolean isLandlord = viewModel.findLandlord(viewModel.getSignedInUser()) != null;

        // Layout position will be used to notify change at the right place in the tenantRequests list
        int layoutPosition = getLayoutPosition();

        // Create + show bottom sheet
        RequestListBottomSheet requestInfoDialog = new RequestListBottomSheet((TenantCoreActivity) requestListFragment.getActivity(), tenantRequests.get(layoutPosition), isLandlord);
        requestInfoDialog.show();

        // On dismiss listener only if user is landlord
        // (nothing can be changed in bottom sheet if user is not a landlord)
        if (isLandlord) {
          requestInfoDialog.setOnDismissListener(dialogInterface -> {
            viewModel.notifyChange();
            notifyItemChanged(layoutPosition);
          });
        }
      });
    }

    public void bind(Request request) {
      this.request = request;

      // Set the request due date
      this.binding.requestDueDateTextView.setText(DATE_FORMAT.format(request.getDueDate()));

      // Set the request status
      this.binding.requestSatusTextView.setText(request.getStatus().name());

      // Set the request title
      this.binding.requestTitleTextView.setText(request.getTitle());

      // Set the request status color
      this.binding.requestSatusTextView.setTextColor(Color.parseColor(getStatusTextColor(request.getStatus())));

      // Set the request background color
      this.binding.requestItemConstraintLayout.setBackgroundColor(Color.parseColor(getRequestBackgroundColor()));
    }

    /**
     * Gets the request background color depending on its priority level.
     *
     * @return String containing the Hex color code indicating the background color of the task
     */
    private String getRequestBackgroundColor() {
      return request.getPriority().equals(Priority.HIGH) ? Request.Color.HIGH_PRIORITY_REQUEST : request.getPriority().equals(Priority.MEDIUM) ? Request.Color.MEDIUM_PRIORITY_REQUEST : Request.Color.LOW_PRIORITY_REQUEST;
    }

    /**
     * Gets the Hex color code to be used for the given Status.
     * The Hex color code indicates the text color of the status.
     *
     * @param status Status for which to get the Hex color code
     * @return String containing the Hex color code indicating the given status' text color
     */
    private String getStatusTextColor(Status status) {
      switch (status) {
        case REFUSED:
          return Request.Color.REFUSED_REQUEST;
        case ACCEPTED:
          return Request.Color.APPROVED_REQUEST;
        default:
          return Request.Color.PENDING_REQUEST_DARK;
      }
    }
  }
}


