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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    private Button login;
    private TextView regist,forget;
    private EditText email, password;
    private FirebaseAuth mAuth;
    private ProgressDialog mLoading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.eT_email);
        password = findViewById(R.id.eT_password);
        mLoading = new ProgressDialog(this);
        mLoading.setMessage("Please Wait..");
        mAuth = FirebaseAuth.getInstance();
        login = findViewById(R.id.btn_login);
        regist = findViewById(R.id.tV_regist);
        forget = findViewById(R.id.tV_forget);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initialLogin();
            }
        });

        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,ChooseLoginActivity.class));
            }
        });

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,ForgetPassword.class));
            }
        });
    }

    private void initialLogin() {
        String email_user = email.getText().toString();
        final String password_user = password.getText().toString();
        if (email_user.isEmpty()) {
            email.setError("Masukan Email");
            email.setFocusable(true);
        } else if (password_user.isEmpty()) {
            password.setError("Masukan Password");
            password.setFocusable(true);
        }  else {
            login(email_user, password_user);
        }
    }

    private void login(String email_user, String password_user) {
        mLoading.show();

        mAuth.signInWithEmailAndPassword(email_user, password_user).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                final FirebaseUser user = mAuth.getCurrentUser();
                FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
                CollectionReference usersRef = rootRef.collection("user");
                if (task.isSuccessful()) {
                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    usersRef.document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String type = document.getString("role");
                                String nama = document.getString("nama");
                                if(type.equals("dokter")) {
                                    Toast.makeText(LoginActivity.this, "Berhasil Login", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this, BerandaDokterActivity.class));
                                    finish();
                                } else if (type.equals("pasien")) {
                                    Intent intent = new Intent(LoginActivity.this, BerandaPasienActivity.class);
                                    intent.putExtra("nama",nama);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }
                    });
//                    Toast.makeText(LoginActivity.this, "Berhasil Login", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(LoginActivity.this, BerandaActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    intent.putExtra("nama", user.getDisplayName());
//                    Log.d("cobaNama", "onComplete: " +user.getDisplayName());
//                    startActivity(intent);
//                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mLoading.dismiss();
                Toast.makeText(LoginActivity.this, "Gagal Login", Toast.LENGTH_SHORT).show();
            }
        });
    }
}