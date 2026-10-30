package com.example.rungta.ui.faculty;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.rungta.databinding.ActivityFacultyManagementBinding;

public class FacultyManagementActivity extends AppCompatActivity {

    private ActivityFacultyManagementBinding binding;
    private FacultyViewModel viewModel;
    private FacultyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFacultyManagementBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());

        viewModel = new ViewModelProvider(this).get(FacultyViewModel.class);

        adapter = new FacultyAdapter(null, faculty -> {
            MutableLiveData<Boolean> result = new MutableLiveData<>();
            result.observe(this, success -> {
                if (Boolean.TRUE.equals(success)) {
                    Toast.makeText(this, "Faculty deleted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failed to delete faculty", Toast.LENGTH_SHORT).show();
                }
            });
            viewModel.deleteFaculty(faculty.getFacultyId(), result);
        });

        binding.recyclerViewFaculty.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewFaculty.setAdapter(adapter);

        viewModel.getFacultyList().observe(this, facultyList -> {
            adapter.setFacultyList(facultyList);
        });

        binding.fabAddFaculty.setOnClickListener(v -> {
            startActivity(new Intent(this, AddFacultyActivity.class));
        });
    }
}
