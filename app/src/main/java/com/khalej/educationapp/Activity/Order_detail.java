package com.khalej.educationapp.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.khalej.educationapp.R;
import com.khalej.educationapp.model.apiinterface_home;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Order_detail extends AppCompatActivity {
    TextView name, phone, address, details, getfinal, charge, price;
    Intent intent;
    Toolbar toolbar;
    private apiinterface_home apiinterface;
    private SharedPreferences sharedpref;
    private SharedPreferences.Editor edt;
    int id, idd;
    ProgressDialog progressDialog;

    String provider_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);


        toolbar = (Toolbar) findViewById(R.id.app_bar);
        this.setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_black_24dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );


        name = findViewById(R.id.name);
        address = findViewById(R.id.address);
        phone = findViewById(R.id.phone);

        details = findViewById(R.id.details);
        charge = findViewById(R.id.charge);
        price = findViewById(R.id.price);
        intent = getIntent();
        charge.setText(intent.getStringExtra("charge"));
        name.setText(intent.getStringExtra("name"));
        phone.setText(intent.getStringExtra("phone"));
        address.setText(intent.getStringExtra("address"));
        details.setText(intent.getStringExtra("details"));
        price.setText(intent.getStringExtra("price"));
        idd = intent.getIntExtra("id", 0);


    }
}