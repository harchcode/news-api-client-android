package com.hartonochandra.getnews;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ArticleActivity extends AppCompatActivity {
    class DownloadTask extends AsyncTask<String, Void, String> {
        private ArticleActivity articleActivity;

        public DownloadTask(ArticleActivity articleActivity) {
            this.articleActivity = articleActivity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        ArticleActivity.DownloadTask task = new ArticleActivity.DownloadTask(this);
        task.execute("https://newsapi.org/v2/articles?category=technology&language=en&apiKey=f475e05b73974cc393c210ad1f0f1ac2");
    }

    public void fillListView(ArrayList<Article> articles) {
        ListView articleListView = (ListView)findViewById(R.id.articleListView);
        ArticleAdapter adapter = new ArticleAdapter(this, articles);
        articleListView.setAdapter(adapter);
    }
}
