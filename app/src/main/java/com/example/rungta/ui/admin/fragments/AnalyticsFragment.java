package com.example.rungta.ui.admin.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.rungta.databinding.FragmentAnalyticsBinding;
import com.example.rungta.ui.analytics.AnalyticsActivity;

public class AnalyticsFragment extends Fragment {

    private FragmentAnalyticsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAnalyticsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnViewAnalyticsTab.setOnClickListener(v -> 
            startActivity(new Intent(requireContext(), AnalyticsActivity.class))
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
