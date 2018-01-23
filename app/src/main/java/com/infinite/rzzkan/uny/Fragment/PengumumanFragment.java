package com.infinite.rzzkan.uny.Fragment;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.infinite.rzzkan.uny.Adapter.PengumumanAdapter;
import com.infinite.rzzkan.uny.Model.PengumumanModel;
import com.infinite.rzzkan.uny.R;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class PengumumanFragment extends Fragment {
    protected int page = 1;
    private String API_URL = "http://api.ngeartstudio.com/pengumuman/";

    private RecyclerView mList_pengumuman = null;
    private PengumumanAdapter adapter = null;
    private ArrayList<PengumumanModel> mListData = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    public PengumumanFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pengumuman, container, false);
        getActivity().setTitle("Pengumuman UNY");
        mList_pengumuman = (RecyclerView) view.findViewById(R.id.mList_pengumuman);
        loadMyContent(page);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        mList_pengumuman.setLayoutManager(linearLayoutManager);
        adapter = new PengumumanAdapter(getActivity(),mListData);
        mList_pengumuman.setAdapter(adapter);
        mList_pengumuman.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if(linearLayoutManager.findLastCompletelyVisibleItemPosition() == mListData.size()-1){
                    page+=1;
                    loadMyContent(page);
                }
            }
        });
        return view;
    }

    private void loadMyContent(final int id) {
        AsyncTask<Integer,Void,Void> task =new AsyncTask<Integer, Void, Void>() {
            @Override
            protected Void doInBackground(Integer... integers) {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://api.ngeartstudio.com/pengumuman/"+id)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    JSONObject object = new JSONObject(response.body().string());
                    JSONArray jsonArray = object.getJSONArray("pengumuman");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject json = jsonArray.getJSONObject(i);

                        String judul = json.getString("judul");
                        String tanggal = json.getString("tanggal");
                        String link = json.getString("link");
                        PengumumanModel pengumumanObject = new PengumumanModel(judul,link,tanggal);
                        mListData.add(pengumumanObject);
                    }
                } catch (JSONException e) {
                    System.out.println(e.getMessage());
                } catch (IOException e) {
                    System.out.println("End of Content");
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                adapter.notifyDataSetChanged();
            }
        };
        task.execute(id);
    }
}

