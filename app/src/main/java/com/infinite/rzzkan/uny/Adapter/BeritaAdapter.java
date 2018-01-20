package com.infinite.rzzkan.uny.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.infinite.rzzkan.uny.Fragment.BeritaDetailFragment;
import com.infinite.rzzkan.uny.Interface.OnBeritaClickListener;
import com.infinite.rzzkan.uny.R;
import com.squareup.picasso.Picasso;
import com.infinite.rzzkan.uny.Model.BeritaModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rzzkan on 19/01/2018.
 */

public class BeritaAdapter extends RecyclerView.Adapter<BeritaAdapter.BeritaPostHolder> {

    private ArrayList<BeritaModel> dataSet;
    private final OnBeritaClickListener listener;
    private Context mContext = null;

    public BeritaAdapter(Context mContext, ArrayList<BeritaModel> dataSet, OnBeritaClickListener listener) {
        this.dataSet = dataSet;
        this.listener = listener;
        this.mContext = mContext;

    }

    @Override
    public BeritaPostHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_berita_layout,parent,false);
        BeritaPostHolder postHolder = new BeritaPostHolder(view);
        return postHolder;
    }

    @Override
    public void onBindViewHolder(BeritaPostHolder holder, int position) {

        //set the views here
        TextView textViewTitle = holder.textViewTitle;
        TextView textViewDate = holder.textViewDate;
        ImageView ImageThumb = holder.ImageThumb;

        BeritaModel object = dataSet.get(position);

        textViewTitle.setText(object.getJudul());
        textViewDate.setText(object.getTanggal());

        holder.bind(dataSet.get(position), listener);

        //TODO: image will be downloaded from url
        Picasso.with(mContext).load(object.getGambar()).into(ImageThumb);

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class BeritaPostHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewUrl;
        TextView textViewDate;
        ImageView ImageThumb;

        public BeritaPostHolder(View itemView) {
            super(itemView);
            this.textViewTitle = (TextView) itemView.findViewById(R.id.textViewTitle);
            this.textViewDate = (TextView) itemView.findViewById(R.id.textViewDate);
            this.ImageThumb = (ImageView) itemView.findViewById(R.id.ImageThumb);

        }
        public void bind(final BeritaModel item, final OnBeritaClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onBeritaClick(item);
                }
            });
        }

    }

}
