package com.ip.barcodescanner.entity;

/**
 * Created by Georgey on 06/04/2016.
 */
public class ResponseRegister {
    private String email;

    private String fullName;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public String toString() {
        return "ClassPojo [email = " + email + ", fullName = " + fullName + "]";
    }
}
