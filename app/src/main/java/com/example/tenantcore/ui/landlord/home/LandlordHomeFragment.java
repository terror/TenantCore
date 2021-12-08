package com.example.tenantcore.ui.landlord.home;

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
import com.example.tenantcore.ui.TenantCoreActivity;

public class LandlordHomeFragment extends Fragment {
  public static String TAG_NAME = "landlord_home";
  private static final String ARG_COLUMN_COUNT = "column-count";
  private int mColumnCount = 1;
  private FragmentLandlordHomeBinding binding;
  private TenantCoreActivity tenantCoreActivity;

  public LandlordHomeFragment() {
  }

  @Override
  public void onAttach(@NonNull Context context) {
    super.onAttach(context);

    // set the activity
    tenantCoreActivity = (TenantCoreActivity) context;
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

    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }
}
