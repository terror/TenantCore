package com.example.tenantcore.ui.shared;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tenantcore.R;
import com.example.tenantcore.databinding.FragmentRequestListBinding;

public class RequestListFragment extends Fragment {

  private static final String ARG_COLUMN_COUNT = "column-count";
  private int mColumnCount = 1;
  private FragmentRequestListBinding binding;

  public RequestListFragment(){}

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (getArguments() != null) {
      mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    binding = FragmentRequestListBinding.inflate(inflater, container, false);
    RecyclerView recyclerView = binding.getRoot().findViewById(R.id.requestList_RecyclerView);

    // Set layout appropriately
    if (mColumnCount <= 1)
      recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    else
      recyclerView.setLayoutManager(new GridLayoutManager(getContext(), mColumnCount));

    // Set adapter
    recyclerView.setAdapter(new RequestListRecyclerViewAdapter(this));

    return binding.getRoot();

  }
}
