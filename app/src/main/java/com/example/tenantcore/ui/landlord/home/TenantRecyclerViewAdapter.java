package com.example.tenantcore.ui.landlord.home;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tenantcore.R;
import com.example.tenantcore.databinding.ListItemTenantBinding;
import com.example.tenantcore.db.DatabaseException;
import com.example.tenantcore.model.Tenant;
import com.example.tenantcore.ui.TenantCoreActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TenantRecyclerViewAdapter extends RecyclerView.Adapter<TenantRecyclerViewAdapter.ViewHolder> {
  private final List<Tenant> tenants;
  private final LandlordHomeFragment landlordHomeFragment;

  // Date format to use for date displaying
  private final DateFormat DATE_FORMAT = new SimpleDateFormat("EEE MMMM dd, yyyy", Locale.US);

  public TenantRecyclerViewAdapter(List<Tenant> tenants, LandlordHomeFragment landlordHomeFragment) {
    this.tenants = tenants;
    this.landlordHomeFragment = landlordHomeFragment;
    setHasStableIds(true);
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    ListItemTenantBinding binding = ListItemTenantBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
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
    private ListItemTenantBinding binding;
    private Tenant tenant;

    public ViewHolder(ListItemTenantBinding binding) {
      super(binding.getRoot());
      this.binding = binding;

      binding.listRequestItemLayout.setOnClickListener(view -> {
        // Store the tenant clicked on in the ViewModel
        TenantCoreActivity activity = (TenantCoreActivity) landlordHomeFragment.getActivity();
        activity.getTenantViewModel().setTenant(tenant);

        // Navigate to RequestListFragment
        NavController controller = Navigation.findNavController(activity, R.id.nav_host_fragment_content_main);
        controller.navigate(R.id.action_LandlordHomeFragment_to_RequestListFragment);
      });
    }

    public void bind(Tenant tenant) {
      this.tenant = tenant;

//      binding.requestTenantImage.setImageResource(R.drawable.);

      // set the tenant id
      binding.tenantNameTextView.setText(tenant.getName());

      binding.lastLoginHeaderTextView.setText("Last Login : ");
      // set the tenant username
      if(tenant.getLastLogin() != null)
        binding.lastLoginDateTextView.setText(DATE_FORMAT.format(tenant.getLastLogin()));
      else
        binding.lastLoginDateTextView.setText("Never");

      TenantCoreActivity activity = (TenantCoreActivity) landlordHomeFragment.getActivity();

      int requestNum = 0;
      try {
        requestNum = activity.getTenantViewModel().getRequestsByTenant(tenant).size();
      } catch (DatabaseException e) {
        e.printStackTrace();
      }

      binding.remainingRequestsTextView.setText(String.valueOf(requestNum));
    }
  }
}
