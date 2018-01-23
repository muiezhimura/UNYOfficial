package com.infinite.rzzkan.uny.Fragment;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.infinite.rzzkan.uny.Adapter.HomeAdapter;
import com.infinite.rzzkan.uny.DetailsActivity;
import com.infinite.rzzkan.uny.Interface.OnSliderClickListener;
import com.infinite.rzzkan.uny.Model.SliderModel;
import com.infinite.rzzkan.uny.PrestasiActivity;
import com.infinite.rzzkan.uny.R;
import com.infinite.rzzkan.uny.SejarahActivity;
import com.infinite.rzzkan.uny.VisimisiActivity;

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
public class HomeFragment extends Fragment {
   // protected int page = 1;
    private String API_URL = "http://api.ngeartstudio.com/";

    private RecyclerView mList_slider = null;
    private HomeAdapter adapter = null;
    private ArrayList<SliderModel> mListData = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private CardView sejarah = null;
    private CardView visimisi = null;
    private CardView prestasi = null;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        getActivity().setTitle("UNY News Apps");
        mList_slider = (RecyclerView) view.findViewById(R.id.mList_slider);
        new RequestAPI().execute();
        initList(mListData);
        linearLayoutManager = new LinearLayoutManager(getActivity(), linearLayoutManager.HORIZONTAL, false);
        sejarah = (CardView) view.findViewById(R.id.cardView_sejarah);
        visimisi = (CardView) view.findViewById(R.id.cardView_visimisi);
        prestasi = (CardView) view.findViewById(R.id.cardView_prestasi);

        sejarah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goPindah = new Intent(getActivity(), SejarahActivity.class);
                startActivity(goPindah);
            }
        });

        visimisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goPindah = new Intent(getActivity(), VisimisiActivity.class);
                startActivity(goPindah);
            }
        });

        prestasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goPindah = new Intent(getActivity(), PrestasiActivity.class);
                startActivity(goPindah);
            }
        });


        return view;
    }

    private void initList(final ArrayList<SliderModel> mListData) {
        mList_slider.setLayoutManager(linearLayoutManager);
        adapter = new HomeAdapter(getActivity(), mListData, new OnSliderClickListener() {
            @Override
            public void onSliderClick(SliderModel item) {
                SliderModel sliderDataModel = item;
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra(SliderModel.class.toString(), sliderDataModel);
                startActivity(intent);
            }
        });

        mList_slider.setAdapter(adapter);

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
            HttpGet httpGet = new HttpGet(API_URL);
            Log.e("URL", API_URL);
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
                    mListData = parseSliderListFromResponse(jsonObject);
                    initList(mListData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public ArrayList<SliderModel> parseSliderListFromResponse(JSONObject jsonObject) {
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("sliders");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                SliderModel Object = new SliderModel();
                String slider = json.getString("slider");
                Object.setSlider(slider);
                mListData.add( Object);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return mListData;

    }

}
