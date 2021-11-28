package com.example.tenantcore.ui.home;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.tenantcore.R;
import com.example.tenantcore.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {
  private FragmentHomeBinding binding;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentHomeBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    // Logging in
    binding.homeLoginButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        if (binding.homeIdentityTenantRadioButton.isChecked()) {

          // TODO: Login validation for tenants
          // For now, a user can simply log in by writing any text in the EditText view.
          if (binding.homeLoginEditText.getText().toString().isEmpty()) {

            // Failure
            new AlertDialog.Builder(getContext())
              .setIcon(R.drawable.ic_baseline_warning_24)
              .setTitle("Invalid tenant login.")
              .setMessage("Please enter a valid username.")
              .setPositiveButton("OK", null)
              .create()
              .show();

          } else {

            // Success
            NavHostFragment.findNavController(HomeFragment.this)
              .navigate(R.id.action_HomeFragment_to_TenantHomeFragment);

          }

        } else {

          // TODO: Login validation for landlords
          // For now, a user can simply log in by writing any text in the EditText view.
          if (binding.homeLoginEditText.getText().toString().isEmpty()) {

            // Failure
            new AlertDialog.Builder(getContext())
              .setIcon(R.drawable.ic_baseline_warning_24)
              .setTitle("Invalid landlord login.")
              .setMessage("Please enter a valid username.")
              .setPositiveButton("OK", null)
              .create()
              .show();

          } else {

            // Success
            NavHostFragment.findNavController(HomeFragment.this)
              .navigate(R.id.action_HomeFragment_to_LandlordHomeFragment);

          }

        }
      }
    });

    // Creating an account
    binding.homeCreateButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (binding.homeIdentityTenantRadioButton.isChecked()) {

          // TODO: Account creation for tenants
          new AlertDialog.Builder(getContext())
            .setIcon(R.drawable.ic_baseline_warning_24)
            .setTitle("Feature not implemented.")
            .setMessage("Sorry, tenants cannot create an account for now.")
            .setPositiveButton("OK", null)
            .create()
            .show();

        } else {

          // TODO: Account creation for landlords
          new AlertDialog.Builder(getContext())
            .setIcon(R.drawable.ic_baseline_warning_24)
            .setTitle("Feature not implemented.")
            .setMessage("Sorry, landlords cannot create an account for now.")
            .setPositiveButton("OK", null)
            .create()
            .show();

        }
      }
    });

    // Radio button change
    binding.homeIdentityTenantRadioButton.setOnClickListener(tenantRadView -> {
      binding.homeCreateEditText.setHint("Invite Code");
    });
    binding.homeIdentityLandlordRadioButton.setOnClickListener(landlordRadView -> {
      binding.homeCreateEditText.setHint("Username");
    });
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}
