package com.example.rungta.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.rungta.databinding.ActivityLoginBinding;
import com.example.rungta.ui.admin.DashboardActivity;
import com.example.rungta.utils.DemoDataSeeder;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        authViewModel.getAuthResult().observe(this, result -> {
            if (result == null) return;

            if (result.isError()) {
                binding.progressBar.setVisibility(View.GONE);
                binding.btnLogin.setEnabled(true);
                String errorMsg = result.getError() != null ? result.getError() : "Login failed";
                Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
            } else if ("Loading...".equals(result.getError())) {
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.btnLogin.setEnabled(false);
            } else if (result.getUserModel() != null) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();
                
                // Seed demo data upon successful authenticated login
                DemoDataSeeder.seedDemoDataIfNeeded(null);

                // Route to DashboardActivity (with Bottom Navigation)
                startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                finish();
            }
        });

        binding.btnLogin.setOnClickListener(v -> {
            String email = binding.etEmail.getText() != null ? binding.etEmail.getText().toString().trim() : "";
            String password = binding.etPassword.getText() != null ? binding.etPassword.getText().toString().trim() : "";

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            authViewModel.login(email, password);
        });

        binding.tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }
}
