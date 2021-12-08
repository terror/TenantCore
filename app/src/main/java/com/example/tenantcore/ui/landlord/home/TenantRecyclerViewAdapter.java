package com.example.tenantcore.ui.landlord.home;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tenantcore.R;
import com.example.tenantcore.databinding.ListItemTenantRequestBinding;
import com.example.tenantcore.model.Tenant;
import com.example.tenantcore.ui.TenantCoreActivity;
import java.util.List;

public class TenantRecyclerViewAdapter extends RecyclerView.Adapter<TenantRecyclerViewAdapter.ViewHolder> {
  private final List<Tenant> tenants;
  private final LandlordHomeFragment landlordHomeFragment;

  public TenantRecyclerViewAdapter(List<Tenant> tenants, LandlordHomeFragment landlordHomeFragment) {
    this.tenants = tenants;
    this.landlordHomeFragment = landlordHomeFragment;
    setHasStableIds(true);
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    ListItemTenantRequestBinding binding = ListItemTenantRequestBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
    return new ViewHolder(binding);
  }

  @Override
  public void onBindViewHolder(final ViewHolder holder, int position) {
    holder.bind(tenants.get(position));
  }

  @Override
  public int getItemViewType(int position) {
    return tenants.get(position).getId().intValue();
  }

  @Override
  public long getItemId(int position) {
    return tenants.get(position).getId();
  }

  @Override
  public int getItemCount() {
    return tenants.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    private ListItemTenantRequestBinding binding;
    private Tenant tenant;

    public ViewHolder(ListItemTenantRequestBinding binding) {
      super(binding.getRoot());
      this.binding = binding;

      binding.listRequestItemLayout.setOnClickListener(view -> {
        // Store the tenant clicked on in the ViewModel
        TenantCoreActivity activity = (TenantCoreActivity) landlordHomeFragment.getActivity();
        activity.getTenantViewModel().setTenant(tenants.get(getLayoutPosition()));

        // Navigate to RequestListFragment
        NavController controller = Navigation.findNavController(activity, R.id.nav_host_fragment_content_main);
        controller.navigate(R.id.action_LandlordHomeFragment_to_RequestListFragment);
      });
    }

    public void bind(Tenant tenant) {
      this.tenant = tenant;

      // set the tenant id
      binding.requestTenantName.setText(tenant.getId().toString());

      // set the tenant username
      binding.requestDescription.setText(tenant.getUsername());
    }
  }
}
