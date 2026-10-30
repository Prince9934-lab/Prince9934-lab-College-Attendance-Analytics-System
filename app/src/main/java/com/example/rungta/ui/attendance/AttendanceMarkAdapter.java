package com.example.rungta.ui.attendance;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rungta.data.model.Student;
import com.example.rungta.databinding.ItemAttendanceMarkBinding;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttendanceMarkAdapter extends RecyclerView.Adapter<AttendanceMarkAdapter.MarkViewHolder> {
    private List<Student> studentList;
    private final Map<String, Boolean> attendanceMap = new HashMap<>();

    public AttendanceMarkAdapter(List<Student> studentList) {
        this.studentList = studentList;
        if (studentList != null) {
            for (Student s : studentList) {
                attendanceMap.put(s.getStudentId(), true); // Default present
            }
        }
    }

    public void setStudentList(List<Student> studentList) {
        this.studentList = studentList;
        attendanceMap.clear();
        if (studentList != null) {
            for (Student s : studentList) {
                attendanceMap.put(s.getStudentId(), true);
            }
        }
        notifyDataSetChanged();
    }

    public Map<String, Boolean> getAttendanceMap() {
        return attendanceMap;
    }

    @NonNull
    @Override
    public MarkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAttendanceMarkBinding binding = ItemAttendanceMarkBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new MarkViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MarkViewHolder holder, int position) {
        Student student = studentList.get(position);
        holder.binding.tvStudentName.setText(student.getName());
        holder.binding.tvRollNo.setText("Roll No: " + student.getRollNo() + " | Dept: " + student.getDepartment());

        boolean isPresent = attendanceMap.getOrDefault(student.getStudentId(), true);
        holder.binding.cbPresent.setChecked(isPresent);

        holder.binding.cbPresent.setOnCheckedChangeListener((buttonView, isChecked) -> {
            attendanceMap.put(student.getStudentId(), isChecked);
        });
    }

    @Override
    public int getItemCount() {
        return studentList == null ? 0 : studentList.size();
    }

    public static class MarkViewHolder extends RecyclerView.ViewHolder {
        private final ItemAttendanceMarkBinding binding;

        public MarkViewHolder(ItemAttendanceMarkBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
