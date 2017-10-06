package com.ip.barcodescanner.entity;

/**
 * Created by Georgey on 06/04/2016.
 */
public class PostRegister {
    private String Phone;

    private String State;

    private String Address2;

    private String Address1;

    private String Fax;

    private String Password;

    private String Username;

    private String Lastname;

    private String Firstname;

    private String Zip;

    private String Country;

    private String City;

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String Phone) {
        this.Phone = Phone;
    }

    public String getState() {
        return State;
    }

    public void setState(String State) {
        this.State = State;
    }

    public String getAddress2() {
        return Address2;
    }

    public void setAddress2(String Address2) {
        this.Address2 = Address2;
    }

    public String getAddress1() {
        return Address1;
    }

    public void setAddress1(String Address1) {
        this.Address1 = Address1;
    }

    public String getFax() {
        return Fax;
    }

    public void setFax(String Fax) {
        this.Fax = Fax;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String Username) {
        this.Username = Username;
    }

    public String getLastname() {
        return Lastname;
    }

    public void setLastname(String Lastname) {
        this.Lastname = Lastname;
    }

    public String getFirstname() {
        return Firstname;
    }

    public void setFirstname(String Firstname) {
        this.Firstname = Firstname;
    }

    public String getZip() {
        return Zip;
    }

    public void setZip(String Zip) {
        this.Zip = Zip;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String Country) {
        this.Country = Country;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String City) {
        this.City = City;
    }

    @Override
    public String toString() {
        return "ClassPojo [Phone = " + Phone + ", State = " + State + ", Address2 = " + Address2 + ", Address1 = " + Address1 + ", Fax = " + Fax + ", Password = " + Password + ", Username = " + Username + ", Lastname = " + Lastname + ", Firstname = " + Firstname + ", Zip = " + Zip + ", Country = " + Country + ", City = " + City + "]";
    }
}
