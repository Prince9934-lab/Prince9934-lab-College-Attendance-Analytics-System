package com.example.rungta.data.model;

public class Faculty {
    private String facultyId;
    private String name;
    private String email;
    private String department;
    private String subject;

    public Faculty() {}

    public Faculty(String facultyId, String name, String email, String department, String subject) {
        this.facultyId = facultyId;
        this.name = name;
        this.email = email;
        this.department = department;
        this.subject = subject;
    }

    public String getFacultyId() { return facultyId; }
    public void setFacultyId(String facultyId) { this.facultyId = facultyId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
}
