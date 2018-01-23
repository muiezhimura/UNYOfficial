package com.infinite.rzzkan.uny.Adapter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.infinite.rzzkan.uny.Interface.OnPengumumanClickListener;
import com.infinite.rzzkan.uny.Model.PengumumanModel;
import com.infinite.rzzkan.uny.R;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rzzkan on 18/01/2018.
 */

public class PengumumanAdapter extends RecyclerView.Adapter<PengumumanAdapter.ViewHolder> {

    private Context context;
    private List<PengumumanModel> list;

    public PengumumanAdapter(Context context, List<PengumumanModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pengumuman_layout,parent,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textTanggal.setText(list.get(position).getTanggal());
        holder.textJudul.setText(list.get(position).getJudul());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity compatActivity = (AppCompatActivity) v.getContext();
//                DetailBeritaFragment detailBeritaFragment = new DetailBeritaFragment();
//                compatActivity.getFragmentManager().beginTransaction().replace(R.id.berita,detailBeritaFragment).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textJudul;
        public TextView textTanggal;
        public CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            textJudul = itemView.findViewById(R.id.textViewTitle);
            textTanggal = itemView.findViewById(R.id.textViewDate);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
