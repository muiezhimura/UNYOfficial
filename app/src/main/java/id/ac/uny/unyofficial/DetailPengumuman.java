package id.ac.uny.unyofficial;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DetailPengumuman extends AppCompatActivity {

    protected String urlId;
    protected String baseUrl;
    protected String sourceUrl;

    protected PengumumanListOpenHelper mDB;
    protected PengumumanModel pengumuman;
    protected TextView mTitle;
    protected TextView mPostDate;
    protected WebView mContents;
    protected boolean useCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pengumuman);

        baseUrl = getResources().getString(R.string.base_url);
        sourceUrl = getResources().getString(R.string.pengumuman_item);
        Log.d("detailPeng", "sourceurl: "+ sourceUrl);

        mTitle = (TextView) findViewById(R.id.detail_pengumuman_title);
        mPostDate = (TextView) findViewById(R.id.detail_pengumuman_date);
        mContents = (WebView) findViewById(R.id.detail_pengumuman_contents);

        Intent intent = getIntent();
        urlId = intent.getStringExtra("id.ac.uny.unyofficial.pengumuman_item_id").replaceAll("^/+", "");
        sourceUrl = String.format(sourceUrl, urlId);
        Log.d("detailPeng", "sourceUrl pengumuman : "+ sourceUrl);

        mDB = new PengumumanListOpenHelper(this);

        JsoupAsyncTask jat = new JsoupAsyncTask();
        jat.execute();
    }

    protected void updatePengumuman() {
        if(pengumuman != null) {
            mTitle.setText(pengumuman.getTitle());
            mPostDate.setText(pengumuman.getFormattedPostDate());

            mContents.getSettings().setJavaScriptEnabled(false);
            mContents.getSettings().setLoadsImagesAutomatically(true);
            mContents.getSettings().setBuiltInZoomControls(true);
            mContents.getSettings().setDisplayZoomControls(false);
            mContents.getSettings().setSupportZoom(true);
            mContents.getSettings().setLoadWithOverviewMode(true);
            mContents.getSettings().setUseWideViewPort(true);

            mContents.loadDataWithBaseURL(baseUrl, pengumuman.getContents(), "text/html; charset=utf-8", "UTF-8", null);
            Log.d("contents", "updatePengumuman: "+pengumuman.getContents());
        }
    }

    protected class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {

        protected Document htmlDocument;
        protected String htmlString;
        protected boolean forceGet = false;
        protected PengumumanModel cache;

        public JsoupAsyncTask() {
            this.forceGet = false;
        }

        public JsoupAsyncTask(boolean forceGet) {
            this.forceGet = forceGet;
        }

        @Override
        protected Void doInBackground(Void... params) {
            PengumumanModel cache = mDB.getPengumumanByUrlId(urlId);
            if(forceGet || cache == null) {
                useCache = false;
                try {
                    Log.d("jsoup_task", "requesting pengumuman item = " + sourceUrl);
                    htmlDocument = Jsoup.connect(sourceUrl).get();
                    htmlString = htmlDocument.outerHtml();
//                Log.d("jsoup_task", "response dump\n" + htmlString);
                } catch (IOException e) {
                    e.printStackTrace();
//                Toast.makeText(DetailPengumuman.this, "An error occurred while loading", Toast.LENGTH_LONG).show();
                    Log.d("jsoup_task", "error loading pengumuman");
                }
            } else {
                useCache = true;
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if(useCache) {
                pengumuman = cache;
            } else {
                Element content = htmlDocument.select("#block-system-main > .content").first();

                String title = content.select("> .title a").first().text().trim();
//            String urlId = content.select("> .title a").first().attr("href");
//            urlId = urlId.substring(urlId.lastIndexOf("/")); // could also be sourceUrl

                // Parse post date
                Calendar cal = Calendar.getInstance();
                Element elmDate = content.getElementById("date-submitted");
                String rawDate = elmDate.select(".day").first().text().trim() + " " +
                        elmDate.select(".month").first().text().trim() + " " +
                        elmDate.select(".year").first().text().trim();
                try {
                    cal.setTime(new SimpleDateFormat("d MMMM d, yy", Locale.ENGLISH).parse(rawDate));
                } catch (ParseException e) {
                    cal = null;
//                e.printStackTrace();
                }

                String htmlContent = content.select(".node-pengumuman .field-item").first().html();
                String plainContent = content.select(".node-pengumuman .field-item").first().text();

                pengumuman = new PengumumanModel(title, cal, htmlContent, plainContent, urlId);
                // Update to database as cache
                if (mDB.checkExists(urlId)) {
                    int affectedRows = mDB.update(pengumuman);
                }

                updatePengumuman();
            }
        }
    }
}
