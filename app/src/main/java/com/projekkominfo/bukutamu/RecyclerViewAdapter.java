package com.projekkominfo.bukutamu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

//Class Adapter ini Digunakan Untuk Mengatur Bagaimana Data akan Ditampilkan
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    //Deklarasi Variable
    private ArrayList<ImageUploadInfo> listData;
    private Context context;

    //Membuat Konstruktor, untuk menerima input dari Database

    public RecyclerViewAdapter(ArrayList<ImageUploadInfo> listData) {
        this.listData = listData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Membuat View untuk Menyiapkan dan Memasang Layout yang Akan digunakan pada RecyclerView
        View V = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_design, parent, false);
        return new ViewHolder(V);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        //Mengambil Nilai/Value yenag terdapat pada RecyclerView berdasarkan Posisi Tertentu
        final String Tanggal1 = listData.get(position).getTanggal();
        final String Nik1 = listData.get(position).getNik();
        final String Nama1 = listData.get(position).getNama();
        final String Alamat1 = listData.get(position).getAlamat();
        final String Pihak1 = listData.get(position).getPihak();
        final String Tujuan1 = listData.get(position).getTujuan();
        final String Gambar1 = listData.get(position).getImageURL();

        holder.Tanggal.setText("Tanggal : "+Tanggal1);
        holder.Nik.setText("NIK : "+Nik1);
        holder.Nama.setText("Nama : "+Nama1);
        holder.Alamat.setText("Alamat : "+Alamat1);
        holder.Pihak.setText("Pihak : "+Pihak1);
        holder.Tujuan.setText("Tujuan : "+Tujuan1);
        Picasso.with(context)
                .load(Gambar1)
                .fit()
                .centerCrop()
                .into(holder.Gambar);
    }

    @Override
    public int getItemCount() {
        //Menghitung Ukuran/Jumlah Data Yang Akan Ditampilkan Pada RecyclerView
        return listData.size();
    }

    //ViewHolder Digunakan Untuk Menyimpan Referensi Dari View-View
    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView Tanggal, Nik, Nama, Alamat, Pihak, Tujuan;
        private LinearLayout ListItem;
        private ImageView Gambar;

        ViewHolder(View itemView) {
            super(itemView);
            //Menginisialisasi View-View yang terpasang pada layout RecyclerView kita
            Tanggal = itemView.findViewById(R.id.tgl);
            Nik = itemView.findViewById(R.id.nk);
            Nama = itemView.findViewById(R.id.nm);
            Alamat = itemView.findViewById(R.id.almt);
            Pihak = itemView.findViewById(R.id.phk);
            Tujuan = itemView.findViewById(R.id.tjn);
            ListItem = itemView.findViewById(R.id.list_item);
            Gambar = itemView.findViewById(R.id.gmbr);
        }
    }
}