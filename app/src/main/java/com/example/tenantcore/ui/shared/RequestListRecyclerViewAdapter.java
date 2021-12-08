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
import com.example.tenantcore.viewmodel.ObservableModel;
import com.example.tenantcore.viewmodel.TenantCoreViewModel;
import java.util.List;

public class RequestListRecyclerViewAdapter extends RecyclerView.Adapter<RequestListRecyclerViewAdapter.ViewHolder> {
  private final RequestListFragment requestListFragment;
  private Tenant tenant;
  private List<Request> tenantRequests;

  public RequestListRecyclerViewAdapter(RequestListFragment listFragment) {
    this.requestListFragment = listFragment;
    TenantCoreActivity activity = (TenantCoreActivity) this.requestListFragment.getActivity();
    this.tenant = activity.getTenantViewModel().getTenant();
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

        // Update listener for view model
        activity.getTenantViewModel().addOnUpdateListener(new ObservableModel.OnUpdateListener<TenantCoreViewModel>() {
          @Override
          public void onUpdate(TenantCoreViewModel item) {
            try {
              tenantRequests = item.getRequestsByTenant(tenant);
            } catch (DatabaseException e) {
              e.printStackTrace();
            }
          }
        });
      }

      // clicking on a request item in the recycler view
      this.binding.requestItemConstraintLayout.setOnClickListener(view -> {
        TenantCoreViewModel viewModel = ((TenantCoreActivity) requestListFragment.getActivity()).getTenantViewModel();

        boolean isLandlord = viewModel.findLandlord(viewModel.getSignedInUser()) != null;
        int layoutPosition = getLayoutPosition();
        RequestListBottomSheet requestInfoDialog = new RequestListBottomSheet((TenantCoreActivity) requestListFragment.getActivity(), tenantRequests.get(layoutPosition), isLandlord);
        requestInfoDialog.show();

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
      this.binding.requestDueDateTextView.setText(request.getDueDate().toString());

      // Set the request status
      this.binding.requestSatusTextView.setText(request.getStatus().name());

      // Set the request title
      this.binding.requestTitleTextView.setText(request.getTitle());

      // Set the request status color
      this.binding.requestSatusTextView.setTextColor(Color.parseColor(getStatusColor(request.getStatus())));

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

    private String getStatusColor(Status status) {
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


