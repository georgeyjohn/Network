package com.ip.barcodescanner.entity;

/**
 * Created by deepak on 18/8/15.
 */
public class PostLogin
{
    private String Password;

    private String Username;

    public String getPassword ()
    {
        return Password;
    }

    public void setPassword (String Password)
    {
        this.Password = Password;
    }

    public String getUsername ()
    {
        return Username;
    }

    public void setUsername (String Username)
    {
        this.Username = Username;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Password = "+Password+", Username = "+Username+"]";
    }
}