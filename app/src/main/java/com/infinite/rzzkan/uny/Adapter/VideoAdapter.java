package com.infinite.rzzkan.uny.Adapter;

import android.content.Context;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;


import com.infinite.rzzkan.uny.Interface.OnVideoClickListener;
import com.infinite.rzzkan.uny.Model.BeritaModel;
import com.infinite.rzzkan.uny.Model.VideoModel;
import com.infinite.rzzkan.uny.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rzzkan on 16/01/2018.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.YoutubePostHolder> {

    private ArrayList<VideoModel> dataSet;
    private Context mContext = null;
    private final OnVideoClickListener listener;
    private Context context;
    private List<VideoModel> list;

//    public VideoAdapter(Context context, List<VideoModel> list) {
//        this.context = context;
//        this.list = list;
//    }
//
//    @Override
//    public VideoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_row,parent,false);
//
//        return new VideoAdapter.ViewHolder(itemView);
//    }
//
//    @Override
//    public void onBindViewHolder(BeritaAdapter.ViewHolder holder, final int position) {
//        holder.textTanggal.setText(list.get(position).getPublishedAt());
//        holder.textDeskripsi.setText(list.get(position).getDescription());
//        holder.textJudul.setText(list.get(position).getTitle());
//        Picasso.with(context).load(list.get(position).getThumbnail()).into(holder.imageView);
//        holder.cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AppCompatActivity compatActivity = (AppCompatActivity) v.getContext();
//                DetailVideoFragment detailVideoFragment = new DetailVideoFragment();
//                Bundle bundle = new Bundle();
//                bundle.putString("url", list.get(position).getLink());
//                detailVideoFragment.setArguments(bundle);
//                compatActivity.getFragmentManager().beginTransaction().replace(R.id.content_video, detailVideoFragment).addToBackStack(null).commit();
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return list.size();
//    }
//
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//
//        public TextView textJudul;
//        public TextView textTanggal;
//        public TextView textDeskripsi;
//        public ImageView imageView;
//        public CardView cardView;
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//            textJudul = itemView.findViewById(R.id.textViewTitle);
//            textTanggal = itemView.findViewById(R.id.textViewDate);
//            textDeskripsi = itemView.findViewById(R.id.textViewDes);
//            imageView = itemView.findViewById(R.id.ImageThumb);
//            cardView = itemView.findViewById(R.id.cardView);
//        }
//    }

    public VideoAdapter(Context mContext, ArrayList<VideoModel> dataSet, OnVideoClickListener listener) {
        this.dataSet = dataSet;
        this.mContext = mContext;
        this.listener = listener;

    }

    @Override
    public YoutubePostHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_row,parent,false);
        YoutubePostHolder postHolder = new YoutubePostHolder(view);
        return postHolder;
    }

    @Override
    public void onBindViewHolder(YoutubePostHolder holder, int position) {

        //set the views here
        TextView textViewTitle = holder.textViewTitle;
        TextView textViewDes = holder.textViewDes;
        TextView textViewDate = holder.textViewDate;
        ImageView ImageThumb = holder.ImageThumb;

        VideoModel object = dataSet.get(position);

        textViewTitle.setText(object.getTitle());
        textViewDes.setText(object.getDescription());
        textViewDate.setText(object.getPublishedAt());

        holder.bind(dataSet.get(position), listener);

        //TODO: image will be downloaded from url
        Picasso.with(mContext).load(object.getThumbnail()).into(ImageThumb);

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class YoutubePostHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewDes;
        TextView textViewDate;
        ImageView ImageThumb;

        public YoutubePostHolder(View itemView) {
            super(itemView);
            this.textViewTitle = (TextView) itemView.findViewById(R.id.textViewTitle);
            this.textViewDes = (TextView) itemView.findViewById(R.id.textViewDes);
            this.textViewDate = (TextView) itemView.findViewById(R.id.textViewDate);
            this.ImageThumb = (ImageView) itemView.findViewById(R.id.ImageThumb);

        }
        public void bind(final VideoModel item, final OnVideoClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onVideoClick(item);
                }
            });
        }

    }

}

