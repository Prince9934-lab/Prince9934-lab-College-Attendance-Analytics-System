package com.example.rungta.ui.attendance;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.rungta.databinding.ActivityAttendanceHistoryBinding;

public class AttendanceHistoryActivity extends AppCompatActivity {

    private ActivityAttendanceHistoryBinding binding;
    private AttendanceViewModel viewModel;
    private AttendanceHistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAttendanceHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        viewModel = new ViewModelProvider(this).get(AttendanceViewModel.class);

        adapter = new AttendanceHistoryAdapter(null);
        binding.recyclerViewHistory.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewHistory.setAdapter(adapter);

        viewModel.getAttendanceHistory().observe(this, records -> {
            adapter.setRecordList(records);
        });
    }
}
