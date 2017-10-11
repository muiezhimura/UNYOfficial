package id.ac.uny.unyofficial;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by groovyle on 10/10/2017.
 */

public class PengumumanListAdapter extends
        RecyclerView.Adapter<PengumumanListAdapter.PengumumanViewHolder> {

    protected ArrayList<PengumumanModel> mPengList;
    protected LayoutInflater mInflater;

    public PengumumanListAdapter(Context context, ArrayList<PengumumanModel> pengList) {
        this.mInflater = LayoutInflater.from(context);
        this.mPengList = pengList;
    }

    class PengumumanViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView pengumumanItemTitle;
        public final TextView pengumumanItemDate;
        final PengumumanListAdapter mAdapter;

        public PengumumanViewHolder(View itemView, PengumumanListAdapter adapter) {
            super(itemView);

            pengumumanItemTitle = (TextView) itemView.findViewById(R.id.pengumuman_item_title);
            pengumumanItemDate = (TextView) itemView.findViewById(R.id.pengumuman_item_date);
            this.mAdapter = adapter;
        }

        @Override
        public void onClick(View v) {
            int mPosition = getLayoutPosition();
            PengumumanModel current = mPengList.get(mPosition);

            Intent intent = new Intent(v.getContext(), DetailPengumuman.class);
            intent.putExtra("id.ac.uny.unyofficial.pengumuman_item_id", current.getUrlIdentifier());
        }
    }

    public PengumumanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.pengumuman_item, parent, false);
        return new PengumumanViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(PengumumanViewHolder holder, int position) {
        PengumumanModel current = mPengList.get(position);
        holder.pengumumanItemTitle.setText(current.getTitle());
        holder.pengumumanItemDate.setText(current.getFormattedPostDate());
    }

    @Override
    public int getItemCount() {
        return mPengList.size();
    }

    public void mergeList(ArrayList<PengumumanModel> list) {
        if(list.size() == 0) {
            return;
        }

        mPengList.addAll(list);
        this.notifyDataSetChanged();
    }

    public interface onClickListener {
        void onClick(String pengUrlIdentifier);
    }
}
