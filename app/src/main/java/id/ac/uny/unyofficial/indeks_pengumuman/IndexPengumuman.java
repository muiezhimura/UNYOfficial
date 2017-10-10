package id.ac.uny.unyofficial.indeks_pengumuman;


import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import id.ac.uny.unyofficial.R;



public class IndexPengumuman extends AppCompatActivity {
    private RecyclerView rv_ipengumuman;
    private PengumumanAdapter adapter;
    ArrayList<HashMap<String, String>> arraylist;
    String url = "https://www.uny.ac.id/index-pengumuman/";
    static String TITLE = "title";
    static String DATE = "date";
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index_pengumuman);
//        adapter.setOnClickListener((adapterPosition, view) -> startActivity(new Intent(IndexPengumuman.this,DetailPengumuman.class)));
        new JsoupPengumuman().execute();
    }


    public class JsoupPengumuman extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(IndexPengumuman.this);
            mProgressDialog.setTitle("Pengumuman");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            arraylist = new ArrayList<HashMap<String, String>>();
            try {
                Document doc = Jsoup.connect(url).get();

                for(Element table : doc.select("tbody")){
                    for (Element row : table.select("tr")  ){
                        HashMap<String, String> map = new HashMap<>();
                        Elements tds = row.select("td");
                        String date = tds.get(2).text().toString();
                        String baru = date.substring(30,31);
                        map.put("title", tds.get(1).text());
                        map.put("date",baru);
                        arraylist.add(map);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            rv_ipengumuman = (RecyclerView) findViewById(R.id.rv_pengumuman);
            LinearLayoutManager layoutManager = new LinearLayoutManager(IndexPengumuman.this, LinearLayoutManager.VERTICAL, false);
            rv_ipengumuman.setLayoutManager(layoutManager);
            adapter = new PengumumanAdapter(IndexPengumuman.this,arraylist);
            rv_ipengumuman.setAdapter(adapter);
            mProgressDialog.dismiss();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
