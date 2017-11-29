package com.hartonochandra.getnews;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class ArticleActivity extends AppCompatActivity {
    private ListView articleListView;
    private ProgressBar progressBar;
    private DownloadTask task;
    private String sourceId;

    private class DownloadTask extends AsyncTask<String, Void, String> {
        private ArticleActivity articleActivity;

        public DownloadTask(ArticleActivity articleActivity) {
            this.articleActivity = articleActivity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            articleListView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... urls) {
            String result = "";

            URL url;
            HttpsURLConnection urlConnection = null;

            try {
                url = new URL(urls[0]);
                urlConnection = (HttpsURLConnection)url.openConnection();

                urlConnection.setRequestProperty("X-Api-Key", "f475e05b73974cc393c210ad1f0f1ac2");

                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while (data != -1) {
                    if (isCancelled()) {
                        return "";
                    }

                    char current = (char)data;
                    result += current;
                    data = reader.read();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            articleListView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);

            try {

                JSONObject jsonObject = new JSONObject(result);
                JSONArray arr = jsonObject.getJSONArray("articles");

                ArrayList<Article> articles = new ArrayList<Article>();

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject jsonPart = arr.getJSONObject(i);
                    Article article = Article.fromJSONObject(jsonPart);
                    articles.add(article);
                }

                articleActivity.fillListView(articles);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    void initActionBar(String sourceName) {
        android.app.ActionBar actionBar = getActionBar();
        ActionBar supportActionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle("Articles from " + sourceName);
        }
        supportActionBar.setTitle("Articles from " + sourceName);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        articleListView = (ListView)findViewById(R.id.articleListView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        Intent intent = getIntent();
        String sourceName = intent.getStringExtra("sourceName");
        sourceId = intent.getStringExtra("sourceId");

        initActionBar(sourceName);

        task = new DownloadTask(this);
        task.execute("https://newsapi.org/v2/everything?sources=" + sourceId + "&language=en&page=1");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            if (query == null || query == "") {
                task.cancel(false);
                task = new DownloadTask(this);
                task.execute("https://newsapi.org/v2/everything?sources=" + sourceId + "&language=en&page=1");
            } else {
                task.cancel(false);
                task = new DownloadTask(this);
                task.execute("https://newsapi.org/v2/everything?q=" + query + "&sources=" + sourceId + "&language=en&page=1");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.searchItem).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        final ArticleActivity activity = this;

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query == null || query == "") {
                    task.cancel(false);
                    task = new DownloadTask(activity);
                    task.execute("https://newsapi.org/v2/everything?sources=" + sourceId + "&language=en&page=1");

                    return true;
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (query == null || query == "") {
                    task.cancel(false);
                    task = new DownloadTask(activity);
                    task.execute("https://newsapi.org/v2/everything?sources=" + sourceId + "&language=en&page=1");

                    return true;
                }
                else {
                    return false;
//                    task.cancel(false);
//                    task = new DownloadTask(activity);
//                    task.execute("https://newsapi.org/v2/everything?q=" + query + "&sources=" + sourceId + "&language=en&page=1");
                }
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                task.cancel(false);
                task = new DownloadTask(activity);
                task.execute("https://newsapi.org/v2/everything?sources=" + sourceId + "&language=en&page=1");
                return true;
            }
        });

        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();

        task.cancel(false);
    }

    public void fillListView(final ArrayList<Article> articles) {
        ArticleAdapter adapter = new ArticleAdapter(this, articles);
        articleListView.setAdapter(adapter);

        articleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), ArticleContentActivity.class);
                intent.putExtra("articleUrl", articles.get(i).url);
                startActivity(intent);
            }
        });
    }
}
