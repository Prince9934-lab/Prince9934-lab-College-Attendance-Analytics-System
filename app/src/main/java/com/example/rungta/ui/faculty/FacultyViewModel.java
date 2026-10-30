package com.example.rungta.ui.faculty;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.rungta.data.model.Faculty;
import com.example.rungta.data.repository.FacultyRepository;

import java.util.List;

public class FacultyViewModel extends ViewModel {
    private final FacultyRepository repository;
    private final LiveData<List<Faculty>> facultyLiveData;

    public FacultyViewModel() {
        repository = new FacultyRepository();
        facultyLiveData = repository.getFacultyList();
    }

    public LiveData<List<Faculty>> getFacultyList() {
        return facultyLiveData;
    }

    public void addFaculty(Faculty faculty, MutableLiveData<Boolean> resultLiveData) {
        repository.addFaculty(faculty, resultLiveData);
    }

    public void deleteFaculty(String facultyId, MutableLiveData<Boolean> resultLiveData) {
        repository.deleteFaculty(facultyId, resultLiveData);
    }
}
