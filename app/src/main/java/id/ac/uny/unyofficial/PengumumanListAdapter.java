package id.ac.uny.unyofficial;

import android.content.Context;
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
    private ArrayList<String> mUrlIds = new ArrayList<>();
    protected LayoutInflater mInflater;
    protected onClickListener onClickListener;

    public void setOnClickListener(PengumumanListAdapter.onClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public PengumumanListAdapter(Context context, ArrayList<PengumumanModel> pengList) {
        this.mInflater = LayoutInflater.from(context);
        this.mPengList = pengList;

        for (PengumumanModel item: pengList) {
            mUrlIds.add(item.getUrlIdentifier());
        }
    }

    class PengumumanViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView pengumumanItemTitle;
        public final TextView pengumumanItemDate;
        final PengumumanListAdapter mAdapter;
        protected onClickListener onClickListener;

        public PengumumanViewHolder(View itemView, PengumumanListAdapter adapter, onClickListener onClickListener) {
            super(itemView);

            pengumumanItemTitle = (TextView) itemView.findViewById(R.id.pengumuman_item_title);
            pengumumanItemDate = (TextView) itemView.findViewById(R.id.pengumuman_item_date);
            this.mAdapter = adapter;
            this.onClickListener = onClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(onClickListener != null){
                onClickListener.onClick(v, getAdapterPosition());
            }
        }
    }

    public PengumumanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.pengumuman_item, parent, false);
        return new PengumumanViewHolder(itemView, this, onClickListener);
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

        // Prevent duplicates
        if(mUrlIds.size() > 0) {
            for (PengumumanModel item : list) {
                if(mUrlIds.contains(item.getUrlIdentifier())) {
                    list.remove(item);
                }
            }
        }
        mPengList.addAll(list);
        this.notifyDataSetChanged();
    }

    public interface onClickListener {
        void onClick(View v, int adapterPosition);
    }
}
