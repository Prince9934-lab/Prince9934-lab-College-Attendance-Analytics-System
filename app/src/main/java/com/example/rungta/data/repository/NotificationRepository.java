package com.example.rungta.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.rungta.data.model.Notification;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class NotificationRepository {
    private FirebaseFirestore firestore;

    public NotificationRepository() {
        try {
            firestore = FirebaseFirestore.getInstance();
        } catch (Exception e) {
            firestore = null;
        }
    }

    public LiveData<List<Notification>> getNotifications() {
        MutableLiveData<List<Notification>> notificationsLiveData = new MutableLiveData<>();
        if (firestore == null) {
            notificationsLiveData.setValue(new ArrayList<>());
            return notificationsLiveData;
        }

        firestore.collection("notifications").addSnapshotListener((value, error) -> {
            if (error != null) {
                return;
            }
            List<Notification> list = new ArrayList<>();
            if (value != null) {
                for (QueryDocumentSnapshot doc : value) {
                    Notification notification = doc.toObject(Notification.class);
                    if (notification.getNotificationId() == null || notification.getNotificationId().isEmpty()) {
                        notification.setNotificationId(doc.getId());
                    }
                    list.add(notification);
                }
            }
            notificationsLiveData.setValue(list);
        });
        return notificationsLiveData;
    }

    public void sendNotification(Notification notification, MutableLiveData<Boolean> resultLiveData) {
        if (firestore == null) {
            resultLiveData.setValue(false);
            return;
        }

        String id = firestore.collection("notifications").document().getId();
        notification.setNotificationId(id);
        firestore.collection("notifications").document(id).set(notification)
                .addOnSuccessListener(aVoid -> resultLiveData.setValue(true))
                .addOnFailureListener(e -> resultLiveData.setValue(false));
    }
}
