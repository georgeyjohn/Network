package com.ip.barcodescanner.entity;

/**
 * Created by deepak on 19/8/15.
 */
public class PostProductInformation {
    private String gTIN;

    private String additionalIdentificationNumber;

    private String userEmailAddress;

    private String serialNumber;

    private double longitude;

    private double latitude;

    public String getGTIN() {
        return gTIN;
    }

    public void setGTIN(String gTIN) {
        this.gTIN = gTIN;
    }

    public String getAdditionalIdentificationNumber() {
        return additionalIdentificationNumber;
    }

    public void setAdditionalIdentificationNumber(String additionalIdentificationNumber) {
        this.additionalIdentificationNumber = additionalIdentificationNumber;
    }

    public String getUserEmailAddress() {
        return userEmailAddress;
    }

    public void setUserEmailAddress(String userEmailAddress) {
        this.userEmailAddress = userEmailAddress;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "ClassPojo [gTIN = " + gTIN + ", additionalIdentificationNumber = " + additionalIdentificationNumber + ", userEmailAddress = " + userEmailAddress + ", serialNumber = " + serialNumber + ", longitude = " + longitude + ", latitude = " + latitude + "]";
    }
}

