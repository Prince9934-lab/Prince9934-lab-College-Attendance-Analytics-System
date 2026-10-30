package com.example.rungta.ui.admin;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rungta.databinding.ActivityAdminDashboardBinding;
import com.example.rungta.ui.analytics.AnalyticsActivity;
import com.example.rungta.ui.attendance.AttendanceHistoryActivity;
import com.example.rungta.ui.attendance.MarkAttendanceActivity;
import com.example.rungta.ui.defaulter.DefaulterActivity;
import com.example.rungta.ui.faculty.FacultyManagementActivity;
import com.example.rungta.ui.notifications.NotificationsActivity;
import com.example.rungta.ui.reports.ReportActivity;
import com.example.rungta.ui.student.StudentManagementActivity;

public class AdminDashboardActivity extends AppCompatActivity {

    private ActivityAdminDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.cardStudents.setOnClickListener(v -> 
            startActivity(new Intent(this, StudentManagementActivity.class))
        );

        binding.cardFaculty.setOnClickListener(v -> 
            startActivity(new Intent(this, FacultyManagementActivity.class))
        );

        binding.cardMarkAttendance.setOnClickListener(v -> 
            startActivity(new Intent(this, MarkAttendanceActivity.class))
        );

        binding.cardHistory.setOnClickListener(v -> 
            startActivity(new Intent(this, AttendanceHistoryActivity.class))
        );

        binding.cardAnalytics.setOnClickListener(v -> 
            startActivity(new Intent(this, AnalyticsActivity.class))
        );

        binding.cardDefaulter.setOnClickListener(v -> 
            startActivity(new Intent(this, DefaulterActivity.class))
        );

        binding.cardReports.setOnClickListener(v -> 
            startActivity(new Intent(this, ReportActivity.class))
        );

        binding.cardNotifications.setOnClickListener(v -> 
            startActivity(new Intent(this, NotificationsActivity.class))
        );
    }
}
