package com.example.rungta.ui.attendance;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.rungta.data.model.AttendanceRecord;
import com.example.rungta.data.model.Student;
import com.example.rungta.data.repository.AttendanceRepository;

import java.util.List;

public class AttendanceViewModel extends ViewModel {
    private final AttendanceRepository repository;
    private final LiveData<List<AttendanceRecord>> historyLiveData;

    public AttendanceViewModel() {
        repository = new AttendanceRepository();
        historyLiveData = repository.getAttendanceHistory();
    }

    public LiveData<List<Student>> getStudentsForAttendance() {
        return repository.getStudentsForAttendance();
    }

    public LiveData<List<AttendanceRecord>> getAttendanceHistory() {
        return historyLiveData;
    }

    public void saveAttendanceRecords(List<AttendanceRecord> records, MutableLiveData<Boolean> resultLiveData) {
        repository.saveAttendanceRecords(records, resultLiveData);
    }
}
