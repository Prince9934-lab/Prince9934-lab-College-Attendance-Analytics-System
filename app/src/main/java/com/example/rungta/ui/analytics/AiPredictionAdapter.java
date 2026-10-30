package com.example.rungta.ui.analytics;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rungta.data.model.StudentRiskPrediction;
import com.example.rungta.databinding.ItemAiPredictionBinding;

import java.util.List;
import java.util.Locale;

public class AiPredictionAdapter extends RecyclerView.Adapter<AiPredictionAdapter.PredictionViewHolder> {
    private List<StudentRiskPrediction> predictionList;

    public AiPredictionAdapter(List<StudentRiskPrediction> predictionList) {
        this.predictionList = predictionList;
    }

    public void setPredictionList(List<StudentRiskPrediction> predictionList) {
        this.predictionList = predictionList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PredictionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAiPredictionBinding binding = ItemAiPredictionBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new PredictionViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PredictionViewHolder holder, int position) {
        StudentRiskPrediction result = predictionList.get(position);
        holder.binding.tvStudentName.setText(result.getStudent().getName());
        holder.binding.tvRollNo.setText("Roll No: " + result.getStudent().getRollNo() + " | Dept: " + result.getStudent().getDepartment());
        
        holder.binding.tvPrevMonthPct.setText(String.format(Locale.getDefault(), "%.0f%%", result.getPreviousMonthPercentage()));
        holder.binding.tvLastMonthPct.setText(String.format(Locale.getDefault(), "%.0f%%", result.getLastMonthPercentage()));
        holder.binding.tvCurrentPct.setText(String.format(Locale.getDefault(), "%.0f%%", result.getCurrentPercentage()));
        holder.binding.tvPredictedPct.setText(String.format(Locale.getDefault(), "%.0f%%", result.getPredictedPercentage()));

        holder.binding.tvRiskBadge.setText(result.getRiskLevel());
        holder.binding.tvReason.setText(result.getExplanationReason());
        holder.binding.tvRecommendation.setText(result.getRecommendation());

        int predictedInt = (int) Math.max(0, Math.min(100, result.getPredictedPercentage()));
        holder.binding.progressPredicted.setProgress(predictedInt);

        String risk = result.getRiskLevel();
        if ("HIGH RISK".equals(risk)) {
            holder.binding.tvRiskBadge.setTextColor(Color.parseColor("#F44336"));
            holder.binding.tvRiskBadge.setChipBackgroundColorResource(android.R.color.transparent);
            holder.binding.tvPredictedPct.setTextColor(Color.parseColor("#F44336"));
            holder.binding.progressPredicted.setIndicatorColor(Color.parseColor("#F44336"));
        } else if ("MEDIUM RISK".equals(risk)) {
            holder.binding.tvRiskBadge.setTextColor(Color.parseColor("#FF9800"));
            holder.binding.tvRiskBadge.setChipBackgroundColorResource(android.R.color.transparent);
            holder.binding.tvPredictedPct.setTextColor(Color.parseColor("#FF9800"));
            holder.binding.progressPredicted.setIndicatorColor(Color.parseColor("#FF9800"));
        } else {
            holder.binding.tvRiskBadge.setTextColor(Color.parseColor("#4CAF50"));
            holder.binding.tvRiskBadge.setChipBackgroundColorResource(android.R.color.transparent);
            holder.binding.tvPredictedPct.setTextColor(Color.parseColor("#4CAF50"));
            holder.binding.progressPredicted.setIndicatorColor(Color.parseColor("#4CAF50"));
        }
    }

    @Override
    public int getItemCount() {
        return predictionList == null ? 0 : predictionList.size();
    }

    public static class PredictionViewHolder extends RecyclerView.ViewHolder {
        private final ItemAiPredictionBinding binding;

        public PredictionViewHolder(ItemAiPredictionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
