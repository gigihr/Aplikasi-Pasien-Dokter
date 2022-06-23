package com.example.hospitalapps;

import static br.eti.balena.security.ecdh.curve25519.Curve25519.ALGORITHM;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.scottyab.aescrypt.AESCrypt;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import br.eti.balena.security.ecdh.curve25519.Curve25519KeyAgreement;
import br.eti.balena.security.ecdh.curve25519.Curve25519KeyPairGenerator;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
//import org.junit.Assert;

public class BerandaPasienActivity extends AppCompatActivity {
    private RecyclerView rV_penyakit;
    PenyakitPasienAdapter penyakitAdapter;
    ArrayList<PenyakitList> listSakit = new ArrayList<>();
    ProgressDialog pd;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore fstore;
    private ImageView image_null;
    private Button btn_input, btn_barcode, btn_generate;
    private TextView tV_null;
    AlertDialog.Builder dialogBuilder;
    AlertDialog alertDialog;
    private EditText nama_penyakit, tahun_penyakit;
    private String getNama, getTahun, getNamaUser;
    private ProgressDialog mLoading;
    MultiFormatWriter multi = new MultiFormatWriter();
    int keey = 3;
    private String key = "Ngulikode";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beranda_pasien);
        rV_penyakit = findViewById(R.id.rV_data);
        pd = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        fstore = FirebaseFirestore.getInstance();
        image_null = findViewById(R.id.iV_null);
        tV_null = findViewById(R.id.tV_data);
        btn_input = findViewById(R.id.btn_input);
        btn_barcode = findViewById(R.id.btn_barcode);
        mLoading = new ProgressDialog(this);
        mLoading.setMessage("Please Wait..");
        btn_barcode.setVisibility(View.INVISIBLE);
        Intent iin = getIntent();
        Bundle b = iin.getExtras();


        if (b != null) {
            getNamaUser = (String) b.get("nama");
        }


        showData();


        btn_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog(R.layout.dialog_input);
            }
        });


        btn_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idDocument = "TYEfIBWLddEygDB4wPOt";
                DocumentReference documentReference = fstore.collection("ecdh").document(idDocument);
                documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                        try {
                            dialogBuilder = new AlertDialog.Builder(BerandaPasienActivity.this);
                            View layoutView = LayoutInflater.from(BerandaPasienActivity.this).inflate(R.layout.dialog_barcode, null);
                            ImageView barcode = layoutView.findViewById(R.id.iV_barcode);
                            String password = "password";
                            String message = "hello world";
                            try {
                                String encryptedMsg = AESCrypt.encrypt(password, firebaseUser.getUid());
//                                    String a = String.valueOf(anaSharedSecret.getEncoded());
                                BitMatrix bitMatrix = multi.encode(encryptedMsg, BarcodeFormat.QR_CODE, 300, 300);
//                                    Toast.makeText(BerandaPasienActivity.this, a, Toast.LENGTH_SHORT).show();

                                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                                Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                                barcode.setImageBitmap(bitmap);
                                dialogBuilder.setView(layoutView);
                                dialogBuilder.show();
                            } catch (GeneralSecurityException e) {
                                //handle error
                            }
//
                        } catch (WriterException e) {
                            e.printStackTrace();
                        }


                    }
                });

            }
        });

    }

    private void showAlertDialog(int layout) {
        dialogBuilder = new AlertDialog.Builder(BerandaPasienActivity.this);
        View layoutView = getLayoutInflater().inflate(layout, null);
        Button kirim = layoutView.findViewById(R.id.btn_simpan_penyakit);
        dialogBuilder.setView(layoutView);
        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        nama_penyakit = alertDialog.findViewById(R.id.eT_masukanNama);
        tahun_penyakit = alertDialog.findViewById(R.id.eT_tahun);
        kirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initialForm();
            }
        });
    }

    private void initialForm() {
        mLoading.show();
        getNama = nama_penyakit.getText().toString();
        getTahun = tahun_penyakit.getText().toString();

        if (getNama.isEmpty()) {
            nama_penyakit.setError("Masukan Nama Penyakit");
            nama_penyakit.setFocusable(true);
        }
        if (getTahun.isEmpty()) {
            tahun_penyakit.setError("Masukan Tahun Penyakit");
            tahun_penyakit.setFocusable(true);
        } else {
            DocumentReference documentReference = fstore.collection("penyakit").document();
            final FirebaseUser user = firebaseAuth.getCurrentUser();
            final String idPenyakit = documentReference.getId();
            String uid = user.getUid();
            String nama = user.getDisplayName();
            HashMap hashMap = new HashMap();


            hashMap.put("key", idPenyakit);
            hashMap.put("id", uid);
            hashMap.put("user", getNamaUser);
            hashMap.put("nama", getNama);
            hashMap.put("tahun", getTahun);
            hashMap.put("status", "false");
            hashMap.put("dokter", "null");
            documentReference.set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    mLoading.dismiss();
                    Toast.makeText(BerandaPasienActivity.this, "Berhasil Menambahkan Data Penyakit", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(BerandaPasienActivity.this, BerandaPasienActivity.class);
                    intent.putExtra("nama",getNamaUser);
                    startActivity(intent);
                    finish();
                }
            });
        }

    }

    private void showData() {
        pd.setTitle("Loading Data...");
        pd.show();
        fstore.collection("penyakit")
                .whereEqualTo("id", firebaseUser.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        listSakit.clear();
                        List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                        pd.dismiss();
                        for (DocumentSnapshot doc : snapshotList) {
                            PenyakitList penyakitList = new PenyakitList(
                                    doc.getString("nama"),
                                    doc.getString("tahun"),
                                    doc.getString("key"),
                                    doc.getString("id"),
                                    doc.getString("user"),
                                    doc.getString("status"));
                            listSakit.add(penyakitList);
                            if (listSakit == null) {
                                image_null.setVisibility(View.VISIBLE);
                                tV_null.setVisibility(View.VISIBLE);
                                btn_barcode.setVisibility(View.INVISIBLE);
                            } else {
                                image_null.setVisibility(View.INVISIBLE);
                                tV_null.setVisibility(View.INVISIBLE);
                                btn_barcode.setVisibility(View.VISIBLE);


                                //adapter
                                penyakitAdapter = new PenyakitPasienAdapter(BerandaPasienActivity.this, listSakit);
                                rV_penyakit.setAdapter(penyakitAdapter);
                                rV_penyakit.setHasFixedSize(true);
                                LinearLayoutManager lm = new LinearLayoutManager(BerandaPasienActivity.this, LinearLayoutManager.VERTICAL, false);
                                rV_penyakit.setLayoutManager(lm);
                                penyakitAdapter = new PenyakitPasienAdapter(BerandaPasienActivity.this, listSakit);
                                rV_penyakit.setNestedScrollingEnabled(true);
                                rV_penyakit.setAdapter(penyakitAdapter);
                            }

                        }

                    }
                });
    }


}