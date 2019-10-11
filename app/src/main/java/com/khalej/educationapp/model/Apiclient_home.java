package com.khalej.educationapp.model;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Apiclient_home {
    //http://jamalah.com/
    private static final String url="http://jamalah.com/";

    private static Retrofit retrofit =null;
    public static Retrofit getapiClient(){
        if(retrofit== null){
            retrofit=new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }
}
