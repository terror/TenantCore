package com.example.tenantcore.ui.home;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.example.tenantcore.R;
import com.example.tenantcore.databinding.FragmentHomeBinding;
import com.example.tenantcore.db.DatabaseException;
import com.example.tenantcore.model.InviteCode;
import com.example.tenantcore.model.Landlord;
import com.example.tenantcore.model.Tenant;
import com.example.tenantcore.ui.TenantCoreActivity;
import com.example.tenantcore.viewmodel.TenantCoreViewModel;
import java.util.Date;

public class HomeFragment extends Fragment {
  private TenantCoreActivity activity;
  private TenantCoreViewModel viewmodel;
  private FragmentHomeBinding binding;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentHomeBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    // Initialize database
    activity = (TenantCoreActivity) this.getContext();
    activity.initializeViewModel(activity);
    viewmodel = activity.getTenantViewModel();

    // Logging in
    binding.homeLoginButton.setOnClickListener(view1 -> {
      String usernameEntered = binding.homeLoginUsernameEditText.getText().toString();
      if (binding.homeIdentityTenantRadioButton.isChecked()) {

        // For now, a user can simply log in by writing any text in the EditText view.
        if (usernameEntered.isEmpty()) {
          // Failure
          activity.displayErrorMessage("Invalid tenant login.", "Please enter a valid username.");

        } else if (viewmodel.findTenant(usernameEntered) != null) {
          // Success
          viewmodel.setSignedInUser(usernameEntered);
          NavHostFragment.findNavController(HomeFragment.this)
            .navigate(R.id.action_HomeFragment_to_TenantHomeFragment);

        } else {
          activity.displayErrorMessage("Invalid tenant username", "Please enter a valid username");
        }

      } else {

        if (viewmodel.findLandlord(binding.homeLoginUsernameEditText.getText().toString()) == null) {
          // Failure
          activity.displayErrorMessage("Invalid landlord login.", "Please enter a valid username.");

        } else {
          if (binding.homeLoginUsernameEditText.getText().toString().isEmpty()) {
            // Failure
            activity.displayErrorMessage("Invalid landlord login.", "Please enter a valid username.");
          } else {
            // Check for a landlord matching the entered in username
            if (viewmodel.findLandlord(binding.homeLoginUsernameEditText.getText().toString()) != null) {
              // Success
              viewmodel.setSignedInUser(usernameEntered);
              NavHostFragment.findNavController(HomeFragment.this)
                .navigate(R.id.action_HomeFragment_to_LandlordHomeFragment);
            } else
              activity.displayErrorMessage("Invalid landlord login.", "Not a valid username.");
          }
        }
      }
    });

    // Creating an account
    binding.homeSignUpButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        // Make an account creation dialog
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.dialog_account_creation);

        final EditText usernameView = dialog.findViewById(R.id.userCreate_editTextUsername);
        final EditText nameView = dialog.findViewById(R.id.userCreate_editTextName);
        final Button createButton = dialog.findViewById(R.id.userCreate_createButton);
        final Button cancelButton = dialog.findViewById(R.id.userCreate_cancelButton);

        if (binding.homeIdentityTenantRadioButton.isChecked()) {
          // Empty invite code validation
          String inviteCodeText = binding.homeSignUpInviteEditText.getText().toString();
          if (inviteCodeText.isEmpty()) {
            activity.displayErrorMessage("No invite code provided.",
              "To create an account, ask your landlord to send you an invite code.");
            return;
          }

          // Invite code validation
          InviteCode inviteCode = viewmodel.findInviteCode(inviteCodeText);
          if (inviteCode == null) {
            activity.displayErrorMessage("Invalid invite code.",
              "To create an account, ask your landlord to send you an invite code.");
            return;
          }

          createButton.setOnClickListener(_view -> {
            // Empty username validation
            String username = usernameView.getText().toString();
            if (username.isEmpty()) {
              activity.displayErrorMessage("No username provided.", "Please fill the username field.");
              return;
            }

            // Empty name validation
            String name = nameView.getText().toString();
            if (name.isEmpty()) {
              activity.displayErrorMessage("No name provided.", "Please fill the name field.");
              return;
            }

            // Duplicate username validation
            if (viewmodel.findTenant(username) != null) {
              activity.displayErrorMessage("Username already taken.",
                "This username has already been taken. Please use another one.");
              return;
            }

            // Delete the invite code, make the new tenant account, and navigate to the tenant home
            try {
              // TODO: Uncomment once Invite Codes can be generated manually
              // For now the placeholder invite codes do not get deleted after being used
              // viewmodel.removeInvite(inviteCode);

              viewmodel.addTenant(new Tenant()
                .setLandlordId(inviteCode.getLandlordId())
                .setUsername(username)
                .setName(name)
                .setLastLogin(new Date()));

              viewmodel.setSignedInUser(username);

              NavHostFragment.findNavController(HomeFragment.this)
                .navigate(R.id.action_HomeFragment_to_TenantHomeFragment);

              dialog.dismiss();
            } catch (DatabaseException e) {
              e.printStackTrace();
              activity.displayErrorMessage("Create Tenant Account", "Failed to create a new Tenant account.");
            }
          });

          cancelButton.setOnClickListener(_view -> dialog.dismiss());

          dialog.show();
        } else {
          createButton.setOnClickListener(_view -> {
            // Empty username validation
            String username = usernameView.getText().toString();
            if (username.isEmpty()) {
              activity.displayErrorMessage("No username provided.", "Please fill the username field.");
              return;
            }

            // Empty name validation
            String name = nameView.getText().toString();
            if (name.isEmpty()) {
              activity.displayErrorMessage("No name provided.", "Please fill the name field.");
              return;
            }

            // Duplicate username validation
            if (viewmodel.findLandlord(username) != null) {
              activity.displayErrorMessage("Username already taken.",
                "This username has already been taken. Please use another one.");
              return;
            }

            try {
              viewmodel.addLandlord(new Landlord()
                .setUsername(username)
                .setName(name)
                .setLastLogin(new Date()));

              viewmodel.setSignedInUser(username);

              NavHostFragment.findNavController(HomeFragment.this)
                .navigate(R.id.action_HomeFragment_to_LandlordHomeFragment);

              dialog.dismiss();
            } catch (DatabaseException e) {
              e.printStackTrace();
              activity.displayErrorMessage("Create Landlord Account", "Failed to create a new landlord account.");
            }
          });

          cancelButton.setOnClickListener(_view -> dialog.dismiss());

          dialog.show();
        }
      }
    });

    // Radio button change
    binding.homeIdentityRadioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
      if (binding.homeIdentityTenantRadioButton.isChecked())
        binding.homeSignUpInviteEditText.setVisibility(View.VISIBLE);
      else if (binding.homeIdentityLandlordRadioButton.isChecked())
        binding.homeSignUpInviteEditText.setVisibility(View.GONE);
    });
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}
