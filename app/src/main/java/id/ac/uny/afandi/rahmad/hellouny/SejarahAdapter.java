package id.ac.uny.afandi.rahmad.hellouny;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by rmd on 1/18/2018.
 */

class SejarahAdapter extends RecyclerView.Adapter<SejarahAdapter.ViewHolder> {

    private Context context;
    private List<Sejarah> list;

    public SejarahAdapter(Context context, List<Sejarah> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sejarah,parent,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textSejarah.setText(list.get(position).getSejarah());
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

        public TextView textSejarah;
        public CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            textSejarah = itemView.findViewById(R.id.sejarah);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}

