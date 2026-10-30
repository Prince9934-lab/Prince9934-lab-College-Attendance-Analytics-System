package com.example.rungta.ui.student;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.rungta.data.model.Student;
import com.example.rungta.databinding.ActivityStudentManagementBinding;

public class StudentManagementActivity extends AppCompatActivity {

    private ActivityStudentManagementBinding binding;
    private StudentViewModel viewModel;
    private StudentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentManagementBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());

        viewModel = new ViewModelProvider(this).get(StudentViewModel.class);

        adapter = new StudentAdapter(null, new StudentAdapter.OnStudentActionListener() {
            @Override
            public void onEdit(Student student) {
                Intent intent = new Intent(StudentManagementActivity.this, AddStudentActivity.class);
                intent.putExtra("studentId", student.getStudentId());
                intent.putExtra("name", student.getName());
                intent.putExtra("rollNo", student.getRollNo());
                intent.putExtra("department", student.getDepartment());
                intent.putExtra("semester", student.getSemester());
                intent.putExtra("phone", student.getPhone());
                intent.putExtra("email", student.getEmail());
                startActivity(intent);
            }

            @Override
            public void onDelete(Student student) {
                new AlertDialog.Builder(StudentManagementActivity.this)
                        .setTitle("Delete Student")
                        .setMessage("Are you sure you want to delete " + student.getName() + "?")
                        .setPositiveButton("Delete", (dialog, which) -> {
                            MutableLiveData<Boolean> result = new MutableLiveData<>();
                            result.observe(StudentManagementActivity.this, success -> {
                                if (Boolean.TRUE.equals(success)) {
                                    Toast.makeText(StudentManagementActivity.this, "Student deleted successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(StudentManagementActivity.this, "Failed to delete student", Toast.LENGTH_SHORT).show();
                                }
                            });
                            viewModel.deleteStudent(student.getStudentId(), result);
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }

            @Override
            public void onView(Student student) {
                new AlertDialog.Builder(StudentManagementActivity.this)
                        .setTitle("Student Details")
                        .setMessage("Name: " + student.getName() +
                                "\nRoll No: " + student.getRollNo() +
                                "\nDepartment: " + student.getDepartment() +
                                "\nSemester: " + student.getSemester() +
                                "\nPhone: " + (student.getPhone() != null ? student.getPhone() : "N/A") +
                                "\nEmail: " + student.getEmail())
                        .setPositiveButton("OK", null)
                        .show();
            }
        });

        binding.recyclerViewStudents.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewStudents.setAdapter(adapter);

        viewModel.getStudents().observe(this, students -> {
            adapter.setStudentList(students);
        });

        binding.searchViewStudent.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return false;
            }
        });

        binding.fabAddStudent.setOnClickListener(v -> {
            startActivity(new Intent(this, AddStudentActivity.class));
        });
    }
}
