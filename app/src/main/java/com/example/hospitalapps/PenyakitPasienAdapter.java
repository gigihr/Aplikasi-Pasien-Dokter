package com.example.hospitalapps;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

public class PenyakitPasienAdapter extends RecyclerView.Adapter<PenyakitPasienAdapter.myViewHolder>{
    Context context;
    ArrayList<PenyakitList> penyakitList;
    FirebaseFirestore fStore;
    public static final String TAG = "ADAPTERCHECK";

    public PenyakitPasienAdapter(Context context, ArrayList<PenyakitList> penyakitList) {
        this.context = context;
        this.penyakitList = penyakitList;
    }

    @NonNull
    @Override
    public PenyakitPasienAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_data_penyakit_pasien, parent, false);
        myViewHolder viewHolder = new myViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PenyakitPasienAdapter.myViewHolder holder, int position) {
        holder.nama_penyakit.setText(penyakitList.get(position).getNamaPenyakit());
        holder.tahun_penyakit.setText(penyakitList.get(position).getTahunPenyakit());
        if (penyakitList.get(position).getStatus().equals("false")){
            holder.LL_update.setBackgroundResource(R.drawable.bg_btn_barcode);
            holder.iV_update.setImageResource(R.drawable.ic_send);
        }else{
            holder.LL_update.setBackgroundResource(R.drawable.bg_btn_ceklis);
            holder.iV_update.setImageResource(R.drawable.ic_update);
        }
        holder.LL_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DocumentReference status = fStore.collection("penyakit").document(penyakitList.get(position).getKey());
                if (penyakitList.get(position).getStatus().equals("false")){

                    status.update("status","true");
                    Toast.makeText(context, "status diupdate", Toast.LENGTH_SHORT).show();
                    v.getContext().startActivity(new Intent(context,BerandaPasienActivity.class));
                }else{

                    status.update("status","false");
                    Toast.makeText(context, "status diupdate", Toast.LENGTH_SHORT).show();
                    v.getContext().startActivity(new Intent(context,BerandaPasienActivity.class));
                }

            }
        });
        holder.LL_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fStore.collection("penyakit").document(penyakitList.get(position).getKey())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(view.getContext(), "Deleted ", Toast.LENGTH_SHORT).show();

                                penyakitList.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, penyakitList.size());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: " + e);
                            }
                        });

            }
        });
    }

    @Override
    public int getItemCount() {
        return penyakitList.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        private TextView nama_penyakit,tahun_penyakit;
        private LinearLayout LL_update,LL_delete;
        private ImageView iV_update;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            nama_penyakit = itemView.findViewById(R.id.tV_nama_penyakit);
            tahun_penyakit = itemView.findViewById(R.id.tV_tahun_penyakit);
//            LL_barcode = itemView.findViewById(R.id.LL_barcode);
            LL_update = itemView.findViewById(R.id.LL_update);
            iV_update = itemView.findViewById(R.id.iV_update);
            LL_delete = itemView.findViewById(R.id.LL_delete);
            fStore = FirebaseFirestore.getInstance();
        }
    }
}
