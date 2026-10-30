package com.example.rungta.utils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.rungta.data.model.AttendanceRecord;
import com.example.rungta.data.model.Faculty;
import com.example.rungta.data.model.Student;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseManager {
    private static FirebaseManager instance;
    private final FirebaseAuth firebaseAuth;
    private final FirebaseFirestore firestore;

    private FirebaseManager() {
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    public static synchronized FirebaseManager getInstance() {
        if (instance == null) {
            instance = new FirebaseManager();
        }
        return instance;
    }

    // ==================== AUTHENTICATION ====================

    public FirebaseUser getCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }

    public void signIn(String email, String password, MutableLiveData<Boolean> callback) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> callback.setValue(true))
                .addOnFailureListener(e -> callback.setValue(false));
    }

    public void signUp(String email, String password, MutableLiveData<Boolean> callback) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> callback.setValue(true))
                .addOnFailureListener(e -> callback.setValue(false));
    }

    public void signOut() {
        firebaseAuth.signOut();
    }

    // ==================== STUDENTS COLLECTION CRUD ====================

    public void addOrUpdateStudent(Student student, MutableLiveData<Boolean> callback) {
        String id = student.getStudentId();
        if (id == null || id.isEmpty()) {
            id = firestore.collection("students").document().getId();
            student.setStudentId(id);
        }
        firestore.collection("students").document(id).set(student)
                .addOnSuccessListener(aVoid -> callback.setValue(true))
                .addOnFailureListener(e -> callback.setValue(false));
    }

    public LiveData<List<Student>> getStudents() {
        MutableLiveData<List<Student>> liveData = new MutableLiveData<>();
        firestore.collection("students").addSnapshotListener((value, error) -> {
            if (error != null) return;
            List<Student> list = new ArrayList<>();
            if (value != null) {
                for (QueryDocumentSnapshot doc : value) {
                    Student student = doc.toObject(Student.class);
                    if (student.getStudentId() == null || student.getStudentId().isEmpty()) {
                        student.setStudentId(doc.getId());
                    }
                    list.add(student);
                }
            }
            liveData.setValue(list);
        });
        return liveData;
    }

    public void deleteStudent(String studentId, MutableLiveData<Boolean> callback) {
        firestore.collection("students").document(studentId).delete()
                .addOnSuccessListener(aVoid -> callback.setValue(true))
                .addOnFailureListener(e -> callback.setValue(false));
    }

    // ==================== FACULTY COLLECTION CRUD ====================

    public void addOrUpdateFaculty(Faculty faculty, MutableLiveData<Boolean> callback) {
        String id = faculty.getFacultyId();
        if (id == null || id.isEmpty()) {
            id = firestore.collection("faculty").document().getId();
            faculty.setFacultyId(id);
        }
        firestore.collection("faculty").document(id).set(faculty)
                .addOnSuccessListener(aVoid -> callback.setValue(true))
                .addOnFailureListener(e -> callback.setValue(false));
    }

    public LiveData<List<Faculty>> getFacultyList() {
        MutableLiveData<List<Faculty>> liveData = new MutableLiveData<>();
        firestore.collection("faculty").addSnapshotListener((value, error) -> {
            if (error != null) return;
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
            liveData.setValue(list);
        });
        return liveData;
    }

    public void deleteFaculty(String facultyId, MutableLiveData<Boolean> callback) {
        firestore.collection("faculty").document(facultyId).delete()
                .addOnSuccessListener(aVoid -> callback.setValue(true))
                .addOnFailureListener(e -> callback.setValue(false));
    }

    // ==================== ATTENDANCE COLLECTION CRUD ====================

    public void saveAttendanceRecords(List<AttendanceRecord> records, MutableLiveData<Boolean> callback) {
        final int[] completed = {0};
        final boolean[] hasError = {false};
        int total = records.size();
        if (total == 0) {
            callback.setValue(true);
            return;
        }

        for (AttendanceRecord record : records) {
            String id = firestore.collection("attendance").document().getId();
            record.setRecordId(id);
            firestore.collection("attendance").document(id).set(record)
                    .addOnSuccessListener(aVoid -> {
                        completed[0]++;
                        if (completed[0] == total) {
                            callback.setValue(!hasError[0]);
                        }
                    })
                    .addOnFailureListener(e -> {
                        hasError[0] = true;
                        completed[0]++;
                        if (completed[0] == total) {
                            callback.setValue(false);
                        }
                    });
        }
    }

    public LiveData<List<AttendanceRecord>> getAttendanceHistory() {
        MutableLiveData<List<AttendanceRecord>> liveData = new MutableLiveData<>();
        firestore.collection("attendance").addSnapshotListener((value, error) -> {
            if (error != null) return;
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
            liveData.setValue(list);
        });
        return liveData;
    }

    // ==================== REPORTS COLLECTION CRUD ====================

    public void saveReportMetadata(String reportTitle, String reportDate, String fileUri, MutableLiveData<Boolean> callback) {
        String id = firestore.collection("reports").document().getId();
        Map<String, Object> report = new HashMap<>();
        report.put("reportId", id);
        report.put("title", reportTitle);
        report.put("date", reportDate);
        report.put("fileUri", fileUri);

        firestore.collection("reports").document(id).set(report)
                .addOnSuccessListener(aVoid -> callback.setValue(true))
                .addOnFailureListener(e -> callback.setValue(false));
    }

    public LiveData<List<Map<String, Object>>> getReportsList() {
        MutableLiveData<List<Map<String, Object>>> liveData = new MutableLiveData<>();
        firestore.collection("reports").addSnapshotListener((value, error) -> {
            if (error != null) return;
            List<Map<String, Object>> list = new ArrayList<>();
            if (value != null) {
                for (QueryDocumentSnapshot doc : value) {
                    Map<String, Object> data = doc.getData();
                    list.add(data);
                }
            }
            liveData.setValue(list);
        });
        return liveData;
    }
}
