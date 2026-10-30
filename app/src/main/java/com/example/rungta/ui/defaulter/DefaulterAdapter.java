package com.example.rungta.ui.defaulter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rungta.databinding.ItemDefaulterBinding;

import java.util.List;

public class DefaulterAdapter extends RecyclerView.Adapter<DefaulterAdapter.DefaulterViewHolder> {
    private List<DefaulterItem> defaulterList;

    public static class DefaulterItem {
        private String studentName;
        private String rollNo;
        private String department;
        private float attendancePercentage;

        public DefaulterItem(String studentName, String rollNo, String department, float attendancePercentage) {
            this.studentName = studentName;
            this.rollNo = rollNo;
            this.department = department;
            this.attendancePercentage = attendancePercentage;
        }

        public String getStudentName() { return studentName; }
        public String getRollNo() { return rollNo; }
        public String getDepartment() { return department; }
        public float getAttendancePercentage() { return attendancePercentage; }
    }

    public DefaulterAdapter(List<DefaulterItem> defaulterList) {
        this.defaulterList = defaulterList;
    }

    public void setDefaulterList(List<DefaulterItem> defaulterList) {
        this.defaulterList = defaulterList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DefaulterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDefaulterBinding binding = ItemDefaulterBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new DefaulterViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DefaulterViewHolder holder, int position) {
        DefaulterItem item = defaulterList.get(position);
        holder.binding.tvStudentName.setText(item.getStudentName());
        holder.binding.tvRollNo.setText("Roll No: " + item.getRollNo() + " | Dept: " + item.getDepartment());
        holder.binding.tvPercentage.setText(String.format("Attendance: %.1f%%", item.getAttendancePercentage()));
    }

    @Override
    public int getItemCount() {
        return defaulterList == null ? 0 : defaulterList.size();
    }

    public static class DefaulterViewHolder extends RecyclerView.ViewHolder {
        private final ItemDefaulterBinding binding;

        public DefaulterViewHolder(ItemDefaulterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
