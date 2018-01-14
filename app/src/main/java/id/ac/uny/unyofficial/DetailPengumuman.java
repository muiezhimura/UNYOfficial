package id.ac.uny.unyofficial;

import android.content.Intent;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_detail_pengumuman);
        setSupportActionBar(toolbar);

        baseUrl = getResources().getString(R.string.base_url);
        sourceUrl = getResources().getString(R.string.pengumuman_item);

        mTitle = (TextView) findViewById(R.id.detail_pengumuman_title);
        mPostDate = (TextView) findViewById(R.id.detail_pengumuman_date);
        mContents = (WebView) findViewById(R.id.detail_pengumuman_contents);

        Intent intent = getIntent();
        urlId = intent.getStringExtra("id.ac.uny.unyofficial.pengumuman_item_id").replaceAll("^/+", "");
        sourceUrl = String.format(sourceUrl, urlId);

        mDB = new PengumumanListOpenHelper(this);


        pengumuman = mDB.getPengumumanByUrlId(urlId);
        updatePengumuman();

        loadContent(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    protected void loadContent(final boolean forceGet) {
        NetworkInfoHelper nih = new NetworkInfoHelper();
        nih.doIfConnected(new NetworkInfoHelper.OnConnectionCallback() {
            @Override
            public void onConnectionSuccess() {}

            @Override
            public void onConnectionFailed(String errorMessage) {}

            @Override
            public void onConnectionFinished(Boolean result, String errorMessage) {
                JsoupAsyncTask jat = new JsoupAsyncTask(forceGet, result);
                jat.execute();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail_pengumuman, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_detail_refresh:
                loadContent(true);
                break;
            case R.id.action_detail_open_link:
                // Implicit intent
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(sourceUrl));
                startActivity(intent);
                break;
            default:
                // Do nothing
        }

        return super.onOptionsItemSelected(item);
    }

    protected void updatePengumuman() {
        if(pengumuman != null) {
            // Do we need to clear the contents first?
            mContents.loadUrl("about:blank");

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
        }
    }

    protected class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {

        protected Document htmlDocument;
        protected String htmlString;
        protected boolean forceGet = false;
        protected boolean isConnected = true; // assume connected
        protected PengumumanModel cache;

        public JsoupAsyncTask(boolean forceGet) {
            this.forceGet = forceGet;
        }

        public JsoupAsyncTask(boolean forceGet, boolean isConnected) {
            this.forceGet = forceGet;
            this.isConnected = isConnected;
        }

        @Override
        protected Void doInBackground(Void... params) {
            PengumumanModel cache = mDB.getPengumumanByUrlId(urlId);
            if((isConnected && forceGet) || cache == null || cache.getContents().trim().equals("")) {
                useCache = false;
                try {
                    htmlDocument = Jsoup.connect(sourceUrl).get();
                    htmlString = htmlDocument.outerHtml();
                } catch (IOException e) {
                    e.printStackTrace();
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

                // Parse post date
                Calendar cal = Calendar.getInstance();
                Element elmDate = content.getElementById("date-submitted");
                String rawDate = elmDate.select(".day").first().text().trim() + " " +
                        elmDate.select(".month").first().text().trim() + " " +
                        elmDate.select(".year").first().text().trim();
                try {
                    cal.setTime(new SimpleDateFormat("d MMMM yy", Locale.ENGLISH).parse(rawDate));
                } catch (ParseException e) {
                    cal = null;
                }

                String htmlContent = content.select(".node-pengumuman .field-item").first().html();
                String plainContent = content.select(".node-pengumuman .field-item").first().text();

                Elements additionalContent = content.select(".node-pengumuman > .field:nth-child(n+2)");
                if(additionalContent.size() > 0) {
                    String additional = "";
                    for (Element elm : additionalContent) {
                        for (Element elm2 : elm.select(".field-label, .field-item")) {
                            additional += elm2.outerHtml();
                            plainContent += elm2.text();
                        }
                    }

                    // Wrap for bigger font size, so that something like a list of attachments
                    // could be easily clicked
                    htmlContent += String.format("<div style=\"font-size: 2em;\">%s</div>", additional);
                }

                if(pengumuman == null) {
                    pengumuman = new PengumumanModel(title, cal, htmlContent, plainContent, urlId);
                } else {
                    pengumuman.title = title;
                    pengumuman.contents = htmlContent;
                    pengumuman.plainContents = plainContent;
                }
                // Update to database as cache
                if (mDB.checkExists(urlId)) {
                    int affectedRows = mDB.update(pengumuman);
                }

            }

            updatePengumuman();
        }
    }
}
