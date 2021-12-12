package com.example.tenantcore.ui.landlord.home;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tenantcore.R;
import com.example.tenantcore.databinding.FragmentLandlordHomeBinding;
import com.example.tenantcore.db.DatabaseException;
import com.example.tenantcore.model.InviteCode;
import com.example.tenantcore.model.Landlord;
import com.example.tenantcore.services.EmailSender;
import com.example.tenantcore.ui.TenantCoreActivity;
import com.example.tenantcore.viewmodel.TenantCoreViewModel;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LandlordHomeFragment extends Fragment {
  public static String TAG_NAME = "landlord_home";
  private static final String ARG_COLUMN_COUNT = "column-count";
  private int mColumnCount = 1;
  private FragmentLandlordHomeBinding binding;
  private TenantCoreActivity tenantCoreActivity;
  private TenantCoreViewModel viewModel;
  private Landlord user;

  public LandlordHomeFragment() {
  }

  @Override
  public void onAttach(@NonNull Context context) {
    super.onAttach(context);

    // set the activity, view-model, and landlord user
    tenantCoreActivity = (TenantCoreActivity) context;
    viewModel = tenantCoreActivity.getTenantViewModel();
    user = viewModel.findLandlord(viewModel.getSignedInUser());
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (getArguments() != null) {
      mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    if (getActivity().getSupportFragmentManager().findFragmentByTag(TAG_NAME) == null)
      getActivity().getSupportFragmentManager().beginTransaction().add(this, TAG_NAME).commit();

    binding = FragmentLandlordHomeBinding.inflate(inflater, container, false);

    // Grab the recycler view from layout
    RecyclerView recyclerView = binding.getRoot().findViewById(R.id.tenant_request_recyclerView);

    // Account for `mColumnCount`
    if (mColumnCount <= 1)
      recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    else
      recyclerView.setLayoutManager(new GridLayoutManager(getContext(), mColumnCount));

    String signedInLandlord = tenantCoreActivity.getTenantViewModel().getSignedInUser();

    // Set the adapter
    try {
      recyclerView.setAdapter(
        new TenantRecyclerViewAdapter(
          tenantCoreActivity.getTenantViewModel().getTenantsByLandlord(tenantCoreActivity.getTenantViewModel().findLandlord(signedInLandlord)),
          this
        )
      );
    } catch (DatabaseException e) {
      e.printStackTrace();
    }

    binding.inviteTenantButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        try {
          sendInvitation();
        } catch (DatabaseException e) {
          e.printStackTrace();
        }
      }
    });

    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }

  private void sendInvitation() throws DatabaseException {
    String email = binding.inviteTenantEditText.getText().toString();

    // Validate the entered email, if the email is found to be invalid display an error message and return
    if (!validateEmail(email)) {
      tenantCoreActivity.displayErrorMessage("Invalid Email", "Please enter a valid email address.");
      return;
    }

    // Generate the invite code, add it to the database, and send the invitation email
    InviteCode inviteCode = generateInviteCode();
    viewModel.addInviteCode(inviteCode);
    sendInvitationEmail(email, inviteCode);

    // Clear the entered email and display a confirmation pop-up
    binding.inviteTenantEditText.getText().clear();
    tenantCoreActivity.displaySnackbar("Invitation sent.");
  }

  private boolean validateEmail(String email) {
    // Create a pattern using the valid email regex
    Pattern pattern = Pattern.compile("^[\\.a-zA-Z0-9]+@[^@\\s]+\\.[^@\\s]+$");

    // Return true if the provided email matches the pattern, otherwise return false
    return pattern.matcher(email).matches();
  }

  private InviteCode generateInviteCode() throws DatabaseException {
    int code = 0;

    // Generate a random code that doesn't already belong to an invite code in the database
    do {
      code = InviteCode.generateCode();
    } while (viewModel.findInviteCode(String.valueOf(code)) != null);

    // Return a new invite code consisting of this landlords ID, the generated code, and the default expiry
    return new InviteCode()
      .setLandlordId(user.getId())
      .setCode(code)
      .setExpiry(InviteCode.DEFAULT_EXPIRY);
  }

  private void sendInvitationEmail(String email, InviteCode inviteCode) {
    // Send invitation email to entered email address
    EmailSender emailSender = new EmailSender(
      getContext(),
      email,
      "Invitation code",
      String.format("%s just invited you to their building!\nYour invitation code is: %2d", user.getName(), inviteCode.getCode())
    );

    emailSender.execute();
  }
}
