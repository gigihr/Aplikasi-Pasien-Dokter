package com.example.hospitalapps;

import static br.eti.balena.security.ecdh.curve25519.Curve25519.ALGORITHM;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.netricecake.ecdh.Curve25519;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.primitives.Longs;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.Result;
import com.scottyab.aescrypt.AESCrypt;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import br.eti.balena.security.ecdh.curve25519.Curve25519KeyAgreement;
import br.eti.balena.security.ecdh.curve25519.Curve25519KeyPairGenerator;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import android.util.Base64;

public class BerandaDokterActivity extends Activity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    private Button scanner, history;
    private FirebaseFirestore fstore;
    private FirebaseAuth firebaseAuth;
    int keey=3;
    private String key = "Ngulikode";

    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beranda_dokter);
        scanner = findViewById(R.id.btn_scan);
        history = findViewById(R.id.btn_history);
        fstore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();



        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BerandaDokterActivity.this,HistoryNamaActivity.class));
            }
        });


//        Buat Testing Dekripsi

//        String password = "password";
//        String encryptedMsg = "n4RYenP15vSCUUv0Owk4wmjUieBU4pSOKUoCDvoDiOo=";
//        try {
////            String messageAfterDecrypt = AESCrypt.decrypt(password, result.getText());
//            String messageAfterDecryptTest = AESCrypt.decrypt(password, encryptedMsg);
////            Toast.makeText(this, messageAfterDecrypt, Toast.LENGTH_SHORT).show();
////            Toast.makeText(this, messageAfterDecryptTest, Toast.LENGTH_SHORT).show();
//            scanPenyakit(messageAfterDecryptTest);
//        }catch (GeneralSecurityException e){
//            Buat Handle Errornya
//        }

    }



    public void onClick(View v) {
//        String idDocument = "TYEfIBWLddEygDB4wPOt";
//        DocumentReference documentReference = fstore.collection("ecdh").document(idDocument);
//        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                        mScannerView = new ZXingScannerView(BerandaDokterActivity.this);
                        setContentView(mScannerView);
                        mScannerView.setResultHandler(BerandaDokterActivity.this);
                        mScannerView.startCamera();
//
//
//            }
//        });


    }

    @Override
    protected void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(BerandaDokterActivity.this, BerandaDokterActivity.class));
    }

    @Override
    public void handleResult(Result result) {
        String password = "password";
        String encryptedMsg = "n4RYenP15vSCUUv0Owk4wmjUieBU4pSOKUoCDvoDiOo=";
        try {

            String messageAfterDecrypt = AESCrypt.decrypt(password, result.getText());
            String messageAfterDecryptTest = AESCrypt.decrypt(password, encryptedMsg);

            scanPenyakit(messageAfterDecrypt);





        }catch (GeneralSecurityException e){
            //handle error - could be due to incorrect password or tampered encryptedMsg
        }
//        Log.w("handleResult", DecrytionCc(result.getText()));
//        Toast.makeText(this, deskripsi(result.getText(),key), Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, result.getText(), Toast.LENGTH_SHORT).show();
//        DocumentReference documentReference = fstore.collection("penyakit").document(result.getText());
//        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(BerandaDokterActivity.this);
//                builder.setTitle("Scan Result");
//                builder.setMessage("Nama Penyakit : "+value.getString("nama") +"\n" + "Tahun Penyakit : " + value.getString("tahun"));
//                AlertDialog alertDialog = builder.create();
//                alertDialog.show();
//
//            }
//        });


        //Resume scanning uncomment below
        //mScannerView.resumeCameraPreview(this);
    }

    private void scanPenyakit(String messageAfterDecrypt) {
        fstore.collection("penyakit")
                .whereEqualTo("id",messageAfterDecrypt)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot doc : snapshotList) {
                            PenyakitList penyakitList = new PenyakitList(
                                    doc.getString("nama"),
                                    doc.getString("tahun"),
                                    doc.getString("key"),
                                    doc.getString("id"),
                                    doc.getString("user"),
                                    doc.getString("status"));

                            DocumentReference documentReference = fstore.collection("history").document();
                            final String idhistory = documentReference.getId();
                            final FirebaseUser user = firebaseAuth.getCurrentUser();
                            HashMap hashMap = new HashMap();
                            String iddokter = user.getUid();
                            if (user != null) {
                                DocumentReference documentReferencePenyakit = fstore.collection("user").document(iddokter);
                                documentReferencePenyakit.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//                                        Log.d("Cek masuk gak", value.getString("nama"));
//                                        String nama = value.getString("nama");
//                                        String id = value.getString("nama");
//                                        Toast.makeText(BerandaDokterActivity.this, id, Toast.LENGTH_SHORT).show();

//                                        if (value.getString("id").equals(messageAfterDecrypt));
                                                if (doc.getString("status").equals("true")){
                                                    hashMap.put("id", idhistory);
                                                    hashMap.put("iddokter", iddokter);
                                                    hashMap.put("nama", doc.getString("nama"));
                                                    hashMap.put("tahun", doc.getString("tahun"));
                                                    hashMap.put("key", doc.getString("key"));
                                                    hashMap.put("user", doc.getString("user"));
//                                                    hashMap.put("dokter", nama);

//                                                    documentReferencePenyakit.update("dokter",nama);
                                                    documentReference.set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Toast.makeText(BerandaDokterActivity.this, "Berhasil", Toast.LENGTH_SHORT).show();
                                                            startActivity(new Intent(BerandaDokterActivity.this,BerandaDokterActivity.class));
                                                        }
                                                    });
                                                }


//                                        }

                                    }
                                });
                            }





                        }


                    }
                });
    }





}