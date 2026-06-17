package org.example.ra.JB_IOC_260310_IOCBE102.model;

public class Admin {
    private final int id;
    private final String username;
    private final String fullName;

    public Admin(int id, String username, String fullName) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }
}
