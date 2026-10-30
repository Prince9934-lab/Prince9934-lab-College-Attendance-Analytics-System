package com.example.rungta.ui.student;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.rungta.data.model.Student;
import com.example.rungta.databinding.ActivityAddStudentBinding;

public class AddStudentActivity extends AppCompatActivity {

    private ActivityAddStudentBinding binding;
    private StudentViewModel viewModel;
    private String studentId = "";
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddStudentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        viewModel = new ViewModelProvider(this).get(StudentViewModel.class);

        // Check if editing existing student
        if (getIntent() != null && getIntent().hasExtra("studentId")) {
            isEditMode = true;
            studentId = getIntent().getStringExtra("studentId");
            binding.etName.setText(getIntent().getStringExtra("name"));
            binding.etRollNo.setText(getIntent().getStringExtra("rollNo"));
            binding.etDepartment.setText(getIntent().getStringExtra("department"));
            binding.etSemester.setText(getIntent().getStringExtra("semester"));
            binding.etPhone.setText(getIntent().getStringExtra("phone"));
            binding.etEmail.setText(getIntent().getStringExtra("email"));
            binding.btnSaveStudent.setText("Update Student");
        }

        binding.btnSaveStudent.setOnClickListener(v -> {
            String name = binding.etName.getText() != null ? binding.etName.getText().toString().trim() : "";
            String rollNo = binding.etRollNo.getText() != null ? binding.etRollNo.getText().toString().trim() : "";
            String department = binding.etDepartment.getText() != null ? binding.etDepartment.getText().toString().trim() : "";
            String semester = binding.etSemester.getText() != null ? binding.etSemester.getText().toString().trim() : "";
            String phone = binding.etPhone.getText() != null ? binding.etPhone.getText().toString().trim() : "";
            String email = binding.etEmail.getText() != null ? binding.etEmail.getText().toString().trim() : "";

            if (name.isEmpty() || rollNo.isEmpty() || department.isEmpty() || semester.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
                return;
            }

            Student student = new Student(studentId, name, rollNo, department, semester, phone, email);
            MutableLiveData<Boolean> result = new MutableLiveData<>();
            result.observe(this, success -> {
                if (Boolean.TRUE.equals(success)) {
                    String msg = isEditMode ? "Student updated successfully" : "Student added successfully";
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Operation failed", Toast.LENGTH_SHORT).show();
                }
            });
            viewModel.addStudent(student, result);
        });
    }
}
