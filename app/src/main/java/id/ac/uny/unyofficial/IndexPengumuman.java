package id.ac.uny.unyofficial;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IndexPengumuman extends AppCompatActivity {

    protected Document htmlDocument;
    protected String htmlString;
    public String sourceUrl = "https://www.uny.ac.id/index-pengumuman";
    protected int page;
    protected boolean isLoading = false;
    protected boolean reachedEnd = false;

    protected RecyclerView mRecyclerView;
    protected LinearLayoutManager mLayoutManager;
    protected PengumumanListAdapter mAdapter;
    protected TextView mTextLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index_pengumuman);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mTextLoading = (TextView) findViewById(R.id.text_loading);

        page = 1;
        JsoupAsyncTask jat = new JsoupAsyncTask();
        jat.execute();
    }

    protected void updateList(ArrayList<PengumumanModel> list) {
        if(list.size() == 0) {
            return;
        }

        if(mAdapter == null) {
            // First time
            mAdapter = new PengumumanListAdapter(this, list);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setLayoutManager(mLayoutManager);

            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                // https://stackoverflow.com/a/36128493/7770384
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    if(isLoading) {
                        return;
                    }

                    int visibleItemCount = mLayoutManager.getChildCount();
                    int totalItemCount = mLayoutManager.getItemCount();
                    int pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();
                    // Sure, we could load when the scroll actually reached the end,
                    // but it's way better to load before the user actually reached
                    // the bottom so they don't have to wait for a long time.
                    //if(pastVisibleItems + visibleItemCount >= totalItemCount) {
                    if(pastVisibleItems + visibleItemCount + 5 >= totalItemCount) {
                        if(reachedEnd) {
                            Toast.makeText(IndexPengumuman.this, "Udah abis...", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            // End of list, request more content
                            Log.d("jsoup_task", "requesting more content...");
                            JsoupAsyncTask jat = new JsoupAsyncTask(++page);
                            Log.d("jsoup_task", "page is now " + page);
                            jat.execute();
                        }
                    }
                }
            });
        } else {
            // Subsequent calls; just merge the list
            mAdapter.mergeList(list);
        }

//        isLoading = false;
        setIsLoading(false);
    }

    protected void setIsLoading(boolean isLoading) {
        this.isLoading = isLoading;
        if(isLoading) {
            mRecyclerView.setPadding(0, 0, 0, Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics())));
            mTextLoading.setVisibility(View.VISIBLE);
        } else {
            mTextLoading.setVisibility(View.GONE);
            mRecyclerView.setPadding(0, 0, 0, 0);
        }
    }

    protected class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {

        protected int page;

        public JsoupAsyncTask() {
            this.page = 0;
        }

        public JsoupAsyncTask(int page) {
            this.page = Math.max(0, page - 1);
        }

        @Override
        protected Void doInBackground(Void... params) {
            if(!reachedEnd) {
                try {
                    Log.d("jsoup_task", "requesting to URL with page=" + page);
                    htmlDocument = Jsoup.connect(sourceUrl).data("page", String.valueOf(this.page)).get();
                    htmlString = htmlDocument.outerHtml();
                    Log.d("jsoup_task", "response dump\n" + htmlString);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            isLoading = true;
            setIsLoading(true);
        }

        @Override
        protected void onPostExecute(Void result) {
            // Parse HTML and get contents
            if(!reachedEnd) {
                Elements elms = htmlDocument.select("#block-system-main .views-table > tbody > tr");
                ArrayList<PengumumanModel> list = new ArrayList<>();
                Log.d("jsoup_task", "array size is " + elms.size());
                for (int i = 0; i < elms.size(); i++) {
                    Element elm = elms.get(i);
                    String title = elm.select(".views-field-title").text().trim();

                    String urlId = elm.select(".views-field-title a").attr("href");
                    urlId = urlId.substring(urlId.lastIndexOf("/"));
                    String postDateString = elm.select(".views-field-created").text().trim();
                    Calendar cal = Calendar.getInstance();

                    // Parse the date somehow
                    Matcher matchDate = Pattern.compile("Post date.*?,\\s?(\\w+ \\d+, \\d{4} - \\d+:\\d+).*?",
                            Pattern.CASE_INSENSITIVE).matcher(postDateString);

                    if (matchDate.find()) {
                        try {
                            postDateString = matchDate.group(1);
                            Log.d("post_date", "post date " + i + " acquired");
                            cal.setTime(new SimpleDateFormat("MMMM d, yyyy - kk:mm", Locale.ENGLISH).parse(postDateString));
                        } catch (ParseException e) {
                            cal = null;
                            Log.d("post_date", "post date " + i + " fails. post date is " + postDateString);
                        }
                    } else {
                        cal = null;
                        Log.d("post_date", "post date " + i + "no match");
                    }

                    list.add(new PengumumanModel(title, cal, "", urlId));
                }

                updateList(list);

                // Check, have we reached the end?
                if(elms.size() == 0) {
                    reachedEnd = true;
                }
            }
            isLoading = false;
            setIsLoading(false);
        }
    }
}
