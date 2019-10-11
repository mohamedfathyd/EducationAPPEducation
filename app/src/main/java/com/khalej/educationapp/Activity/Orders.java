package com.khalej.educationapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.khalej.educationapp.Adapter.RecyclerAdapter_recervations;
import com.khalej.educationapp.R;
import com.khalej.educationapp.model.Apiclient_home;
import com.khalej.educationapp.model.apiinterface_home;
import com.khalej.educationapp.model.contact_order;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.VISIBLE;

public class Orders extends AppCompatActivity {
    Toolbar toolbar;
    ProgressBar progressBar;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerAdapter_recervations recyclerAdapter_secondry;
    private List<contact_order> contactList;
    private apiinterface_home apiinterface;
    int id = 0;
    String name;
    int user_id;
    int sec_id;
    private SharedPreferences sharedpref;
    private SharedPreferences.Editor edt;
    Typeface myTypeface;
    Intent intent;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_subject);
        textView = (TextView) findViewById(R.id.toolbar_title);
        progressBar.setVisibility(VISIBLE);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        intent = getIntent();



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

        recyclerView = (RecyclerView) findViewById(R.id.review);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_subject);
        progressBar.setVisibility(VISIBLE);
        layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        fetchInfo();
    }

    public void fetchInfo() {
        sharedpref= getSharedPreferences("Education", Context.MODE_PRIVATE);
        apiinterface = Apiclient_home.getapiClient().create(apiinterface_home.class);
        Call<List<contact_order>> call = apiinterface.get_all_neworders(sharedpref.getString("name","").trim()
                ,sharedpref.getString("address","").trim(),sharedpref.getString("phone","").trim());
        call.enqueue(new Callback<List<contact_order>>() {
            @Override
            public void onResponse(Call<List<contact_order>> call, Response<List<contact_order>> response) {
                contactList = response.body();
                progressBar.setVisibility(View.GONE);
                recyclerAdapter_secondry = new RecyclerAdapter_recervations(Orders.this, contactList);
                recyclerView.setAdapter(recyclerAdapter_secondry);


            }

            @Override
            public void onFailure(Call<List<contact_order>> call, Throwable t) {

            }
        });
    }}
