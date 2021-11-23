package com.example.tenantcore.ui.landlordhome;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tenantcore.R;
import com.example.tenantcore.databinding.FragmentLandlordHomeBinding;

public class LandlordHomeFragment extends Fragment {
  private FragmentLandlordHomeBinding binding;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentLandlordHomeBinding.inflate(inflater, container, false);
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
