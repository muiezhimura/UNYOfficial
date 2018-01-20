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
import com.infinite.rzzkan.uny.DetailsActivity;
import com.infinite.rzzkan.uny.Interface.OnBeritaClickListener;
import com.infinite.rzzkan.uny.Model.BeritaModel;
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
        mList_berita = (RecyclerView) view.findViewById(R.id.mList_berita);
        new RequestAPI().execute();
        initList(mListData);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        mList_berita.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if(linearLayoutManager.findLastCompletelyVisibleItemPosition() == mListData.size()-1){
                    page+=1;
                    new RequestAPI().execute();
                }
            }
        });

        return view;
    }

    private void initList(final ArrayList<BeritaModel> mListData) {
        mList_berita.setLayoutManager(linearLayoutManager);
        adapter = new BeritaAdapter(getActivity(), mListData, new OnBeritaClickListener() {
            @Override
            public void onBeritaClick(BeritaModel item) {
               BeritaModel beritaDataModel = item;
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra(BeritaModel.class.toString(), beritaDataModel);
                startActivity(intent);
            }
        });

        mList_berita.setAdapter(adapter);

    }

    //create an asynctask to get all the data
    private class RequestAPI extends AsyncTask<Void, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(API_URL + page );
            Log.e("URL", API_URL+ page);
            try {
                HttpResponse response = httpClient.execute(httpGet);
                HttpEntity httpEntity = response.getEntity();
                String json = EntityUtils.toString(httpEntity);
                return json;
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if (response != null) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("response", jsonObject.toString());
                    mListData = parseBeritaListFromResponse(jsonObject);
                    initList(mListData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public ArrayList<BeritaModel> parseBeritaListFromResponse(JSONObject jsonObject) {
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("berita");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                BeritaModel beritaObject = new BeritaModel();
                String judul = json.getString("judul");
                String tanggal = json.getString("tanggal");
                String link = json.getString("link");
                String gambar = json.getString("gambar");

                beritaObject.setJudul(judul);
                beritaObject.setTanggal(tanggal);
                beritaObject.setLink(link);
                beritaObject.setGambar(gambar);
                mListData.add( beritaObject);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return mListData;

    }

}