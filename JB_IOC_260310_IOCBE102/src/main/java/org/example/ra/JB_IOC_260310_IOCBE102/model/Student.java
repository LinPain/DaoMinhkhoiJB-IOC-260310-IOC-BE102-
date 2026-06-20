package org.example.ra.JB_IOC_260310_IOCBE102.model;

import java.time.LocalDate;

public class Student {
    private int id;
    private String name;
    private LocalDate dob;
    private String email;
    private boolean male;
    private String phone;
    private String password;
    private LocalDate createdAt;

    public Student() {
    }

    public Student(int id, String name, LocalDate dob, String email, boolean male,
                   String phone, String password, LocalDate createdAt) {
        this.id = id;
        this.name = name;
        this.dob = dob;
        this.email = email;
        this.male = male;
        this.phone = phone;
        this.password = password;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isMale() {
        return male;
    }

    public void setMale(boolean male) {
        this.male = male;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }
}
