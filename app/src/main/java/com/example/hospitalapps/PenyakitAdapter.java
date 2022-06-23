package com.example.hospitalapps;

import static android.content.Context.WINDOW_SERVICE;



import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.ArrayList;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class PenyakitAdapter extends RecyclerView.Adapter<PenyakitAdapter.myViewHolder>{
    Context context;
    ArrayList<PenyakitList> penyakitList;
    FirebaseFirestore fStore;
    private DatabaseReference databaseReference;
    public static final String TAG = "ADAPTERCHECK";
    Bitmap bitmap;
    QRGEncoder qrgEncoder;
    AlertDialog.Builder dialogBuilder;
    AlertDialog alertDialog;
    MultiFormatWriter multi = new MultiFormatWriter();


    public PenyakitAdapter(Context context, ArrayList<PenyakitList> penyakitList) {
        this.context = context;
        this.penyakitList = penyakitList;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_data_penyakit, parent, false);
        myViewHolder viewHolder = new myViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.nama_penyakit.setText(penyakitList.get(position).getNamaPenyakit());
        holder.tahun_penyakit.setText(penyakitList.get(position).getTahunPenyakit());
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

//        holder.LL_barcode.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//
//                try {
//                    dialogBuilder = new AlertDialog.Builder(view.getRootView().getContext());
//                    View layoutView =LayoutInflater.from(view.getRootView().getContext()).inflate(R.layout.dialog_barcode,null);
//                    ImageView barcode = layoutView.findViewById(R.id.iV_barcode);
//                    BitMatrix bitMatrix = multi.encode(penyakitList.get(position).getKey(), BarcodeFormat.QR_CODE, 300, 300);
//
//                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
//                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
//                    barcode.setImageBitmap(bitmap);
//                    dialogBuilder.setView(layoutView);
//                    dialogBuilder.show();
//                }catch (WriterException e){
//                    e.printStackTrace();
//                }
//
//
//
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return penyakitList.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        private TextView nama_penyakit,tahun_penyakit;
        private LinearLayout LL_barcode,LL_delete;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            nama_penyakit = itemView.findViewById(R.id.tV_nama_penyakit);
            tahun_penyakit = itemView.findViewById(R.id.tV_tahun_penyakit);
//            LL_barcode = itemView.findViewById(R.id.LL_barcode);
            LL_delete = itemView.findViewById(R.id.LL_delete);
            fStore = FirebaseFirestore.getInstance();
        }
    }


}
