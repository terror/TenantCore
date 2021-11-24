package com.example.tenantcore.ui.landlord.home;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tenantcore.databinding.ListItemTenantRequestBinding;
import com.example.tenantcore.model.PlaceholderContent.PlaceholderItem;
import java.util.List;

public class TenantRequestRecyclerViewAdapter extends RecyclerView.Adapter<TenantRequestRecyclerViewAdapter.ViewHolder> {
  private final List<PlaceholderItem> requests;

  public TenantRequestRecyclerViewAdapter(List<PlaceholderItem> requests) {
    this.requests = requests;
    setHasStableIds(true);
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    ListItemTenantRequestBinding binding = ListItemTenantRequestBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
    return new ViewHolder(binding);
  }

  @Override
  public void onBindViewHolder(final ViewHolder holder, int position) {
    holder.bind(requests.get(position));
  }

  @Override
  public int getItemViewType(int position) {
    return requests.get(position).getId();
  }

  @Override
  public long getItemId(int position) {
    return requests.get(position).getId();
  }

  @Override
  public int getItemCount() {
    return requests.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    private ListItemTenantRequestBinding binding;
    private PlaceholderItem request;

    public ViewHolder(ListItemTenantRequestBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    public void bind(PlaceholderItem request) {
      this.request = request;

      // set the request tenant name
      binding.requestTenantName.setText(request.getContent());

      // set the request description
      binding.requestDescription.setText(request.getDetails());
    }
  }
}
