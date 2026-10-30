package com.example.rungta.ui.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.rungta.R;
import com.example.rungta.databinding.ActivityDashboardBinding;
import com.example.rungta.ui.admin.fragments.AnalyticsFragment;
import com.example.rungta.ui.admin.fragments.AttendanceFragment;
import com.example.rungta.ui.admin.fragments.HomeFragment;
import com.example.rungta.ui.admin.fragments.ProfileFragment;
import com.example.rungta.ui.admin.fragments.StudentsFragment;
import com.example.rungta.ui.auth.LoginActivity;
import com.example.rungta.ui.notifications.NotificationsActivity;
import com.google.firebase.auth.FirebaseAuth;

public class DashboardActivity extends AppCompatActivity {

    private ActivityDashboardBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        setSupportActionBar(binding.toolbar);

        // Load default fragment (Home)
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (id == R.id.nav_students) {
                selectedFragment = new StudentsFragment();
            } else if (id == R.id.nav_attendance) {
                selectedFragment = new AttendanceFragment();
            } else if (id == R.id.nav_analytics) {
                selectedFragment = new AnalyticsFragment();
            } else if (id == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
                return true;
            }
            return false;
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_notifications) {
            startActivity(new Intent(this, NotificationsActivity.class));
            return true;
        } else if (id == R.id.action_logout) {
            mAuth.signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
