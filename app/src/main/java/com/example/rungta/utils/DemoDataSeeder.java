package com.example.rungta.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class DemoDataSeeder {

    public static void seedDemoDataIfNeeded(Runnable onComplete) {
        try {
            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                if (onComplete != null) onComplete.run();
                return;
            }

            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            firestore.collection("students").limit(1).get().addOnSuccessListener(snapshot -> {
                if (snapshot.isEmpty()) {
                    // Demo seeding logic
                }
                if (onComplete != null) onComplete.run();
            }).addOnFailureListener(e -> {
                if (onComplete != null) onComplete.run();
            });
        } catch (Exception e) {
            if (onComplete != null) onComplete.run();
        }
    }
}
