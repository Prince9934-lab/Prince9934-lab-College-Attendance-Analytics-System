package com.example.rungta.ui.notifications;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rungta.data.model.Notification;
import com.example.rungta.databinding.ItemNotificationBinding;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {
    private List<Notification> notificationList;

    public NotificationAdapter(List<Notification> notificationList) {
        this.notificationList = notificationList;
    }

    public void setNotificationList(List<Notification> notificationList) {
        this.notificationList = notificationList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemNotificationBinding binding = ItemNotificationBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new NotificationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification notification = notificationList.get(position);
        holder.binding.tvTitle.setText(notification.getTitle());
        holder.binding.tvMessage.setText(notification.getMessage());
        holder.binding.tvTimestamp.setText(notification.getTimestamp());
    }

    @Override
    public int getItemCount() {
        return notificationList == null ? 0 : notificationList.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        private final ItemNotificationBinding binding;

        public NotificationViewHolder(ItemNotificationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
