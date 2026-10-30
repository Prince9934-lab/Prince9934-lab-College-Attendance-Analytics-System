package com.example.rungta.ui.student;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.rungta.data.model.Student;
import com.example.rungta.data.repository.StudentRepository;

import java.util.List;

public class StudentViewModel extends ViewModel {
    private final StudentRepository repository;
    private final LiveData<List<Student>> studentsLiveData;

    public StudentViewModel() {
        repository = new StudentRepository();
        studentsLiveData = repository.getStudents();
    }

    public LiveData<List<Student>> getStudents() {
        return studentsLiveData;
    }

    public void addStudent(Student student, MutableLiveData<Boolean> resultLiveData) {
        repository.addStudent(student, resultLiveData);
    }

    public void deleteStudent(String studentId, MutableLiveData<Boolean> resultLiveData) {
        repository.deleteStudent(studentId, resultLiveData);
    }
}
