package com.example.rungta.ui.attendance;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rungta.data.model.AttendanceRecord;
import com.example.rungta.databinding.ItemAttendanceHistoryBinding;

import java.util.List;

public class AttendanceHistoryAdapter extends RecyclerView.Adapter<AttendanceHistoryAdapter.HistoryViewHolder> {
    private List<AttendanceRecord> recordList;

    public AttendanceHistoryAdapter(List<AttendanceRecord> recordList) {
        this.recordList = recordList;
    }

    public void setRecordList(List<AttendanceRecord> recordList) {
        this.recordList = recordList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAttendanceHistoryBinding binding = ItemAttendanceHistoryBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new HistoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        AttendanceRecord record = recordList.get(position);
        holder.binding.tvStudentName.setText(record.getStudentName() + " (" + record.getRollNo() + ")");
        holder.binding.tvSubject.setText("Subject: " + record.getSubject());
        holder.binding.tvDate.setText("Date: " + record.getDate());
        holder.binding.tvStatus.setText(record.isPresent() ? "Present" : "Absent");
        holder.binding.tvStatus.setTextColor(record.isPresent() ? 0xFF2E7D32 : 0xFFC62828);
    }

    @Override
    public int getItemCount() {
        return recordList == null ? 0 : recordList.size();
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
        private final ItemAttendanceHistoryBinding binding;

        public HistoryViewHolder(ItemAttendanceHistoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
