package com.infinite.rzzkan.uny.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.infinite.rzzkan.uny.Interface.OnPengumumanClickListener;
import com.infinite.rzzkan.uny.Model.PengumumanModel;
import com.infinite.rzzkan.uny.R;


import java.util.ArrayList;

/**
 * Created by Rzzkan on 18/01/2018.
 */

public class PengumumanAdapter extends RecyclerView.Adapter<PengumumanAdapter.PengumumanPostHolder> {

    private ArrayList<PengumumanModel> dataSet;
    private final OnPengumumanClickListener listener;

    public PengumumanAdapter(Context mContext, ArrayList<PengumumanModel> dataSet, OnPengumumanClickListener listener) {
        this.dataSet = dataSet;
        this.listener = listener;

    }

    @Override
    public PengumumanPostHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pengumuman_layout,parent,false);
        PengumumanPostHolder postHolder = new PengumumanPostHolder(view);
        return postHolder;
    }

    @Override
    public void onBindViewHolder(PengumumanPostHolder holder, int position) {

        //set the views here
        TextView textViewTitle = holder.textViewTitle;
        TextView textViewDate = holder.textViewDate;

        PengumumanModel object = dataSet.get(position);

        textViewTitle.setText(object.getJudul());
        textViewDate.setText(object.getTanggal());

        holder.bind(dataSet.get(position), listener);

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class PengumumanPostHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewUrl;
        TextView textViewDate;

        public PengumumanPostHolder(View itemView) {
            super(itemView);
            this.textViewTitle = (TextView) itemView.findViewById(R.id.textViewTitle);
            this.textViewDate = (TextView) itemView.findViewById(R.id.textViewDate);

        }
        public void bind(final PengumumanModel item, final OnPengumumanClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onPengumumanClick(item);
                }
            });
        }

    }

}
