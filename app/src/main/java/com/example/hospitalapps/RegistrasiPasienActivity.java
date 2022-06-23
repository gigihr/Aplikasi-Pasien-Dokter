package com.example.hospitalapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class RegistrasiPasienActivity extends AppCompatActivity {
    private EditText eT_nama, eT_email, eT_pass, eT_konf;
    private TextView tV_login;
    private Button btn_daftar;
    private String nama, email, pass, konf;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore fStore;
    private ProgressDialog mLoading;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrasi_pasien);
        eT_nama = findViewById(R.id.eT_nama);
        eT_email = findViewById(R.id.eT_email);
        eT_pass = findViewById(R.id.eT_password);
        eT_konf = findViewById(R.id.eT_konf);
        btn_daftar = findViewById(R.id.btn_daftar);
        tV_login = findViewById(R.id.tV_login);
        firebaseAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        mLoading = new ProgressDialog(this);
        mLoading.setMessage("Mohon Ditunggu..");

        tV_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            startActivity(new Intent(RegistrasiPasienActivity.this,LoginActivity.class));
            }
        });

        btn_daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                initialRegist();

            }
        });


    }

    private void initialRegist() {
        nama = eT_nama.getText().toString();
        email = eT_email.getText().toString();
        pass = eT_pass.getText().toString();
        konf = eT_konf.getText().toString();

        if (nama.isEmpty()) {
            eT_nama.setError("Masukan nama terlebih dahulu");
            eT_nama.setFocusable(true);
        } else if (email.isEmpty()) {
            eT_email.setError("Masukan email terlebih dahulu");
            eT_email.setFocusable(true);
        } else if (!email.matches(emailPattern)) {
            eT_email.setError("Masukan Format Email yang Benar");
            eT_email.setFocusable(true);
        } else if (pass.isEmpty()) {
            eT_pass.setError("Masukan password terlebih dahulu");
            eT_pass.setFocusable(true);
        } else if (pass.length() < 8) {
            eT_pass.setError("Masukan Password minimal 8");
            eT_pass.setFocusable(true);
        } else if (konf.isEmpty()) {
            eT_konf.setError("Masukan Confirm Password Terlebih Dahulu");
            eT_konf.setFocusable(true);
        } else if (!konf.equals(pass)) {
            eT_konf.setError("Masukan Password yang Sama");
            eT_konf.setFocusable(true);
        } else {
            registrasiUser(email, pass);
        }
    }

    private void registrasiUser(String email, String pass) {

        firebaseAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        final FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (task.isSuccessful()) {
                            String uid = user.getUid();
                            DocumentReference documentReference = fStore.collection("user").document(uid);


                            HashMap hashMap = new HashMap();
                            String emailmap = user.getEmail();

                            hashMap.put("uid", uid);
                            hashMap.put("nama", nama);
                            hashMap.put("email", emailmap);
                            hashMap.put("role", "pasien");
                            documentReference.set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    mLoading.dismiss();
                                    Toast.makeText(RegistrasiPasienActivity.this, "Berhasil Daftar Dengan Email\n" + user.getEmail(), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(RegistrasiPasienActivity.this, LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            });

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegistrasiPasienActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
