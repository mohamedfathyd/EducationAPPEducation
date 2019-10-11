package com.khalej.educationapp.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class orders_realm extends RealmObject {
    @SerializedName("id")
    int id;

    @SerializedName("name")
    String name;
    @SerializedName("details")
    String details;
    @SerializedName("price")
    double price;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
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

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
