package com.example.rungta.ui.attendance;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.rungta.data.model.AttendanceRecord;
import com.example.rungta.data.model.Student;
import com.example.rungta.databinding.ActivityMarkAttendanceBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MarkAttendanceActivity extends AppCompatActivity {

    private ActivityMarkAttendanceBinding binding;
    private AttendanceViewModel viewModel;
    private AttendanceMarkAdapter adapter;
    private List<Student> studentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMarkAttendanceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        viewModel = new ViewModelProvider(this).get(AttendanceViewModel.class);

        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        if (binding.etDate.getText() != null && binding.etDate.getText().toString().isEmpty()) {
            binding.etDate.setText(currentDate);
        }

        adapter = new AttendanceMarkAdapter(studentList);
        binding.recyclerViewMark.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewMark.setAdapter(adapter);

        viewModel.getStudentsForAttendance().observe(this, students -> {
            studentList = students;
            adapter.setStudentList(students);
        });

        binding.btnSubmitAttendance.setOnClickListener(v -> {
            String subject = binding.etSubject.getText() != null ? binding.etSubject.getText().toString().trim() : "";
            String date = binding.etDate.getText() != null ? binding.etDate.getText().toString().trim() : "";

            if (subject.isEmpty() || date.isEmpty()) {
                Toast.makeText(this, "Please enter subject and date", Toast.LENGTH_SHORT).show();
                return;
            }

            if (studentList.isEmpty()) {
                Toast.makeText(this, "No students available to mark attendance", Toast.LENGTH_SHORT).show();
                return;
            }

            Map<String, Boolean> attendanceMap = adapter.getAttendanceMap();
            List<AttendanceRecord> records = new ArrayList<>();
            String facultyId = FirebaseAuth.getInstance().getCurrentUser() != null ? 
                    FirebaseAuth.getInstance().getCurrentUser().getUid() : "admin";

            for (Student student : studentList) {
                boolean isPresent = attendanceMap.getOrDefault(student.getStudentId(), true);
                AttendanceRecord record = new AttendanceRecord(
                        "",
                        student.getStudentId(),
                        student.getName(),
                        student.getRollNo(),
                        subject,
                        date,
                        isPresent,
                        facultyId
                );
                records.add(record);
            }

            MutableLiveData<Boolean> result = new MutableLiveData<>();
            result.observe(this, success -> {
                if (Boolean.TRUE.equals(success)) {
                    Toast.makeText(this, "Attendance submitted successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Failed to submit attendance", Toast.LENGTH_SHORT).show();
                }
            });
            viewModel.saveAttendanceRecords(records, result);
        });
    }
}
