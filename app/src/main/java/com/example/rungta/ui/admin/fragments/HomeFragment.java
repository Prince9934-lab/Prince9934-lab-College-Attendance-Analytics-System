package com.example.rungta.ui.admin.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.rungta.data.model.AttendanceRecord;
import com.example.rungta.data.model.Student;
import com.example.rungta.data.model.StudentRiskPrediction;
import com.example.rungta.databinding.FragmentHomeBinding;
import com.example.rungta.ui.analytics.AiPredictionActivity;
import com.example.rungta.ui.analytics.AiPredictionAdapter;
import com.example.rungta.ui.analytics.AnalyticsActivity;
import com.example.rungta.ui.attendance.MarkAttendanceActivity;
import com.example.rungta.ui.defaulter.DefaulterActivity;
import com.example.rungta.ui.student.StudentManagementActivity;
import com.example.rungta.utils.AttendancePredictionEngine;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private AiPredictionAdapter aiAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        try {
            firestore = FirebaseFirestore.getInstance();
        } catch (Exception e) {
            firestore = null;
        }

        if (mAuth.getCurrentUser() != null && mAuth.getCurrentUser().getEmail() != null) {
            binding.tvAdminEmail.setText(mAuth.getCurrentUser().getEmail());
        }

        setupTrendChart();
        setupClickListeners();
        setupAiRiskPredictions();
    }

    private void setupTrendChart() {
        if (binding == null) return;
        LineChart lineChart = binding.lineChartTrend;

        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry(1f, 82f));
        entries.add(new Entry(2f, 85f));
        entries.add(new Entry(3f, 88f));
        entries.add(new Entry(4f, 86f));
        entries.add(new Entry(5f, 90f));
        entries.add(new Entry(6f, 89.5f));

        LineDataSet dataSet = new LineDataSet(entries, "Attendance %");
        dataSet.setColor(getResources().getColor(android.R.color.holo_blue_dark));
        dataSet.setValueTextColor(getResources().getColor(android.R.color.darker_gray));
        dataSet.setLineWidth(3f);
        dataSet.setCircleRadius(5f);
        dataSet.setDrawCircleHole(true);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.getDescription().setEnabled(false);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.animateX(1000);
        lineChart.invalidate();
    }

    private void setupClickListeners() {
        binding.cardTotalStudents.setOnClickListener(v -> 
            startActivity(new Intent(requireContext(), StudentManagementActivity.class))
        );
        binding.cardPresentToday.setOnClickListener(v -> 
            startActivity(new Intent(requireContext(), MarkAttendanceActivity.class))
        );
        binding.cardAbsentToday.setOnClickListener(v -> 
            startActivity(new Intent(requireContext(), DefaulterActivity.class))
        );
        binding.cardAvgAttendance.setOnClickListener(v -> 
            startActivity(new Intent(requireContext(), AnalyticsActivity.class))
        );
        binding.btnViewAllAi.setOnClickListener(v ->
            startActivity(new Intent(requireContext(), AiPredictionActivity.class))
        );
    }

    private void setupAiRiskPredictions() {
        if (getContext() == null) return;
        aiAdapter = new AiPredictionAdapter(null);
        binding.recyclerViewAiRiskHome.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerViewAiRiskHome.setAdapter(aiAdapter);

        try {
            if (firestore == null) {
                loadMockPredictions();
                return;
            }

            firestore.collection("students").get().addOnSuccessListener(studentDocs -> {
                if (!isAdded() || binding == null) return;
                List<Student> students = new ArrayList<>();
                for (QueryDocumentSnapshot doc : studentDocs) {
                    Student student = doc.toObject(Student.class);
                    if (student.getStudentId() == null || student.getStudentId().isEmpty()) {
                        student.setStudentId(doc.getId());
                    }
                    students.add(student);
                }

                if (students.isEmpty()) {
                    loadMockPredictions();
                    return;
                }

                firestore.collection("attendance").get().addOnSuccessListener(attendanceDocs -> {
                    if (!isAdded() || binding == null) return;
                    List<AttendanceRecord> records = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : attendanceDocs) {
                        AttendanceRecord record = doc.toObject(AttendanceRecord.class);
                        records.add(record);
                    }

                    List<StudentRiskPrediction> allPredictions = 
                            AttendancePredictionEngine.predictDefaulters(students, records);

                    List<StudentRiskPrediction> highRiskTop5 = new ArrayList<>();
                    for (StudentRiskPrediction p : allPredictions) {
                        if ("HIGH RISK".equals(p.getRiskLevel())) {
                            highRiskTop5.add(p);
                            if (highRiskTop5.size() >= 5) break;
                        }
                    }

                    aiAdapter.setPredictionList(highRiskTop5);
                }).addOnFailureListener(e -> loadMockPredictions());
            }).addOnFailureListener(e -> loadMockPredictions());
        } catch (Exception e) {
            loadMockPredictions();
        }
    }

    private void loadMockPredictions() {
        if (!isAdded() || binding == null) return;
        List<Student> mockStudents = new ArrayList<>();
        mockStudents.add(new Student("stud_1", "Rahul Kumar", "RNG2024001", "CSE", "5th", "9876543001", "rahul@rungta.ac.in"));
        mockStudents.add(new Student("stud_2", "Priya Sharma", "RNG2024002", "CSE", "5th", "9876543002", "priya@rungta.ac.in"));
        
        List<AttendanceRecord> mockRecords = new ArrayList<>();
        for (Student s : mockStudents) {
            for (int i = 0; i < 10; i++) {
                mockRecords.add(new AttendanceRecord("rec_" + i, s.getStudentId(), s.getName(), s.getRollNo(), "Data Structures", "01-10-2024", i < 6, "fac_1"));
            }
        }

        List<StudentRiskPrediction> predictions = AttendancePredictionEngine.predictDefaulters(mockStudents, mockRecords);
        aiAdapter.setPredictionList(predictions);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
