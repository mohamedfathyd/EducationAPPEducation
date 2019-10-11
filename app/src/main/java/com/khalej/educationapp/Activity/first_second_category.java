package com.khalej.educationapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.khalej.educationapp.Adapter.RecyclerAdapter;
import com.khalej.educationapp.R;
import com.khalej.educationapp.model.Apiclient_home;
import com.khalej.educationapp.model.apiinterface_home;
import com.khalej.educationapp.model.contact_second_home;
import com.khalej.educationapp.model.contact_second_home_realm;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import me.anwarshahriar.calligrapher.Calligrapher;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class first_second_category extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerAdapter recyclerAdapter;
    private List<contact_second_home> contactList;
    private apiinterface_home apiinterface;
    ProgressBar progressBar;
    ImageView car;
    Realm realm;
    TextView textView;
    private SharedPreferences sharedpref;
    Typeface myTypeface;
    Intent intent;
    int secondry_id;
    String name;
    TextView textCartItemCount;
    int mCartItemCount = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_second_category);
        realm = Realm.getDefaultInstance();
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        this.setTitle("");
        intent=getIntent();
        secondry_id=intent.getIntExtra("id",0);
        name=intent.getStringExtra("name");
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "Droid.ttf", true);
        sharedpref = getSharedPreferences("Education", Context.MODE_PRIVATE);


        recyclerView=(RecyclerView)findViewById(R.id.recyclerview);
        progressBar=(ProgressBar)findViewById(R.id.progressBar_subject);
        progressBar.setVisibility(View.VISIBLE);
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

        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        if (!isNetworkAvailable(first_second_category.this)) {
            showdata();
            progressBar.setVisibility(View.GONE);
        } else {
            fetchInfo();
        }


    }

    public void fetchInfo(){
        apiinterface= Apiclient_home.getapiClient().create(apiinterface_home.class);
        Call<List<contact_second_home>> call = apiinterface.getcontacts(secondry_id);
        call.enqueue(new Callback<List<contact_second_home>>() {
            @Override
            public void onResponse(Call<List<contact_second_home>> call, Response<List<contact_second_home>> response) {
                contactList = response.body();
                progressBar.setVisibility(View.GONE);
                WriteTodatabase(contactList);

            }

            @Override
            public void onFailure(Call<List<contact_second_home>> call, Throwable t) {

            }
        });
    }
    public void WriteTodatabase(List<contact_second_home> contactList){
//             realm.delete(subject_content_realm.class);
        // Create a new object
        deletedata();
        if(!(contactList==null)){
            for(int i=0;i<contactList.size();i++){
                realm.beginTransaction();
                contact_second_home_realm images = realm.createObject(contact_second_home_realm.class);
                images.setId(contactList.get(i).getId());
                images.setName(contactList.get(i).getName());
                images.setImg(contactList.get(i).getImg());

                images.setPrice(contactList.get(i).getPrice());
                images.setDescription(contactList.get(i).getDescription());
                images.setNameca(name);
                realm.commitTransaction();
            }
            showdata();}
        else{}
    }
    public void showdata(){

        RealmResults<contact_second_home_realm> content_realms = realm.where(contact_second_home_realm.class).equalTo("nameca", name).findAll();
        if (content_realms.isEmpty() || content_realms.equals(null)) {

        } else {    // realm.beginTransaction();
            List<contact_second_home_realm> result = content_realms;


            recyclerAdapter=new RecyclerAdapter(first_second_category.this,result);
            recyclerView.setAdapter(recyclerAdapter);
        }
    }

    public void deletedata(){
        realm.beginTransaction();
        RealmResults<contact_second_home_realm> content_realms=realm.where(contact_second_home_realm.class).equalTo("nameca", name).findAll();
        content_realms.deleteAllFromRealm();
        realm.commitTransaction();
    }
    public boolean isNetworkAvailable(Context ctx) {
        if (ctx == null)
            return false;

        ConnectivityManager cm =
                (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        final MenuItem menuItem = menu.findItem(R.id.action_cart);

        View actionView = MenuItemCompat.getActionView(menuItem);
        textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);

        setupBadge();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_cart: {
                // Do something
                startActivity(new Intent(first_second_category.this,Order_Details.class));
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupBadge() {
        mCartItemCount= sharedpref.getInt("number",0);
        if (textCartItemCount != null) {
            if (mCartItemCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }


}
