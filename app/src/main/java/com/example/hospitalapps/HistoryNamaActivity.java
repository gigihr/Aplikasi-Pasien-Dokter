package com.example.hospitalapps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HistoryNamaActivity extends AppCompatActivity {
    private RecyclerView rV_nama_pasien;
    HistoryPenyakitAdapter historyPenyakitAdapter;
    ArrayList<PenyakitList> listSakit = new ArrayList<>();
    ProgressDialog pd;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore fstore;
    private ProgressDialog mLoading;
    private EditText search;
    private Button searchbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_nama);
        rV_nama_pasien = findViewById(R.id.rV_nama_pasien);
        pd = new ProgressDialog(this);
        search = findViewById(R.id.eT_search);
        searchbtn = findViewById(R.id.btn_search);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        fstore = FirebaseFirestore.getInstance();
        mLoading = new ProgressDialog(this);
        mLoading.setMessage("Please Wait..");
        showData();

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textSearch = search.getText().toString();

                if (textSearch.isEmpty()){
                    showData();
                }else{
                    search(textSearch);
                }
            }
        });
    }

    private void showData() {
        pd.setTitle("Loading Data...");
        pd.show();
        fstore.collection("history")
                .whereEqualTo("iddokter",firebaseUser.getUid())
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
//                            if (listSakit == null) {
//                                image_null.setVisibility(View.VISIBLE);
//                                tV_null.setVisibility(View.VISIBLE);
//                                btn_barcode.setVisibility(View.INVISIBLE);
//                            } else {
//                                image_null.setVisibility(View.INVISIBLE);
//                                tV_null.setVisibility(View.INVISIBLE);
//                                btn_barcode.setVisibility(View.VISIBLE);


                                //adapter
                            historyPenyakitAdapter = new HistoryPenyakitAdapter(HistoryNamaActivity.this, listSakit);
                                rV_nama_pasien.setAdapter(historyPenyakitAdapter);
                            rV_nama_pasien.setHasFixedSize(true);
                                LinearLayoutManager lm = new LinearLayoutManager(HistoryNamaActivity.this, LinearLayoutManager.VERTICAL, false);
                            rV_nama_pasien.setLayoutManager(lm);
                            historyPenyakitAdapter = new HistoryPenyakitAdapter(HistoryNamaActivity.this, listSakit);
                            rV_nama_pasien.setNestedScrollingEnabled(true);
                            rV_nama_pasien.setAdapter(historyPenyakitAdapter);
//                            }

                        }

                    }
                });
    }

    private void search(String textSearch){
        pd.setTitle("Loading Data...");
        pd.show();
        fstore.collection("history")
                .whereEqualTo("user",textSearch)
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
//                            if (listSakit == null) {
//                                image_null.setVisibility(View.VISIBLE);
//                                tV_null.setVisibility(View.VISIBLE);
//                                btn_barcode.setVisibility(View.INVISIBLE);
//                            } else {
//                                image_null.setVisibility(View.INVISIBLE);
//                                tV_null.setVisibility(View.INVISIBLE);
//                                btn_barcode.setVisibility(View.VISIBLE);


                            //adapter
                            historyPenyakitAdapter = new HistoryPenyakitAdapter(HistoryNamaActivity.this, listSakit);
                            rV_nama_pasien.setAdapter(historyPenyakitAdapter);
                            rV_nama_pasien.setHasFixedSize(true);
                            LinearLayoutManager lm = new LinearLayoutManager(HistoryNamaActivity.this, LinearLayoutManager.VERTICAL, false);
                            rV_nama_pasien.setLayoutManager(lm);
                            historyPenyakitAdapter = new HistoryPenyakitAdapter(HistoryNamaActivity.this, listSakit);
                            rV_nama_pasien.setNestedScrollingEnabled(true);
                            rV_nama_pasien.setAdapter(historyPenyakitAdapter);
//                            }


                        }
                    }
                });
    }
}