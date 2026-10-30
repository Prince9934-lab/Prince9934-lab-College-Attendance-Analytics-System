package com.example.rungta.ui.faculty;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rungta.data.model.Faculty;
import com.example.rungta.databinding.ItemFacultyBinding;

import java.util.List;

public class FacultyAdapter extends RecyclerView.Adapter<FacultyAdapter.FacultyViewHolder> {
    private List<Faculty> facultyList;
    private final OnFacultyDeleteListener deleteListener;

    public interface OnFacultyDeleteListener {
        void onDelete(Faculty faculty);
    }

    public FacultyAdapter(List<Faculty> facultyList, OnFacultyDeleteListener deleteListener) {
        this.facultyList = facultyList;
        this.deleteListener = deleteListener;
    }

    public void setFacultyList(List<Faculty> facultyList) {
        this.facultyList = facultyList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FacultyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFacultyBinding binding = ItemFacultyBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new FacultyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FacultyViewHolder holder, int position) {
        Faculty faculty = facultyList.get(position);
        holder.binding.tvFacultyName.setText(faculty.getName());
        holder.binding.tvDepartment.setText("Dept: " + faculty.getDepartment());
        holder.binding.tvSubject.setText("Subject: " + faculty.getSubject());
        holder.binding.tvEmail.setText(faculty.getEmail());

        holder.binding.btnDeleteFaculty.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onDelete(faculty);
            }
        });
    }

    @Override
    public int getItemCount() {
        return facultyList == null ? 0 : facultyList.size();
    }

    public static class FacultyViewHolder extends RecyclerView.ViewHolder {
        private final ItemFacultyBinding binding;

        public FacultyViewHolder(ItemFacultyBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
