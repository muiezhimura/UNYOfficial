package com.infinite.rzzkan.uny.Fragment;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.infinite.rzzkan.uny.Adapter.BeritaAdapter;
import com.infinite.rzzkan.uny.Adapter.PengumumanAdapter;
import com.infinite.rzzkan.uny.DetailsActivity;
import com.infinite.rzzkan.uny.Interface.OnBeritaClickListener;
import com.infinite.rzzkan.uny.Model.BeritaModel;
import com.infinite.rzzkan.uny.Model.PengumumanModel;
import com.infinite.rzzkan.uny.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class BeritaFragment extends Fragment {
    protected int page = 1;
    private String API_URL = "http://api.ngeartstudio.com/berita/";

    private RecyclerView mList_berita = null;
    private BeritaAdapter adapter = null;
    private ArrayList<BeritaModel> mListData = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    public BeritaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_berita, container, false);
        getActivity().setTitle("Berita UNY");
        mList_berita = (RecyclerView) view.findViewById(R.id.mList_berita);
        loadMyContent(page);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        mList_berita.setLayoutManager(linearLayoutManager);
        adapter = new BeritaAdapter(getActivity(),mListData);
        mList_berita.setAdapter(adapter);
        mList_berita.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                        .url("http://api.ngeartstudio.com/berita/"+id)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    JSONObject object = new JSONObject(response.body().string());
                    JSONArray jsonArray = object.getJSONArray("berita");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject json = jsonArray.getJSONObject(i);
                        String judul = json.getString("judul");
                        String tanggal = json.getString("tanggal");
                        String link = json.getString("link");
                        String gambar = json.getString("gambar");

                        BeritaModel beritaObject = new BeritaModel(judul,link,gambar,tanggal);
                        mListData.add(beritaObject);
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