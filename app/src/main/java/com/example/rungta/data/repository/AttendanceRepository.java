package com.example.rungta.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.rungta.data.model.AttendanceRecord;
import com.example.rungta.data.model.Student;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AttendanceRepository {
    private FirebaseFirestore firestore;

    public AttendanceRepository() {
        try {
            firestore = FirebaseFirestore.getInstance();
        } catch (Exception e) {
            firestore = null;
        }
    }

    public LiveData<List<Student>> getStudentsForAttendance() {
        MutableLiveData<List<Student>> studentsLiveData = new MutableLiveData<>();
        if (firestore == null) {
            studentsLiveData.setValue(new ArrayList<>());
            return studentsLiveData;
        }

        firestore.collection("students").get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<Student> list = new ArrayList<>();
            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                Student student = doc.toObject(Student.class);
                if (student.getStudentId() == null || student.getStudentId().isEmpty()) {
                    student.setStudentId(doc.getId());
                }
                list.add(student);
            }
            studentsLiveData.setValue(list);
        }).addOnFailureListener(e -> studentsLiveData.setValue(new ArrayList<>()));
        return studentsLiveData;
    }

    public LiveData<List<AttendanceRecord>> getAttendanceHistory() {
        MutableLiveData<List<AttendanceRecord>> historyLiveData = new MutableLiveData<>();
        if (firestore == null) {
            historyLiveData.setValue(new ArrayList<>());
            return historyLiveData;
        }

        firestore.collection("attendance").addSnapshotListener((value, error) -> {
            if (error != null) {
                return;
            }
            List<AttendanceRecord> list = new ArrayList<>();
            if (value != null) {
                for (QueryDocumentSnapshot doc : value) {
                    AttendanceRecord record = doc.toObject(AttendanceRecord.class);
                    if (record.getRecordId() == null || record.getRecordId().isEmpty()) {
                        record.setRecordId(doc.getId());
                    }
                    list.add(record);
                }
            }
            historyLiveData.setValue(list);
        });
        return historyLiveData;
    }

    public void saveAttendanceRecords(List<AttendanceRecord> records, MutableLiveData<Boolean> resultLiveData) {
        if (firestore == null) {
            resultLiveData.setValue(false);
            return;
        }

        final int[] completed = {0};
        final boolean[] hasError = {false};
        int total = records.size();
        if (total ==0) {
            resultLiveData.setValue(true);
            return;
        }

        for (AttendanceRecord record : records) {
            String id = firestore.collection("attendance").document().getId();
            record.setRecordId(id);
            firestore.collection("attendance").document(id).set(record)
                    .addOnSuccessListener(aVoid -> {
                        completed[0]++;
                        if (completed[0] == total) {
                            resultLiveData.setValue(!hasError[0]);
                        }
                    })
                    .addOnFailureListener(e -> {
                        hasError[0] = true;
                        completed[0]++;
                        if (completed[0] == total) {
                            resultLiveData.setValue(false);
                        }
                    });
        }
    }
}
