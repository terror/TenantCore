package com.example.tenantcore.ui.shared;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tenantcore.databinding.ListItemRequestBinding;
import com.example.tenantcore.model.PlaceholderContent;
import com.example.tenantcore.model.Priority;
import com.example.tenantcore.model.Request;
import com.example.tenantcore.ui.TenantCoreActivity;
import com.example.tenantcore.ui.landlord.home.TenantRequestRecyclerViewAdapter;

import java.util.List;


public class RequestListRecyclerViewAdapter extends RecyclerView.Adapter<RequestListRecyclerViewAdapter.ViewHolder> {

  private PlaceholderContent.PlaceholderItem tenant;
  private final RequestListFragment requestListFragment;
  private List<Request> tenantRequests;

  public RequestListRecyclerViewAdapter(RequestListFragment listFragment){
    this.requestListFragment = listFragment;
    TenantCoreActivity activity = (TenantCoreActivity) this.requestListFragment.getActivity();
    this.tenant = activity.getTaskViewModel().getTenant();
    this.tenantRequests = tenant.getRequests();
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
    }

    public void bind(Request request) {
      this.request = request;

      // Set the request due date
      this.binding.requestDueDateTextView.setText(request.getDueDate().toString());

      // Set the request status
      this.binding.requestSatusTextView.setText(request.getStatus().name());

      // Set the request title
      this.binding.requestTitleTextView.setText(request.getTitle());

      // Set the request background color
      this.binding.requestItemConstraintLayout.setBackgroundColor(Color.parseColor(getRequestBackgroundColor()));


    }

    /**
     * Gets the request background color depending on its priority level.
     * @return String containing the Hex color code indicating the background color of the task
     */
    private String getRequestBackgroundColor(){
      return request.getPriority().equals(Priority.HIGH) ? Request.Color.HIGH_PRIORITY_TASK : request.getPriority().equals(Priority.MEDIUM) ? Request.Color.MEDIUM_PRIORITY_TASK : Request.Color.LOW_PRIORITY_TASK;
    }
  }



}


