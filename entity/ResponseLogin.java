package com.ip.barcodescanner.entity;

/**
 * Created by deepak on 18/8/15.
 */
public class ResponseLogin
{
    private String result;

    private String sessionId;

    private String userId;

    public String getResult ()
    {
        return result;
    }

    public void setResult (String result)
    {
        this.result = result;
    }

    public String getSessionId ()
    {
        return sessionId;
    }

    public void setSessionId (String sessionId)
    {
        this.sessionId = sessionId;
    }

    public String getUserId ()
    {
        return userId;
    }

    public void setUserId (String userId)
    {
        this.userId = userId;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [result = "+result+", sessionId = "+sessionId+", userId = "+userId+"]";
    }
}
