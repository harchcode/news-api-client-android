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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SourceActivity extends AppCompatActivity {
    private ListView sourceListView;
    private ProgressBar progressBar;
    private AsyncHttpClient httpClient;

    void initActionBar() {
        android.app.ActionBar actionBar = getActionBar();
        ActionBar supportActionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle("Source List");
        }

        if (supportActionBar != null) {
            supportActionBar.setTitle("Source List");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source);

        initActionBar();

        TextView nodataTextView = (TextView)findViewById(R.id.nodataTextView);
        nodataTextView.setVisibility(View.INVISIBLE);
        sourceListView = (ListView)findViewById(R.id.sourceListView);
        sourceListView.setEmptyView(nodataTextView);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        httpClient = new AsyncHttpClient();
        httpClient.addHeader("X-Api-Key", "f475e05b73974cc393c210ad1f0f1ac2");
        httpClient.get("https://newsapi.org/v2/sources?category=technology&language=en", null, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                sourceListView.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                sourceListView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);

                try {
                    JSONArray arr = response.getJSONArray("sources");

                    ArrayList<Source> sources = new ArrayList<Source>();

                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject jsonPart = arr.getJSONObject(i);
                        Source source = Source.fromJSONObject(jsonPart);
                        sources.add(source);
                    }

                    SourceActivity.this.fillListView(sources);

                } catch (JSONException e) {
                    Toast.makeText(SourceActivity.this,
                            "Error when reading data from server.",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                sourceListView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);

                Toast.makeText(SourceActivity.this,
                        "Failed to fetch data from server.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected  void onPause() {
        super.onPause();

        httpClient.cancelAllRequests(false);
    }

    public void fillListView(final ArrayList<Source> sources) {
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
    }
}
