package com.example.hospitalapps;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HistoryPenyakitAdapter extends RecyclerView.Adapter<HistoryPenyakitAdapter.myViewHolder> {
    Context context;
    ArrayList<PenyakitList> penyakitList;

    public HistoryPenyakitAdapter(Context context, ArrayList<PenyakitList> penyakitList) {
        this.context = context;
        this.penyakitList = penyakitList;
    }

    @NonNull
    @Override
    public HistoryPenyakitAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_history_nama, parent, false);
        myViewHolder viewHolder = new myViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryPenyakitAdapter.myViewHolder holder, int position) {
        holder.nama_pasien.setText(penyakitList.get(position).getNamaPasien());
        holder.detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,HistoryPenyakitActivity.class);
                intent.putExtra("id",penyakitList.get(position).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return penyakitList.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        private TextView nama_pasien;
        private ImageView detail;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            nama_pasien = itemView.findViewById(R.id.tV_nama_pasien);
            detail = itemView.findViewById(R.id.img_detail);
        }
    }
}
