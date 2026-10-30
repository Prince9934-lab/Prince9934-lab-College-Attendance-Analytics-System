package com.example.rungta.ui.notifications;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.rungta.data.model.Notification;
import com.example.rungta.data.repository.NotificationRepository;
import com.example.rungta.databinding.ActivityNotificationsBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NotificationsActivity extends AppCompatActivity {

    private ActivityNotificationsBinding binding;
    private NotificationRepository repository;
    private NotificationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        repository = new NotificationRepository();

        adapter = new NotificationAdapter(null);
        binding.recyclerViewNotifications.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewNotifications.setAdapter(adapter);

        repository.getNotifications().observe(this, notifications -> {
            adapter.setNotificationList(notifications);
        });

        binding.fabSendNotification.setOnClickListener(v -> showSendNotificationDialog());
    }

    private void showSendNotificationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Send Notification");

        android.widget.LinearLayout layout = new android.widget.LinearLayout(this);
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);
        layout.setPadding(40, 20, 40, 20);

        final EditText etTitle = new EditText(this);
        etTitle.setHint("Title");
        layout.addView(etTitle);

        final EditText etMessage = new EditText(this);
        etMessage.setHint("Message");
        layout.addView(etMessage);

        builder.setView(layout);

        builder.setPositiveButton("Send", (dialog, which) -> {
            String title = etTitle.getText().toString().trim();
            String message = etMessage.getText().toString().trim();

            if (title.isEmpty() || message.isEmpty()) {
                Toast.makeText(this, "Please enter title and message", Toast.LENGTH_SHORT).show();
                return;
            }

            String timestamp = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(new Date());
            Notification notification = new Notification("", title, message, timestamp);

            MutableLiveData<Boolean> result = new MutableLiveData<>();
            result.observe(this, success -> {
                if (Boolean.TRUE.equals(success)) {
                    Toast.makeText(this, "Notification sent successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failed to send notification", Toast.LENGTH_SHORT).show();
                }
            });
            repository.sendNotification(notification, result);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }
}
