package com.khalej.educationapp.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.khalej.educationapp.R;
import com.khalej.educationapp.model.orders_realm;

import io.realm.Realm;
import me.anwarshahriar.calligrapher.Calligrapher;

public class Show extends AppCompatActivity {
Intent intent;
ImageView imageView,car;
String img;
TextView name,price,add,remove,num,description;
    AppCompatButton AddToCard;
    private SharedPreferences sharedpref;
    private SharedPreferences.Editor edt;
    Toolbar toolbar;
    int id;
    TextView textCartItemCount;
    int mCartItemCount = 10;
int x=1;
    Realm realm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "Droid.ttf", true);


        name=findViewById(R.id.name);
        imageView=findViewById(R.id.img);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_black_24dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Show.this,MainActivity.class));
                        finish();
                    }
                }
        );
        this.setTitle("");
        price=findViewById(R.id.price);
        num=findViewById(R.id.num);
        add=findViewById(R.id.add);

        remove=findViewById(R.id.remove);
        description=findViewById(R.id.description);
        intent=getIntent();
        name.setText(intent.getStringExtra("name"));
        img=intent.getStringExtra("image");
        price.setText(intent.getStringExtra("price") +"ريال");
        sharedpref = getSharedPreferences("Education", Context.MODE_PRIVATE);

        id=intent.getIntExtra("id",0);
        description.setText(intent.getStringExtra("description"));
        realm= Realm.getDefaultInstance();
        AddToCard=findViewById(R.id.appCompatButtonLogin);
        Glide.with(this).load(img).error(R.drawable.log).into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog settingsDialog = new Dialog(Show.this);
                settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                settingsDialog.setContentView(R.layout.image_show);
                ImageView imgg = (ImageView) settingsDialog.findViewById(R.id.img);
                Glide.with(Show.this).load(img).error(R.drawable.log).into(imgg);
                settingsDialog.show();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                x++;
                num.setText(x+"");

            }
        });
    AddToCard.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            edt = sharedpref.edit();

            float a=Float.parseFloat(intent.getStringExtra("price")) *(x);
            float p=sharedpref.getFloat("totalprice",0)+a;
            edt.putInt("number",sharedpref.getInt("number",0)+1);
            //Toast.makeText(Show.this,p+"",Toast.LENGTH_LONG).show();
            edt.putFloat("totalprice",p);
            edt.apply();
            WriteTodatabase();

        }
    });
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(x==0){

                }
                else {
                    x--;
                }
                num.setText(x+"");
            }
        });
    }
    public void WriteTodatabase(){
//             realm.delete(subject_content_realm.class);
        // Create a new object
String details=name.getText().toString()+"  ,  الكمية :"+num.getText().toString();
        realm.beginTransaction();
        orders_realm fatora = realm.createObject(orders_realm.class);
        fatora.setDetails(details);
        fatora.setName(name.getText().toString());
        fatora.setId(id);
        realm.commitTransaction();
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(Show.this);
        dlgAlert.setMessage("تم أضافة طلبك الى سلة الطلبات  ");
        dlgAlert.setTitle("Education App");
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
        startActivity(new Intent(Show.this,MainActivity.class));
        finish();

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
                startActivity(new Intent(Show.this,Order_Details.class));
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
    public void onBackPressed(){
       startActivity(new Intent(Show.this,MainActivity.class));
       finish();
    }
}
