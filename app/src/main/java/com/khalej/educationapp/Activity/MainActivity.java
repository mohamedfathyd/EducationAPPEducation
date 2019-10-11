package com.khalej.educationapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.khalej.educationapp.Adapter.RecyclerAdapter_first;
import com.khalej.educationapp.Adapter.RecyclerAdapter_first_annonce;
import com.khalej.educationapp.R;
import com.khalej.educationapp.model.Apiclient_home;
import com.khalej.educationapp.model.apiinterface_home;
import com.khalej.educationapp.model.contact_annonce;
import com.khalej.educationapp.model.contact_annonce_realm;
import com.khalej.educationapp.model.contact_home;
import com.khalej.educationapp.model.contact_home_realm;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import me.anwarshahriar.calligrapher.Calligrapher;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView,recyclerView2;
    private RecyclerView.LayoutManager layoutManager;
    CountDownTimer countDownTimer;
    private RecyclerAdapter_first recyclerAdapter;
    private RecyclerAdapter_first_annonce recyclerAdapter_annonce;
    private List<contact_home> contactList;
    private List<contact_annonce> contactList_annonce;
    private apiinterface_home apiinterface;
    int x=0;
    int y=0;
    ProgressBar progressBar;
    Realm realm;
    ImageView add;
    TextView textView;
    private SharedPreferences sharedpref;
    Typeface myTypeface;
    private SharedPreferences.Editor edt;
    TextView points ,whous,callus,aramix,bared ,myorder;

ImageView car;
    TextView textCartItemCount;
    int mCartItemCount = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "Droid.ttf", true);


        realm = Realm.getDefaultInstance();
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        this.setTitle("");
        bared=findViewById(R.id.bared);
        aramix=findViewById(R.id.aramix);
        callus=findViewById(R.id.callus);
        whous=findViewById(R.id.whous);
        myorder=findViewById(R.id.myorder);
        myorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Orders.class));
            }
        });
        bared.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Bared.class));
            }
        });
        aramix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Aramex.class));
            }
        });
        callus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                String url = "https://api.whatsapp.com/send?phone="+"+966 50 736 9231";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);}
                catch( Exception e){
                    Toast.makeText(MainActivity.this, "غير متاحه الأن عاود المحاولة لاحقا " ,Toast.LENGTH_LONG).show();
                }
//                String dial = "tel:" + "+966 50 736 9231";
//                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(dial)));
            }
        });
        whous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Whous.class));
            }
        });
        sharedpref = getSharedPreferences("Education", Context.MODE_PRIVATE);
        edt = sharedpref.edit();
        recyclerView=(RecyclerView)findViewById(R.id.recyclerview);
        progressBar=(ProgressBar)findViewById(R.id.progressBar_subject);
        progressBar.setVisibility(View.VISIBLE);
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView2=(RecyclerView)findViewById(R.id.recyclerview2);
        layoutManager = new GridLayoutManager(this, 1);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
        recyclerView2.setHasFixedSize(true);

        if (!isNetworkAvailable(MainActivity.this)) {
            showdata();
            showdata_annonce();
            progressBar.setVisibility(View.GONE);
        } else {
            fetchInfo();
            fetchInfo_annonce();
        }

        try {


            final int counter=100*5000;

            countDownTimer=   new CountDownTimer(counter, 5000) {

                public void onTick(long millisUntilFinished) {
                    // Toast.makeText(MainActivity.this , ""+(millisUntilFinished / 1000),Toast.LENGTH_LONG).show();
                    recyclerView2.smoothScrollToPosition(y);
                    y++;
                    if(y>x){
                        y=0;
                    }
                    //here you can have your logic to set text to edittext

                }

                public void onFinish() {

                }

            }.start();}
        catch (Exception e){}


    }


    public void fetchInfo(){
        apiinterface= Apiclient_home.getapiClient().create(apiinterface_home.class);
        Call<List<contact_home>> call = apiinterface.getcontacts_first();
        call.enqueue(new Callback<List<contact_home>>() {
            @Override
            public void onResponse(Call<List<contact_home>> call, Response<List<contact_home>> response) {
                contactList = response.body();
                progressBar.setVisibility(View.GONE);
                WriteTodatabase(contactList);

            }

            @Override
            public void onFailure(Call<List<contact_home>> call, Throwable t) {

            }
        });
    }
    public void WriteTodatabase(List<contact_home> contactList){
//             realm.delete(subject_content_realm.class);
        // Create a new object
        deletedata();
        if(!(contactList==null)){
            for(int i=0;i<contactList.size();i++){
                realm.beginTransaction();
                contact_home_realm images = realm.createObject(contact_home_realm.class);
                images.setId(contactList.get(i).getId());
                images.setname(contactList.get(i).getname());
                images.setImg(contactList.get(i).getImg());
                realm.commitTransaction();
            }
            showdata();}

    }
    public void showdata(){

        RealmResults<contact_home_realm> content_realms = realm.where(contact_home_realm.class).findAll();
        if (content_realms.isEmpty() || content_realms.equals(null)) {

        } else {    // realm.beginTransaction();
            List<contact_home_realm> result = content_realms;


            recyclerAdapter=new RecyclerAdapter_first(MainActivity.this,result);
            recyclerView.setAdapter(recyclerAdapter);
        }
    }

    public void deletedata(){
        realm.beginTransaction();
        RealmResults<contact_home_realm> content_realms=realm.where(contact_home_realm.class).findAll();
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
    public void fetchInfo_annonce(){
        apiinterface= Apiclient_home.getapiClient().create(apiinterface_home.class);
        Call<List<contact_annonce>> call = apiinterface.getcontacts_annonce();
        call.enqueue(new Callback<List<contact_annonce>>() {
            @Override
            public void onResponse(Call<List<contact_annonce>> call, Response<List<contact_annonce>> response) {
                contactList_annonce = response.body();
                progressBar.setVisibility(View.GONE);
                WriteTodatabase_annonce(contactList_annonce);

            }

            @Override
            public void onFailure(Call<List<contact_annonce>> call, Throwable t) {

            }
        });
    }
    public void WriteTodatabase_annonce(List<contact_annonce> contactList_){
//             realm.delete(subject_content_realm.class);
        // Create a new object
        deletedata_annonce();
        if(!(contactList_==null)){
            for(int i=0;i<contactList_.size();i++){
                realm.beginTransaction();
                contact_annonce_realm images = realm.createObject(contact_annonce_realm.class);

                images.setImage(contactList_.get(i).getImage());

                realm.commitTransaction();
            }
            showdata_annonce();}
    }
    public void showdata_annonce(){

        RealmResults<contact_annonce_realm> content_realms = realm.where(contact_annonce_realm.class).findAll();
        if (content_realms.isEmpty() || content_realms.equals(null)) {

        } else {    // realm.beginTransaction();
            List<contact_annonce_realm> result2 = content_realms;

            x=result2.size();
            recyclerAdapter_annonce=new RecyclerAdapter_first_annonce(MainActivity.this,result2,recyclerView2);
            recyclerView2.setAdapter(recyclerAdapter_annonce);
        }
    }

    public void deletedata_annonce(){
        realm.beginTransaction();
        RealmResults<contact_annonce_realm> content_realms=realm.where(contact_annonce_realm.class).findAll();
        content_realms.deleteAllFromRealm();
        realm.commitTransaction();
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
                startActivity(new Intent(MainActivity.this,Order_Details.class));
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
