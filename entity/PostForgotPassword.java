package com.ip.barcodescanner.entity;

/**
 * Created by Georgey on 06/04/2016.
 */
public class PostForgotPassword {
    private String Username;

    public String getUsername() {
        return Username;
    }

    public void setUsername(String Username) {
        this.Username = Username;
    }

    @Override
    public String toString() {
        return "ClassPojo [Username = " + Username + "]";
    }
}
