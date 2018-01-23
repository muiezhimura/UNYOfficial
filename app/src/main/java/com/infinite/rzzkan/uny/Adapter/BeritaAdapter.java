package com.infinite.rzzkan.uny.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.infinite.rzzkan.uny.Fragment.DetailBeritaFragment;
import com.infinite.rzzkan.uny.Model.BeritaModel;
import com.infinite.rzzkan.uny.R;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by Rzzkan on 19/01/2018.
 */

public class  BeritaAdapter extends RecyclerView.Adapter<BeritaAdapter.ViewHolder> {

    private Context context;
    private List<BeritaModel> list;

    public BeritaAdapter(Context context, List<BeritaModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_berita_layout,parent,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.textTanggal.setText(list.get(position).getTanggal());
        holder.textJudul.setText(list.get(position).getJudul());
        Picasso.with(context).load(list.get(position).getGambar()).into(holder.imageView);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity compatActivity = (AppCompatActivity) v.getContext();
                DetailBeritaFragment detailBeritaFragment = new DetailBeritaFragment();
                Bundle bundle = new Bundle();
                bundle.putString("url", list.get(position).getLink());
                detailBeritaFragment.setArguments(bundle);
                compatActivity.getFragmentManager().beginTransaction().replace(R.id.content_berita,detailBeritaFragment).addToBackStack(null).commit();
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
        public ImageView imageView;
        public CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            textJudul = itemView.findViewById(R.id.textViewTitle);
            textTanggal = itemView.findViewById(R.id.textViewDate);
            imageView = itemView.findViewById(R.id.ImageThumb);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
