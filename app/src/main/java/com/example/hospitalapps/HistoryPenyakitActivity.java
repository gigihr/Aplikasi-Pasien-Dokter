package com.example.hospitalapps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HistoryPenyakitActivity extends AppCompatActivity {
    private RecyclerView rV_penyakit;
    PenyakitAdapter penyakitAdapter;
    ArrayList<PenyakitList> listSakit = new ArrayList<>();
    ProgressDialog pd;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore fstore;
    private ImageView image_null;
    private TextView tV_null;
    private String idpenyakit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_penyakit);
        rV_penyakit = findViewById(R.id.rV_data);
        pd = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        fstore = FirebaseFirestore.getInstance();
        image_null = findViewById(R.id.iV_null);
        tV_null = findViewById(R.id.tV_data);

        Intent iin= getIntent();
        Bundle b = iin.getExtras();


        if(b!=null)
        {
            idpenyakit =(String) b.get("id");
        }
        showData();
    }

    private void showData() {
        pd.setTitle("Loading Data...");
        pd.show();
        fstore.collection("history")
                .whereEqualTo("id",idpenyakit)
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
                            } else {
                                image_null.setVisibility(View.INVISIBLE);
                                tV_null.setVisibility(View.INVISIBLE);


                                //adapter
                                penyakitAdapter = new PenyakitAdapter(HistoryPenyakitActivity.this, listSakit);
                                rV_penyakit.setAdapter(penyakitAdapter);
                                rV_penyakit.setHasFixedSize(true);
                                LinearLayoutManager lm = new LinearLayoutManager(HistoryPenyakitActivity.this, LinearLayoutManager.VERTICAL, false);
                                rV_penyakit.setLayoutManager(lm);
                                penyakitAdapter = new PenyakitAdapter(HistoryPenyakitActivity.this, listSakit);
                                rV_penyakit.setNestedScrollingEnabled(true);
                                rV_penyakit.setAdapter(penyakitAdapter);
                            }

                        }

                    }
                });
    }
}