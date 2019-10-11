package com.khalej.educationapp.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.khalej.educationapp.model.Apiclient_home;
import com.khalej.educationapp.model.apiinterface_home;
import com.khalej.educationapp.model.contact_userinfo;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class login_ {
    private List<contact_userinfo> contactList;
    private apiinterface_home apiinterface;
    private SharedPreferences sharedpref;
    private SharedPreferences.Editor edt;
    ProgressDialog progressDialog;
    public void fetchInfo(final Context context, String phone, String password){
        sharedpref = context.getSharedPreferences("Education", Context.MODE_PRIVATE);
        edt = sharedpref.edit();

        progressDialog = ProgressDialog.show(context,"جاري تسجيل الدخول","Please wait...",false,false);
        progressDialog.show();
        apiinterface= Apiclient_home.getapiClient().create(apiinterface_home.class);
        Call<List<contact_userinfo>> call= apiinterface.getcontacts_login(phone,
               password);
        call.enqueue(new Callback<List<contact_userinfo>>() {
            @Override
            public void onResponse(Call<List<contact_userinfo>> call, Response<List<contact_userinfo>> response) {
                contactList = response.body();
                progressDialog.dismiss();
            try{
                AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(context);
                dlgAlert.setMessage("تم تسجيل الدخول بنجاح");
                dlgAlert.setTitle("ManoAd");
                dlgAlert.setPositiveButton("OK", null);
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
                edt.putInt("id",contactList.get(0).getId());
                edt.putFloat("totalprice",0);
                edt.putString("name",contactList.get(0).getName());
                edt.putString("phone",contactList.get(0).getPhone());
                edt.putString("address",contactList.get(0).getmaddress());
                edt.putString("password",contactList.get(0).getPassword());
                edt.putString("points",contactList.get(0).getPoints());
                edt.putString("country",contactList.get(0).getCountry());
                edt.putString("age", String.valueOf(contactList.get(0).getAge()));
                edt.putString("remember","yes");
                edt.apply();
                context.startActivity(new Intent(context,MainActivity.class));}
                catch (Exception e){

                }
            }

            @Override
            public void onFailure(Call<List<contact_userinfo>> call, Throwable t) {
                Toast.makeText(context,t.toString(),Toast.LENGTH_LONG).show();
            }
        });
    }

}
