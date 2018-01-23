package com.infinite.rzzkan.uny.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.infinite.rzzkan.uny.Interface.OnSliderClickListener;
import com.infinite.rzzkan.uny.Model.SliderModel;
import com.infinite.rzzkan.uny.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Rzzkan on 21/01/2018.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.SliderPostHolder> {

    private ArrayList<SliderModel> dataSet;
    private final OnSliderClickListener listener;
    private Context mContext = null;

    public HomeAdapter(Context mContext, ArrayList<SliderModel> dataSet, OnSliderClickListener listener) {
        this.dataSet = dataSet;
       this.listener = listener;
        this.mContext = mContext;

    }

    @Override
    public SliderPostHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slider_layout,parent,false);
        SliderPostHolder postHolder = new SliderPostHolder(view);
        return postHolder;
    }

    @Override
    public void onBindViewHolder(SliderPostHolder holder, int position) {

        //set the views here
        ImageView ImageThumb = holder.ImageThumb;

        SliderModel object = dataSet.get(position);


      //  holder.bind(dataSet.get(position), listener);

        //TODO: image will be downloaded from url
        Picasso.with(mContext).load(object.getSlider()).into(ImageThumb);

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class SliderPostHolder extends RecyclerView.ViewHolder {

        ImageView ImageThumb;

        public SliderPostHolder(View itemView) {
            super(itemView);

            this.ImageThumb = (ImageView) itemView.findViewById(R.id.ImageThumb);

        }
        public void bind(final SliderModel item, final OnSliderClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onSliderClick(item);
                }
            });
        }

    }

}

