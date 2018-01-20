package com.infinite.rzzkan.uny.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.infinite.rzzkan.uny.Model.BeritaDetailModel;
import com.infinite.rzzkan.uny.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Rzzkan on 19/01/2018.
 */

public class BeritaDetailAdapter extends RecyclerView.Adapter<BeritaDetailAdapter.ViewHolder> {

    private Context context;
    private List<BeritaDetailModel> list;

    public BeritaDetailAdapter(Context context, List<BeritaDetailModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_berita_detail_layout,parent,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.textKonten.setText(list.get(position).getKonten());
        Picasso.with(context).load(list.get(position).getGambar()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textKonten;
        public ImageView imageView;
        public CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            textKonten = itemView.findViewById(R.id.konten);
            imageView = itemView.findViewById(R.id.imageView);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}