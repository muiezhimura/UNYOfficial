package com.infinite.rzzkan.uny.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.infinite.rzzkan.uny.Adapter.BeritaDetailAdapter;
import com.infinite.rzzkan.uny.Model.BeritaDetailModel;
import com.infinite.rzzkan.uny.R;

import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BeritaDetailFragment extends android.app.Fragment {
    private RecyclerView recyclerView;
    protected int page;
    private GridLayoutManager gridLayoutManager;
    private List<BeritaDetailModel> list;
    private BeritaDetailAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_berita_detail,container,false);
        getActivity().setTitle("Detail Berita");
        recyclerView =  view.findViewById(R.id.recycler_view);
        list = new ArrayList<>();
        page=1;
        String url = getArguments().getString("url");
        Log.d("url",url);
        loadMyContent(url);
        gridLayoutManager = new GridLayoutManager(getActivity(),1);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter = new BeritaDetailAdapter(getActivity(),list);
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void loadMyContent(final String url) {
        AsyncTask<String,Void,Void> task =new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... strings) {
                OkHttpClient client = new OkHttpClient();
                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("url",url)
                        .build();
                Request request = new Request.Builder()
                        .url("http://api.ngeartstudio.com/api/detail")
                        .post(requestBody)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    JSONObject obj = new JSONObject(response.body().string());
                    BeritaDetailModel berita = new BeritaDetailModel(obj.getString("judul"),obj.getString("gambar"),obj.getString("konten"));
                    list.add(berita);
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
        task.execute(url);
    }
}

