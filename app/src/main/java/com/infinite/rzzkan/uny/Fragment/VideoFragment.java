package com.infinite.rzzkan.uny.Fragment;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.infinite.rzzkan.uny.DetailsActivity;
import com.infinite.rzzkan.uny.Interface.OnVideoClickListener;
import com.infinite.rzzkan.uny.R;
import com.infinite.rzzkan.uny.Adapter.VideoAdapter;
import com.infinite.rzzkan.uny.Model.VideoModel;

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
public class VideoFragment extends Fragment {
    private static String GOOGLE_YOUTUBE_API_KEY = "&key=AIzaSyCE8t1cq2plkWkrftYW6qMUF6FI6xaCGi8";
    private static String CHANNEL_ID = "&channelId=UCFfPBvdsP0ROarlekViw3Vw";
    private static String URL = "https://www.googleapis.com/youtube/v3/search?part=snippet&order=date";
    private static String RESULT = "&maxResults=20";
    private static String PAGE_TOKEN = "&pageToken=";
    private String Token = "";

    private RecyclerView mList_videos = null;
    private VideoAdapter adapter = null;
    private ArrayList<VideoModel> mListData = new ArrayList<>();

    private LinearLayoutManager linearLayoutManager;

    public VideoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        mList_videos = (RecyclerView) view.findViewById(R.id.mList_videos);
        initList(mListData);
        new RequestYoutubeAPI().execute();

        linearLayoutManager = new LinearLayoutManager(getActivity());
        mList_videos.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if(linearLayoutManager.findLastCompletelyVisibleItemPosition() == mListData.size()-1){
                   // Token = Token;
                    new RequestYoutubeAPI().execute();
                }
            }
        });

        return view;
    }

    private void initList(ArrayList<VideoModel> mListData) {
        mList_videos.setLayoutManager(linearLayoutManager);
        adapter = new VideoAdapter(getActivity(), mListData, new OnVideoClickListener() {
            @Override
            public void onVideoClick(VideoModel item) {
               VideoModel youtubeDataModel = item;
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra(VideoModel.class.toString(), youtubeDataModel);
                startActivity(intent);
            }
        });
        mList_videos.setAdapter(adapter);

    }


    //create an asynctask to get all the data from youtube
    private class RequestYoutubeAPI extends AsyncTask<Void, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(URL + CHANNEL_ID + RESULT + PAGE_TOKEN + Token +GOOGLE_YOUTUBE_API_KEY);
            Log.e("URL", URL + CHANNEL_ID + RESULT + PAGE_TOKEN + Token +GOOGLE_YOUTUBE_API_KEY);
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
                    mListData = parseVideoListFromResponse(jsonObject);
                    initList(mListData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public ArrayList<VideoModel> parseVideoListFromResponse(JSONObject jsonObject) {
        ArrayList<VideoModel> mList = new ArrayList<>();
        if (jsonObject.has("nextPageToken")){
            try {
                Token = jsonObject.getString("nextPageToken");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (jsonObject.has("items")) {
            try {
                JSONArray jsonArray = jsonObject.getJSONArray("items");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json = jsonArray.getJSONObject(i);
                    if (json.has("id")) {
                        JSONObject jsonID = json.getJSONObject("id");
                        String video_id = "";
                        if (jsonID.has("videoId")) {
                            video_id = jsonID.getString("videoId");
                        }
                        if (jsonID.has("kind")) {
                            if (jsonID.getString("kind").equals("youtube#video")) {
                                VideoModel youtubeObject = new VideoModel();
                                JSONObject jsonSnippet = json.getJSONObject("snippet");
                                String title = jsonSnippet.getString("title");
                                String description = jsonSnippet.getString("description");
                                String publishedAt = jsonSnippet.getString("publishedAt");
                                String thumbnail = jsonSnippet.getJSONObject("thumbnails").getJSONObject("high").getString("url");

                                youtubeObject.setTitle(title);
                                youtubeObject.setDescription(description);
                                youtubeObject.setPublishedAt(publishedAt);
                                youtubeObject.setThumbnail(thumbnail);
                                youtubeObject.setVideo_id(video_id);
                                youtubeObject.setPage_token(Token);
                                mList.add(youtubeObject);

                            }
                        }
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return mList;

    }

}
