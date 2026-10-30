package com.example.rungta.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.rungta.data.model.Faculty;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class FacultyRepository {
    private FirebaseFirestore firestore;

    public FacultyRepository() {
        try {
            firestore = FirebaseFirestore.getInstance();
        } catch (Exception e) {
            firestore = null;
        }
    }

    public LiveData<List<Faculty>> getFacultyList() {
        MutableLiveData<List<Faculty>> facultyLiveData = new MutableLiveData<>();
        if (firestore == null) {
            facultyLiveData.setValue(new ArrayList<>());
            return facultyLiveData;
        }

        firestore.collection("faculty").addSnapshotListener((value, error) -> {
            if (error != null) {
                return;
            }
            List<Faculty> list = new ArrayList<>();
            if (value != null) {
                for (QueryDocumentSnapshot doc : value) {
                    Faculty faculty = doc.toObject(Faculty.class);
                    if (faculty.getFacultyId() == null || faculty.getFacultyId().isEmpty()) {
                        faculty.setFacultyId(doc.getId());
                    }
                    list.add(faculty);
                }
            }
            facultyLiveData.setValue(list);
        });
        return facultyLiveData;
    }

    public void addFaculty(Faculty faculty, MutableLiveData<Boolean> resultLiveData) {
        if (firestore == null) {
            resultLiveData.setValue(false);
            return;
        }

        String id = faculty.getFacultyId();
        if (id == null || id.isEmpty()) {
            id = firestore.collection("faculty").document().getId();
            faculty.setFacultyId(id);
        }
        firestore.collection("faculty").document(id).set(faculty)
                .addOnSuccessListener(aVoid -> resultLiveData.setValue(true))
                .addOnFailureListener(e -> resultLiveData.setValue(false));
    }

    public void deleteFaculty(String facultyId, MutableLiveData<Boolean> resultLiveData) {
        if (firestore == null) {
            resultLiveData.setValue(false);
            return;
        }

        firestore.collection("faculty").document(facultyId).delete()
                .addOnSuccessListener(aVoid -> resultLiveData.setValue(true))
                .addOnFailureListener(e -> resultLiveData.setValue(false));
    }
}
