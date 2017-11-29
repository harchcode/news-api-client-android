package com.hartonochandra.getnews;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

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

public class SourceActivity extends AppCompatActivity {
    private ListView sourceListView;
    private DownloadTask task;
    private ProgressBar progressBar;

    private class DownloadTask extends AsyncTask<String, Void, String> {
        private SourceActivity sourceActivity;

        public DownloadTask(SourceActivity sourceActivity) {
            this.sourceActivity = sourceActivity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            sourceListView.setVisibility(View.INVISIBLE);
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

            sourceListView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);

            try {

                JSONObject jsonObject = new JSONObject(result);
                JSONArray arr = jsonObject.getJSONArray("sources");

                ArrayList<Source> sources = new ArrayList<Source>();

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject jsonPart = arr.getJSONObject(i);
                    Source source = Source.fromJSONObject(jsonPart);
                    sources.add(source);
                }

                sourceActivity.fillListView(sources);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    void initActionBar() {
        android.app.ActionBar actionBar = getActionBar();
        ActionBar supportActionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle("Source List");
        }
        supportActionBar.setTitle("Source List");
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

        task = new DownloadTask(this);
        task.execute("https://newsapi.org/v2/sources?category=technology&language=en");
    }

    @Override
    protected  void onPause() {
        super.onPause();

        task.cancel(false);
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
