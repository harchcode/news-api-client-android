/*
 * Created by Hartono Chandra
 */

package com.hartonochandra.getnews;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * The Source Activity
 */

public class SourceActivity extends AppCompatActivity {
    private ListView    sourceListView;
    private TextView    nodataTextView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source);

        Helper.setActionBarTitle(this, getString(R.string.source_title));

        nodataTextView = (TextView)findViewById(R.id.nodataTextView);
        sourceListView = (ListView)findViewById(R.id.sourceListView);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        getSources();
    }

    @Override
    protected void onPause() {
        super.onPause();

        NewsAPIClient.cancelAllRequests();
    }

    /**
     * Requests source list from newsapi.org.
     */
    private void getSources() {
        RequestParams params = new RequestParams();
        params.put("category", "technology");
        params.put("language", "en");

        NewsAPIClient.get("sources", params, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                showProgress();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                hideProgress();

                processSourcesJSON(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                hideProgress();

                onRequestFailed();
            }
        });
    }

    private void showProgress() {
        sourceListView.setVisibility(View.INVISIBLE);
        nodataTextView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        sourceListView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void showNoData() {
        nodataTextView.setVisibility(View.VISIBLE);
    }

    private void processSourcesJSON(JSONObject json) {
        try {
            JSONArray arr = json.getJSONArray("sources");

            ArrayList<Source> sources = new ArrayList<Source>();

            int arrLength = arr.length();

            for (int i = 0; i < arrLength; i++) {
                JSONObject jsonPart = arr.getJSONObject(i);
                Source source = Source.fromJSONObject(jsonPart);
                sources.add(source);
            }

            SourceActivity.this.fillListView(sources);
        } catch (JSONException e) {
            Helper.toast(this, getString(R.string.error_json));
        }
    }

    private void onRequestFailed() {
        Helper.toast(this, getString(R.string.error_server));
    }

    private void fillListView(final ArrayList<Source> sources) {
        SourceAdapter adapter = new SourceAdapter(this, sources);
        sourceListView.setAdapter(adapter);

        sourceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), ArticleActivity.class);
                intent.putExtra("sourceId", sources.get(i).id);
                intent.putExtra("sourceName", sources.get(i).name);
                startActivity(intent);
            }
        });

        if (sources.size() <= 0) { showNoData(); }
    }
}
