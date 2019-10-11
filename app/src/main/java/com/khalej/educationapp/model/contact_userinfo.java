package com.khalej.educationapp.model;
import com.google.gson.annotations.SerializedName;


public class contact_userinfo {
    @SerializedName("id")
    int id;
    @SerializedName("name")
    String name;
    @SerializedName("phone")
    String phone;
    @SerializedName("address")
    String maddress;
    @SerializedName("password")
    String Password;
    @SerializedName("points")
    String Points;
    @SerializedName("governorate")
    String Country;
    @SerializedName("age")
    String Age;


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getmaddress() {
        return maddress;
    }

    public void setmaddress(String maddress) {
        this.maddress = maddress;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPoints() {
        return Points;
    }

    public void setPoints(String points) {
        Points = points;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
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


}
