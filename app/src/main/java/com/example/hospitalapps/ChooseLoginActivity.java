package com.example.hospitalapps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class ChooseLoginActivity extends AppCompatActivity {
private LinearLayout dokter,pasien;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_login);
        dokter = findViewById(R.id.LL_registDokter);
        pasien = findViewById(R.id.LL_registPasien);

        dokter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChooseLoginActivity.this,RegistrasiDokterActivity.class));
            }
        });

        pasien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChooseLoginActivity.this,RegistrasiPasienActivity.class));
            }
        });
    }
}