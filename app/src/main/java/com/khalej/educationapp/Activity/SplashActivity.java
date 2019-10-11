package com.khalej.educationapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.khalej.educationapp.R;

public class SplashActivity extends AppCompatActivity {
    ImageView i;
    private SharedPreferences sharedpref;
    private SharedPreferences.Editor edt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);

        i = (ImageView) findViewById(R.id.imageView);
        i.startAnimation(anim);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                sharedpref = getSharedPreferences("Education", Context.MODE_PRIVATE);
                edt = sharedpref.edit();
                edt.putInt("number",0);
                startActivity(new Intent(SplashActivity.this, Login.class));
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
}