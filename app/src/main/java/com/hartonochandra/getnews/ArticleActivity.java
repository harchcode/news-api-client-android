package com.hartonochandra.getnews;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

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

import cz.msebera.android.httpclient.Header;

public class ArticleActivity extends AppCompatActivity {
    private ListView    articleListView;
    private ProgressBar progressBar;
    private String      sourceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        TextView nodataTextView = (TextView)findViewById(R.id.nodataTextView);
        articleListView = (ListView)findViewById(R.id.articleListView);
        articleListView.setEmptyView(nodataTextView);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        Intent intent = getIntent();
        String sourceName = intent.getStringExtra("sourceName");
        sourceId = intent.getStringExtra("sourceId");

        String actionBarTitle = getString(R.string.article_title) + " " + sourceName;
        Helper.setActionBarTitle(this, actionBarTitle);
        Helper.enableActionBarBackButton(this);

        getArticles("");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.article_menu, menu);

        initSearchView(menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        NewsAPIClient.cancelAllRequests();
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            NewsAPIClient.cancelAllRequests();
            getArticles(query);
        }
    }

    private void initSearchView(Menu menu) {
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.searchItem).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }

            @Override
            public boolean onQueryTextChange(String query) {
                if (query == null || query.isEmpty()) {
                    NewsAPIClient.cancelAllRequests();
                    getArticles("");

                    return true;
                }
                else { return false; }
            }
        });
    }

    private void getArticles(String query) {
        RequestParams params = new RequestParams();

        if (query != null && !query.isEmpty()) {
            params.put("q", query);
        }

        params.put("sources", sourceId);
        params.put("language", "en");
        params.put("page", "1");

        NewsAPIClient.get("everything", params, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                showProgress();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                hideProgress();

                processArticlesJSON(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                hideProgress();

                onRequestFailed();
            }
        });
    }

    private void showProgress() {
        articleListView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        articleListView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void processArticlesJSON(JSONObject json) {
        try {
            JSONArray arr = json.getJSONArray("articles");

            ArrayList<Article> articles = new ArrayList<Article>();

            for (int i = 0; i < arr.length(); i++) {
                JSONObject jsonPart = arr.getJSONObject(i);
                Article article = Article.fromJSONObject(jsonPart);
                articles.add(article);
            }

            ArticleActivity.this.fillListView(articles);
        } catch (JSONException e) {
            Helper.toast(this, getString(R.string.error_json));
        }
    }

    private void onRequestFailed() {
        Helper.toast(this, getString(R.string.error_server));
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
