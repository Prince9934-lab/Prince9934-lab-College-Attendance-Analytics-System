package com.example.rungta.ui.student;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rungta.data.model.Student;
import com.example.rungta.databinding.ItemStudentBinding;

import java.util.ArrayList;
import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {
    private List<Student> studentList;
    private List<Student> studentListFull;
    private final OnStudentActionListener actionListener;

    public interface OnStudentActionListener {
        void onEdit(Student student);
        void onDelete(Student student);
        void onView(Student student);
    }

    public StudentAdapter(List<Student> studentList, OnStudentActionListener actionListener) {
        this.studentList = studentList != null ? studentList : new ArrayList<>();
        this.studentListFull = new ArrayList<>(this.studentList);
        this.actionListener = actionListener;
    }

    public void setStudentList(List<Student> studentList) {
        this.studentList = studentList != null ? studentList : new ArrayList<>();
        this.studentListFull = new ArrayList<>(this.studentList);
        notifyDataSetChanged();
    }

    public void filter(String query) {
        List<Student> result = new ArrayList<>();
        if (query == null || query.trim().isEmpty()) {
            result.addAll(studentListFull);
        } else {
            String filterPattern = query.toLowerCase().trim();
            for (Student item : studentListFull) {
                if ((item.getName() != null && item.getName().toLowerCase().contains(filterPattern)) ||
                    (item.getRollNo() != null && item.getRollNo().toLowerCase().contains(filterPattern)) ||
                    (item.getDepartment() != null && item.getDepartment().toLowerCase().contains(filterPattern))) {
                    result.add(item);
                }
            }
        }
        studentList = result;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemStudentBinding binding = ItemStudentBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new StudentViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student student = studentList.get(position);
        holder.binding.tvStudentName.setText(student.getName());
        holder.binding.tvRollNo.setText("Roll No: " + student.getRollNo());
        holder.binding.tvDepartment.setText("Dept: " + student.getDepartment() + " | Sem: " + student.getSemester());
        holder.binding.tvEmail.setText(student.getEmail());

        holder.binding.btnEditStudent.setOnClickListener(v -> {
            if (actionListener != null) actionListener.onEdit(student);
        });

        holder.binding.btnDeleteStudent.setOnClickListener(v -> {
            if (actionListener != null) actionListener.onDelete(student);
        });

        holder.itemView.setOnClickListener(v -> {
            if (actionListener != null) actionListener.onView(student);
        });
    }

    @Override
    public int getItemCount() {
        return studentList == null ? 0 : studentList.size();
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder {
        private final ItemStudentBinding binding;

        public StudentViewHolder(ItemStudentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
