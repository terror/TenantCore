package com.example.tenantcore.ui.tenant.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.tenantcore.R;
import com.example.tenantcore.databinding.FragmentTenantHomeBinding;
import com.example.tenantcore.model.PlaceholderContent;
import com.example.tenantcore.ui.TenantCoreActivity;

public class TenantHomeFragment extends Fragment {
  private FragmentTenantHomeBinding binding;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentTenantHomeBinding.inflate(inflater, container, false);

    binding.viewRequestsBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // get place holder tenant and set it in the view-model
        PlaceholderContent.PlaceholderItem tenant = PlaceholderContent.ITEMS.get(0);
        ((TenantCoreActivity)getActivity()).getTaskViewModel().setTenant(tenant);

        NavHostFragment.findNavController(TenantHomeFragment.this)
          .navigate(R.id.action_TenantHomeFragment_to_RequestListFragment);
      }
    });

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
}
