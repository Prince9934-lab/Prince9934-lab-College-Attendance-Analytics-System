package com.example.rungta.ui.splash;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rungta.databinding.ActivitySplashBinding;
import com.example.rungta.ui.auth.LoginActivity;
import com.example.rungta.utils.DemoDataSeeder;

public class SplashActivity extends AppCompatActivity {

    private ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Smooth fade-in & scale animation
        binding.ivSplashLogo.setAlpha(0f);
        binding.ivSplashLogo.setScaleX(0.7f);
        binding.ivSplashLogo.setScaleY(0.7f);

        binding.tvAppTitle.setAlpha(0f);
        binding.tvFoundation.setAlpha(0f);
        binding.tvLocation.setAlpha(0f);

        ObjectAnimator logoAlpha = ObjectAnimator.ofFloat(binding.ivSplashLogo, "alpha", 0f, 1f);
        ObjectAnimator logoScaleX = ObjectAnimator.ofFloat(binding.ivSplashLogo, "scaleX", 0.7f, 1f);
        ObjectAnimator logoScaleY = ObjectAnimator.ofFloat(binding.ivSplashLogo, "scaleY", 0.7f, 1f);
        ObjectAnimator titleAlpha = ObjectAnimator.ofFloat(binding.tvAppTitle, "alpha", 0f, 1f);
        ObjectAnimator foundationAlpha = ObjectAnimator.ofFloat(binding.tvFoundation, "alpha", 0f, 1f);
        ObjectAnimator locationAlpha = ObjectAnimator.ofFloat(binding.tvLocation, "alpha", 0f, 1f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(logoAlpha, logoScaleX, logoScaleY, titleAlpha, foundationAlpha, locationAlpha);
        animatorSet.setDuration(1500);
        animatorSet.start();

        try {
            DemoDataSeeder.seedDemoDataIfNeeded(null);
        } catch (Exception e) {
            // Handled gracefully
        }

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }, 2500);
    }
}
