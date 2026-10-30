package com.example.rungta.data.model;

public class AttendanceRecord {
    private String recordId;
    private String studentId;
    private String studentName;
    private String rollNo;
    private String subject;
    private String date;
    private boolean isPresent;
    private String facultyId;

    public AttendanceRecord() {}

    public AttendanceRecord(String recordId, String studentId, String studentName, String rollNo, String subject, String date, boolean isPresent, String facultyId) {
        this.recordId = recordId;
        this.studentId = studentId;
        this.studentName = studentName;
        this.rollNo = rollNo;
        this.subject = subject;
        this.date = date;
        this.isPresent = isPresent;
        this.facultyId = facultyId;
    }

    public String getRecordId() { return recordId; }
    public void setRecordId(String recordId) { this.recordId = recordId; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getRollNo() { return rollNo; }
    public void setRollNo(String rollNo) { this.rollNo = rollNo; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public boolean isPresent() { return isPresent; }
    public void setPresent(boolean present) { isPresent = present; }

    public String getFacultyId() { return facultyId; }
    public void setFacultyId(String facultyId) { this.facultyId = facultyId; }
}
