package com.infinite.rzzkan.uny.Fragment;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.infinite.rzzkan.uny.Adapter.PengumumanAdapter;
import com.infinite.rzzkan.uny.DetailsActivity;
import com.infinite.rzzkan.uny.Interface.OnPengumumanClickListener;
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
        mList_pengumuman = (RecyclerView) view.findViewById(R.id.mList_pengumuman);
        new RequestAPI().execute();
        initList(mListData);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        mList_pengumuman.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

    private void initList(final ArrayList<PengumumanModel> mListData) {
        mList_pengumuman.setLayoutManager(linearLayoutManager);
        adapter = new PengumumanAdapter(getActivity(), mListData, new OnPengumumanClickListener() {
            @Override
            public void onPengumumanClick(PengumumanModel item) {
                PengumumanModel pengumumanDataModel = item;
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra(PengumumanModel.class.toString(), pengumumanDataModel);
                startActivity(intent);
            }
        });

        mList_pengumuman.setAdapter(adapter);

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
                    mListData = parsePengumumanListFromResponse(jsonObject);
                    initList(mListData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public ArrayList<PengumumanModel> parsePengumumanListFromResponse(JSONObject jsonObject) {
       // ArrayList<PengumumanModel> mList = new ArrayList<>();
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("pengumuman");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                PengumumanModel pengumumanObject = new PengumumanModel();
                String judul = json.getString("judul");
                String tanggal = json.getString("tanggal");
                String link = json.getString("link");

                pengumumanObject.setJudul(judul);
                pengumumanObject.setTanggal(tanggal);
                pengumumanObject.setLink(link);
                mListData.add(pengumumanObject);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }

        return mListData;

    }

}

