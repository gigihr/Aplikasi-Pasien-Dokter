package com.example.hospitalapps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class MainActivity extends AppCompatActivity {
LottieAnimationView animasi;
TextView appsName;
LinearLayout background;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        animasi = findViewById(R.id.animasi);
        appsName = findViewById(R.id.apps_name);
        background = findViewById(R.id.LL_background);

        background.animate().translationY(-2500).setDuration(1000).setStartDelay(5000);
        appsName.animate().translationY(2000).setDuration(1000).setStartDelay(5000);
        animasi.animate().translationY(1500).setDuration(1000).setStartDelay(5000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                finish();
            }
        },6000);
    }
}