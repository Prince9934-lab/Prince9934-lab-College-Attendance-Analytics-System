package com.example.rungta.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.rungta.R;
import com.example.rungta.databinding.ActivityRegisterBinding;
import com.example.rungta.ui.admin.AdminDashboardActivity;
import com.example.rungta.utils.DemoDataSeeder;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        authViewModel.getAuthResult().observe(this, result -> {
            if (result == null) return;
            if (result.getError() != null && !result.getError().isEmpty() && result.isError()) {
                binding.progressBar.setVisibility(View.GONE);
                binding.btnRegister.setEnabled(true);
                Toast.makeText(this, result.getError(), Toast.LENGTH_LONG).show();
            } else if ("Registering...".equals(result.getError())) {
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.btnRegister.setEnabled(false);
            } else if (result.getUserModel() != null) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                
                // Seed demo data upon successful authenticated registration
                DemoDataSeeder.seedDemoDataIfNeeded(null);

                startActivity(new Intent(RegisterActivity.this, AdminDashboardActivity.class));
                finishAffinity();
            }
        });

        binding.btnRegister.setOnClickListener(v -> {
            String name = binding.etName.getText() != null ? binding.etName.getText().toString().trim() : "";
            String email = binding.etEmail.getText() != null ? binding.etEmail.getText().toString().trim() : "";
            String password = binding.etPassword.getText() != null ? binding.etPassword.getText().toString().trim() : "";

            String role = "ADMIN";
            int checkedId = binding.rgRole.getCheckedRadioButtonId();
            if (checkedId == R.id.rbFaculty) {
                role = "FACULTY";
            } else if (checkedId == R.id.rbStudent) {
                role = "STUDENT";
            }

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fix all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            authViewModel.register(email, password, name, role);
        });

        binding.tvLogin.setOnClickListener(v -> finish());
    }
}
