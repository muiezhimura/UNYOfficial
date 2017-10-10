package id.ac.uny.unyofficial.indeks_pengumuman;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import id.ac.uny.unyofficial.R;

/**
 * Created by WIN 8 on 07/10/2017.
 */

public class PengumumanAdapter extends RecyclerView.Adapter<PengumumanAdapter.PengumumanHolder> {
    private OnClickListener onClickListener;
    private Context context;
    ArrayList<HashMap<String, String>> data;
    HashMap<String, String> result = new HashMap<String, String>();

    public PengumumanAdapter(Context context, ArrayList<HashMap<String, String>> data) {
        this.context = context;
        this.data = data;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
    public class PengumumanHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView tv_title,tv_date;
        public PengumumanAdapter.OnClickListener onClickListener;
        HashMap<String, String> result = new HashMap<String, String>();

        public PengumumanHolder(View itemView,PengumumanAdapter.OnClickListener onClickListener) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.onClickListener = onClickListener;
            tv_title =itemView.findViewById(R.id.title);
            tv_date = itemView.findViewById(R.id.date);
        }

        @Override
        public void onClick(View view) {
            if(onClickListener != null){
                onClickListener.onClick(getAdapterPosition(),view);
            }
        }
    }

    @Override
    public PengumumanHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pengumuman, parent, false);
        PengumumanHolder holder = new PengumumanHolder(v,onClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(PengumumanHolder holder, int position) {
        result = data.get(position);
        holder.tv_title.setText(result.get(IndexPengumuman.TITLE));
        holder.tv_date.setText(result.get(IndexPengumuman.DATE));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface OnClickListener{
        void onClick(int adapterPosition, View view);
    }




}
