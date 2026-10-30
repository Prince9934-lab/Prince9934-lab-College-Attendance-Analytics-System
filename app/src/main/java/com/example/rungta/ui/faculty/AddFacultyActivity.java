package com.example.rungta.ui.faculty;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.rungta.data.model.Faculty;
import com.example.rungta.databinding.ActivityAddFacultyBinding;

public class AddFacultyActivity extends AppCompatActivity {

    private ActivityAddFacultyBinding binding;
    private FacultyViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddFacultyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        viewModel = new ViewModelProvider(this).get(FacultyViewModel.class);

        binding.btnSaveFaculty.setOnClickListener(v -> {
            String name = binding.etName.getText() != null ? binding.etName.getText().toString().trim() : "";
            String email = binding.etEmail.getText() != null ? binding.etEmail.getText().toString().trim() : "";
            String department = binding.etDepartment.getText() != null ? binding.etDepartment.getText().toString().trim() : "";
            String subject = binding.etSubject.getText() != null ? binding.etSubject.getText().toString().trim() : "";

            if (name.isEmpty() || email.isEmpty() || department.isEmpty() || subject.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            Faculty faculty = new Faculty("", name, email, department, subject);
            MutableLiveData<Boolean> result = new MutableLiveData<>();
            result.observe(this, success -> {
                if (Boolean.TRUE.equals(success)) {
                    Toast.makeText(this, "Faculty added successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Failed to add faculty", Toast.LENGTH_SHORT).show();
                }
            });
            viewModel.addFaculty(faculty, result);
        });
    }
}
