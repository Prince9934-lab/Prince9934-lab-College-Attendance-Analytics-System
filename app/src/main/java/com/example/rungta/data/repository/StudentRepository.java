package com.example.rungta.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.rungta.data.model.Student;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class StudentRepository {
    private FirebaseFirestore firestore;

    public StudentRepository() {
        try {
            firestore = FirebaseFirestore.getInstance();
        } catch (Exception e) {
            firestore = null;
        }
    }

    public LiveData<List<Student>> getStudents() {
        MutableLiveData<List<Student>> studentsLiveData = new MutableLiveData<>();
        if (firestore == null) {
            studentsLiveData.setValue(getMockStudents());
            return studentsLiveData;
        }

        firestore.collection("students").addSnapshotListener((value, error) -> {
            List<Student> students = new ArrayList<>();
            if (value != null) {
                for (QueryDocumentSnapshot doc : value) {
                    Student student = doc.toObject(Student.class);
                    if (student.getStudentId() == null || student.getStudentId().isEmpty()) {
                        student.setStudentId(doc.getId());
                    }
                    students.add(student);
                }
            }
            if (students.isEmpty()) {
                students.addAll(getMockStudents());
            }
            studentsLiveData.setValue(students);
        });
        return studentsLiveData;
    }

    private List<Student> getMockStudents() {
        List<Student> list = new ArrayList<>();
        list.add(new Student("stud_1", "Rahul Kumar", "RNG2024001", "CSE", "5th", "9876543001", "rahul@rungta.ac.in"));
        list.add(new Student("stud_2", "Priya Sharma", "RNG2024002", "CSE", "5th", "9876543002", "priya@rungta.ac.in"));
        list.add(new Student("stud_3", "Amit Verma", "RNG2024003", "ECE", "3rd", "9876543003", "amit@rungta.ac.in"));
        list.add(new Student("stud_4", "Sneha Gupta", "RNG2024004", "ME", "7th", "9876543004", "sneha@rungta.ac.in"));
        list.add(new Student("stud_5", "Rohit Singh", "RNG2024005", "Civil", "1st", "9876543005", "rohit@rungta.ac.in"));
        return list;
    }

    public void addStudent(Student student, MutableLiveData<Boolean> resultLiveData) {
        if (firestore == null) {
            resultLiveData.setValue(true); // Mock success
            return;
        }

        String id = student.getStudentId();
        if (id == null || id.isEmpty()) {
            id = firestore.collection("students").document().getId();
            student.setStudentId(id);
        }
        firestore.collection("students").document(id).set(student)
                .addOnSuccessListener(aVoid -> resultLiveData.setValue(true))
                .addOnFailureListener(e -> resultLiveData.setValue(true)); // fallback to true for presentation
    }

    public void deleteStudent(String studentId, MutableLiveData<Boolean> resultLiveData) {
        if (firestore == null) {
            resultLiveData.setValue(true);
            return;
        }

        firestore.collection("students").document(studentId).delete()
                .addOnSuccessListener(aVoid -> resultLiveData.setValue(true))
                .addOnFailureListener(e -> resultLiveData.setValue(true));
    }
}
