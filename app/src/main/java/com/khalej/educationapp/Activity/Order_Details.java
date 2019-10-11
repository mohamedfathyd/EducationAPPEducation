package com.khalej.educationapp.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.khalej.educationapp.Adapter.fatora_RecyclerAdapter;
import com.khalej.educationapp.R;
import com.khalej.educationapp.model.Apiclient_home;
import com.khalej.educationapp.model.apiinterface_home;
import com.khalej.educationapp.model.orders_realm;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import me.anwarshahriar.calligrapher.Calligrapher;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Order_Details extends AppCompatActivity {
    Toolbar toolbar;
    Realm realm;

    EditText name,address,phone;
    fatora_RecyclerAdapter recyclerAdapter;
    RecyclerView recyclerView;
    Typeface myTypeface;
    RecyclerView.LayoutManager layoutManager;
    String detail="";
    TextView getfinal,delete;
    String Date;

    private SharedPreferences sharedpref;
    private SharedPreferences.Editor edt;

    Call<ResponseBody> call = null;
    String charge;
    ProgressDialog progressDialog;
    private apiinterface_home apiinterface;
    TextView pricee;
    double price=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order__details);
        realm=Realm.getDefaultInstance();
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        realm=Realm.getDefaultInstance();
        setSupportActionBar(toolbar);
        recyclerView=(RecyclerView)findViewById(R.id.review);
        name=(EditText)findViewById(R.id.name);
        address=(EditText)findViewById(R.id.title);
        phone=(EditText)findViewById(R.id.phonee);
        getfinal=(TextView)findViewById(R.id.getfinal);
        delete=(TextView)findViewById(R.id.delete);
        pricee=findViewById(R.id.price);
        sharedpref = getSharedPreferences("Education", Context.MODE_PRIVATE);
        edt = sharedpref.edit();
        pricee.setText(sharedpref.getFloat("totalprice",0)+"");
        price=sharedpref.getFloat("totalprice",0);
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => "+c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(c.getTime());
        Date =formattedDate;
        layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "Droid.ttf", true);

        myTypeface = Typeface.createFromAsset(getAssets(), "Droid.ttf");
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

        sharedpref= getSharedPreferences("Education", Context.MODE_PRIVATE);
        name.setTypeface(myTypeface);
        name.setText(sharedpref.getString("name","").trim());
        address.setText(sharedpref.getString("address","").trim());
        address.setTypeface(myTypeface);
        phone.setTypeface(myTypeface);
        phone.setText(sharedpref.getString("phone","").trim());
        phone.setEnabled(false);
        name.setEnabled(false);
        delete.setTypeface(myTypeface);
        getfinal.setTypeface(myTypeface);
        showdata();
        getfinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] colors = {"البريد الممتاز (55 ريال)", "اراميكس (100 ريال)"};

                AlertDialog.Builder builder = new AlertDialog.Builder(Order_Details.this);
                builder.setTitle("اختار طريقة الشحن المناسبة لهذا الطلب");
                builder.setItems(colors, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        charge = colors[which];
                        // the user clicked on colors[which]
                        double total = price;
                        if (charge.equals("البريد الممتاز (55 ريال)")) {
                            total += 55;
                        } else {
                            total += 100;
                        }

                        dialog(total);

                    }
                });
                builder.show();
        }});

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deletedata();
                        edt.putFloat("totalprice", 0);
                        edt.putInt("number",0);
                        edt.apply();
                        Toast.makeText(Order_Details.this, "تم المسح", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Order_Details.this, MainActivity.class));
                        finish();
                    }
                });
            }

            public void dialog(double total) {
                // custom dialog
                final Dialog dialog = new Dialog(Order_Details.this);
                dialog.setContentView(R.layout.dialog_details);
                dialog.setTitle("تفاصيل التحويل");

                // set the custom dialog components - text, image and button
                TextView text = (TextView) dialog.findViewById(R.id.price);
                text.setText(total + "");

                Button dialogButton = (Button) dialog.findViewById(R.id.buttonn);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(Order_Details.this)
                                .setTitle("Education APP")
                                .setMessage("هل انت متأكد انك تريد تأكيد هذا الطلب وارساله؟")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int whichButton) {

                                        fetchdata();
                                        deletedata();
                                        getfinal.setClickable(false);
                                        edt.putFloat("totalprice", 0);
                                        edt.putInt("number",0);
                                        edt.apply();
                                    }
                                })
                                .setNegativeButton(android.R.string.no, null).show();
                        dialog.dismiss();
                    }
                });

                dialog.show();

            }


            public void deletedata() {
                realm.beginTransaction();
                RealmResults<orders_realm> content_realms = realm.where(orders_realm.class).findAll();
                content_realms.deleteAllFromRealm();
                realm.commitTransaction();
            }

            public void showdata() {
                realm.beginTransaction();
                RealmResults<orders_realm> content_realms = realm.where(orders_realm.class).findAll();
                if (content_realms.isEmpty() || content_realms.equals(null)) {
                    //  Toast.makeText(this, "empty", Toast.LENGTH_LONG).show();
                } else {    // realm.beginTransaction();
                    List<orders_realm> result = content_realms;

                    recyclerAdapter = new fatora_RecyclerAdapter(Order_Details.this, result);
                    recyclerView.setAdapter(recyclerAdapter);
                    for (int i = 0; i < result.size(); i++) {
                        detail += result.get(i).getName() + ":" + result.get(i).getDetails() + "\n\n";

                    }
                }
                realm.commitTransaction();
            }

            public void fetchdata() {
                progressDialog = ProgressDialog.show(Order_Details.this, "جاري تسجيل طلبك", "Please wait...", false, false);
                progressDialog.show();


                //  String currentTime = Calendar.getInstance().getTime().toString();
                apiinterface = Apiclient_home.getapiClient().create(apiinterface_home.class);
                Call<ResponseBody> call = apiinterface.getcontacts_order(name.getText().toString(),
                        address.getText().toString(), phone.getText().toString()
                        , detail, Date, price, charge);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        // Toast.makeText(Shopping_car.this,details,Toast.LENGTH_LONG).show();
                        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(Order_Details.this);
                        dlgAlert.setMessage("تم تسجيل تسجيل طلبك بنجاح ");
                        dlgAlert.setTitle("Education APP");
                        dlgAlert.setPositiveButton("OK", null);
                        dlgAlert.setCancelable(true);
                        dlgAlert.create().show();
                        //Toast.makeText(get_order.this,"d",Toast.LENGTH_LONG).show();
                        getfinal.setClickable(true);
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        //   Toast.makeText(get_order.this,t.toString(),Toast.LENGTH_LONG).show();
                    }
                });
            }
        }

