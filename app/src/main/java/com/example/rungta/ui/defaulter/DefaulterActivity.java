package com.example.rungta.ui.defaulter;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.rungta.data.model.AttendanceRecord;
import com.example.rungta.data.model.Student;
import com.example.rungta.databinding.ActivityDefaulterBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaulterActivity extends AppCompatActivity {

    private ActivityDefaulterBinding binding;
    private FirebaseFirestore firestore;
    private DefaulterAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDefaulterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        firestore = FirebaseFirestore.getInstance();

        adapter = new DefaulterAdapter(null);
        binding.recyclerViewDefaulters.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewDefaulters.setAdapter(adapter);

        loadDefaulters();
    }

    private void loadDefaulters() {
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
                Map<String, int[]> attendanceStats = new HashMap<>(); // studentId -> [attended, total]

                for (QueryDocumentSnapshot doc : attendanceDocs) {
                    AttendanceRecord record = doc.toObject(AttendanceRecord.class);
                    String studentId = record.getStudentId();
                    if (studentId != null) {
                        int[] stats = attendanceStats.get(studentId);
                        if (stats == null) {
                            stats = new int[]{0, 0};
                        }
                        if (record.isPresent()) {
                            stats[0]++;
                        }
                        stats[1]++;
                        attendanceStats.put(studentId, stats);
                    }
                }

                List<DefaulterAdapter.DefaulterItem> defaulterItems = new ArrayList<>();
                for (Student student : students) {
                    int[] stats = attendanceStats.get(student.getStudentId());
                    float percentage = 100f; // Default if no records yet
                    if (stats != null && stats[1] > 0) {
                        percentage = ((float) stats[0] / stats[1]) * 100f;
                    }

                    if (percentage < 75.0f) {
                        defaulterItems.add(new DefaulterAdapter.DefaulterItem(
                                student.getName(),
                                student.getRollNo(),
                                student.getDepartment(),
                                percentage
                        ));
                    }
                }

                adapter.setDefaulterList(defaulterItems);
            });
        });
    }
}
