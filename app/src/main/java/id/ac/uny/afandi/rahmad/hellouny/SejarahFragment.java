package id.ac.uny.afandi.rahmad.hellouny;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
 * Created by rmd on 1/18/2018.
 */

public class SejarahFragment extends Fragment {

    private RecyclerView recyclerView;
    protected int page;
    private GridLayoutManager gridLayoutManager;
    private List<Sejarah> list;
    private SejarahAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sejarah_fragment,container,false);
        getActivity().setTitle("Sejarah");
        recyclerView =  view.findViewById(R.id.recycler_view);
        list = new ArrayList<>();
        loadMyContent();
        gridLayoutManager = new GridLayoutManager(getActivity(),1);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter = new SejarahAdapter(getActivity(),list);
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void loadMyContent() {
        AsyncTask<Void,Void,Void> task =new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://api.ngeartstudio.com/sejarah/")
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    JSONObject object = new JSONObject(response.body().string());
                    JSONArray array = object.getJSONArray("sejarah");
                    for (int i=0;i<array.length();i++){
                        JSONObject obj = array.getJSONObject(i);
                        Sejarah sejarah = new Sejarah(obj.getString("sejarah"));
                        list.add(sejarah);
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
        task.execute();
    }
}
