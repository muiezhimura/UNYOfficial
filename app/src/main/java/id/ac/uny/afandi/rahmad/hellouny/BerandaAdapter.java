package id.ac.uny.afandi.rahmad.hellouny;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by rmd on 1/18/2018.
 */

class BerandaAdapter extends RecyclerView.Adapter<BerandaAdapter.ViewHolder> {

    private Context context;
    private List<Beranda> list;

    public BerandaAdapter(Context context, List<Beranda> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_berita_beranda,parent,false);

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
//                DetailBeritaFragment detailBeritaFragment = new DetailBeritaFragment();
                Bundle bundle = new Bundle();
                bundle.putString("url", list.get(position).getLink());
//                detailBeritaFragment.setArguments(bundle);
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
        public ImageView imageView;
        public CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            textJudul = itemView.findViewById(R.id.judul);
            textTanggal = itemView.findViewById(R.id.tanggal);
            imageView = itemView.findViewById(R.id.imageView);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}

