package id.ac.uny.unyofficial;

import android.content.Intent;
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

import java.io.IOException;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IndexPengumuman extends AppCompatActivity {

    public static final int DISPLAY_PER_PAGE = 20;

    public String sourceUrl;
    protected int page;
    protected boolean isLoading = false;
    protected boolean reachedEnd = false;

    protected RecyclerView mRecyclerView;
    protected LinearLayoutManager mLayoutManager;
    protected PengumumanListAdapter mAdapter;
    protected TextView mTextLoading;

    // Database instance
    PengumumanListOpenHelper mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index_pengumuman);
        setTitle(R.string.title_pengumuman_list);

        sourceUrl = getResources().getString(R.string.pengumuman_list);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mTextLoading = (TextView) findViewById(R.id.text_loading);

        mDB = new PengumumanListOpenHelper(this);

        page = 1;
        loadContent();
    }

    protected void loadContent() {
        NetworkInfoHelper nih = new NetworkInfoHelper();
        nih.doIfConnected(new NetworkInfoHelper.OnConnectionCallback() {
            @Override
            public void onConnectionSuccess() {}

            @Override
            public void onConnectionFailed(String errorMessage) {}

            @Override
            public void onConnectionFinished(Boolean result, String errorMessage) {
                JsoupAsyncTask jat = new JsoupAsyncTask(page, result);
                jat.execute();
            }
        });
    }

    protected void updateList(ArrayList<PengumumanModel> list) {
        if(list.size() == 0) {
            return;
        }

        if(mAdapter == null) {
            // First time
            mAdapter = new PengumumanListAdapter(this, list);
            mAdapter.setOnClickListener(new PengumumanListAdapter.onClickListener() {
                @Override
                public void onClick(View v, int adapterPosition) {
                    PengumumanModel current = mAdapter.mPengList.get(adapterPosition);

                    Intent intent = new Intent(v.getContext(), DetailPengumuman.class);
                    intent.putExtra("id.ac.uny.unyofficial.pengumuman_item_id", current.getUrlIdentifier());

                    startActivity(intent);
                }
            });
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
                            page++;
                            loadContent();
                        }
                    }
                }
            });
        } else {
            // Subsequent calls; just merge the list
            mAdapter.mergeList(list);
        }

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
        protected boolean useCache;
        protected Document htmlDocument;
        protected String htmlString;
        protected boolean isConnected = true; // assume connected
        protected ArrayList<PengumumanModel> cache;

        public JsoupAsyncTask() {
            this.page = 0;
        }

        public JsoupAsyncTask(int page) {
            this.page = Math.max(0, page - 1);
        }

        public JsoupAsyncTask(boolean isConnected) {
            this.page = 0;
            this.isConnected = isConnected;
        }

        public JsoupAsyncTask(int page, boolean isConnected) {
            this.page = Math.max(0, page - 1);
            this.isConnected = isConnected;
        }

        @Override
        protected Void doInBackground(Void... params) {
            if(!reachedEnd) {
                // Check cache first
                cache = mDB.getPengumumanByPage(page);

                // We will always want to populate the top results with up-to-date items
                if(isConnected && (page == 0 || cache.size() == 0)) {
                    Log.d("qwerty", "doInBackground: NOT use cache");
                    useCache = false;
                    try {
                        Log.d("jsoup_task", "requesting to URL with page=" + page);
                        htmlDocument = Jsoup.connect(sourceUrl).data("page", String.valueOf(this.page)).get();
                        htmlString = htmlDocument.outerHtml();
                        Log.d("jsoup_task", "response dump\n" + htmlString);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d("qwerty", "doInBackground: use cache");
                    useCache = true;
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            setIsLoading(true);
        }

        @Override
        protected void onPostExecute(Void result) {
            if(!reachedEnd) {
                if(useCache) {
                    // Toast.makeText(IndexPengumuman.this, "cache for page " + page, Toast.LENGTH_LONG).show();
                    updateList(cache);
                } else {
                    // Toast.makeText(IndexPengumuman.this, "new content for page " + page, Toast.LENGTH_LONG).show();
                    // Parse HTML and get contents
                    Elements elms = htmlDocument.select("#block-system-main .views-table > tbody > tr");
                    ArrayList<PengumumanModel> list = new ArrayList<>();
                    Log.d("jsoup_task", "array size is " + elms.size());
                    for (int i = 0; i < elms.size(); i++) {
                        Element elm = elms.get(i);
                        String title = elm.select(".views-field-title").first().text().trim();

                        String urlId = elm.select(".views-field-title a").first().attr("href");
                        urlId = urlId.substring(urlId.lastIndexOf("/") + 1);
                        String postDateString = elm.select(".views-field-created").first().text().trim();
                        Calendar cal = Calendar.getInstance();

                        // Parse the date somehow
                        Matcher matchDate = Pattern.compile("Post date.*?,\\s?(\\w+ \\d+, \\d{4} - \\d+:\\d+).*?",
                                Pattern.CASE_INSENSITIVE).matcher(postDateString);

                        if (matchDate.find()) {
                            try {
                                postDateString = matchDate.group(1);
                                cal.setTime(new SimpleDateFormat("MMMM d, yy - kk:mm", Locale.ENGLISH).parse(postDateString));
                            } catch (ParseException e) {
                                cal = null;
                            }
                        } else {
                            cal = null;
                        }

                        PengumumanModel item = new PengumumanModel(title, cal, "", "", urlId);
                        list.add(item);
                        // Insert to database as cache
                        if (!mDB.checkExists(urlId)) {
                            long dbId = mDB.insert(item);
                            if (dbId > 0) {
                                item.setDbId(dbId);
                                PengumumanModel item2 = mDB.getPengumumanByUrlId(urlId);
                            }
                        }
                    }

                    updateList(list);

                    // Check, have we reached the end?
                    if (elms.size() == 0) {
                        reachedEnd = true;
                    }
                }
            }
            isLoading = false;
            setIsLoading(false);
        }
    }
}
