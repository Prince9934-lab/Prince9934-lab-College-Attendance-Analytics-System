package com.example.rungta.data.model;

public class Notification {
    private String notificationId;
    private String title;
    private String message;
    private String timestamp;

    public Notification() {}

    public Notification(String notificationId, String title, String message, String timestamp) {
        this.notificationId = notificationId;
        this.title = title;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getNotificationId() { return notificationId; }
    public void setNotificationId(String notificationId) { this.notificationId = notificationId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}
