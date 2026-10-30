package com.example.rungta.ui.analytics;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.rungta.data.model.AttendanceRecord;
import com.example.rungta.data.model.Student;
import com.example.rungta.data.model.StudentRiskPrediction;
import com.example.rungta.databinding.ActivityAiPredictionBinding;
import com.example.rungta.utils.AttendancePredictionEngine;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AiPredictionActivity extends AppCompatActivity {

    private ActivityAiPredictionBinding binding;
    private FirebaseFirestore firestore;
    private AiPredictionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAiPredictionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        firestore = FirebaseFirestore.getInstance();

        adapter = new AiPredictionAdapter(null);
        binding.recyclerViewPredictions.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewPredictions.setAdapter(adapter);

        loadPredictions();
    }

    private void loadPredictions() {
        firestore.collection("students").get().addOnSuccessListener(studentDocs -> {
            List<Student> students = new ArrayList<>();
            for (QueryDocumentSnapshot doc : studentDocs) {
                Student student = doc.toObject(Student.class);
                if (student.getStudentId() == null || student.getStudentId().isEmpty()) {
                    student.setStudentId(doc.getId());
                }
                students.add(student);
            }

            firestore.collection("attendance").get().addOnSuccessListener(attendanceDocs -> {
                List<AttendanceRecord> records = new ArrayList<>();
                for (QueryDocumentSnapshot doc : attendanceDocs) {
                    AttendanceRecord record = doc.toObject(AttendanceRecord.class);
                    records.add(record);
                }

                List<StudentRiskPrediction> predictions = 
                        AttendancePredictionEngine.predictDefaulters(students, records);

                adapter.setPredictionList(predictions);
            }).addOnFailureListener(e -> {
                Toast.makeText(this, "Error loading attendance: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Error loading students: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}
