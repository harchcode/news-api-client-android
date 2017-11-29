package com.hartonochandra.getnews;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SourceActivity extends AppCompatActivity {
    class DownloadTask extends AsyncTask<String, Void, String> {
        private SourceActivity sourceActivity;

        public DownloadTask(SourceActivity sourceActivity) {
            this.sourceActivity = sourceActivity;
        }

        @Override
        protected String doInBackground(String... urls) {
            return Helper.httpGet(urls[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source);

        DownloadTask task = new DownloadTask(this);
        task.execute("https://newsapi.org/v2/sources?category=technology&language=en&apiKey=f475e05b73974cc393c210ad1f0f1ac2");
    }

    public void fillListView(ArrayList<Source> sources) {
        ListView sourceListView = (ListView)findViewById(R.id.sourceListView);
        SourceAdapter adapter = new SourceAdapter(this, sources);
        sourceListView.setAdapter(adapter);
    }
}
