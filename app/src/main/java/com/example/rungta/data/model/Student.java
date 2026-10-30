package com.example.rungta.data.model;

public class Student {
    private String studentId;
    private String name;
    private String rollNo;
    private String department;
    private String semester;
    private String phone;
    private String email;

    public Student() {}

    public Student(String studentId, String name, String rollNo, String department, String semester, String phone, String email) {
        this.studentId = studentId;
        this.name = name;
        this.rollNo = rollNo;
        this.department = department;
        this.semester = semester;
        this.phone = phone;
        this.email = email;
    }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRollNo() { return rollNo; }
    public void setRollNo(String rollNo) { this.rollNo = rollNo; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
