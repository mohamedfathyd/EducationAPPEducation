package com.khalej.educationapp.model;
import com.google.gson.annotations.SerializedName;

public class contact_annonce {
    @SerializedName("id")
    int id;

    @SerializedName("image")
    String Image;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
