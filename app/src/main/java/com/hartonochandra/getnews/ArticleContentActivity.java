package com.hartonochandra.getnews;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

public class ArticleContentActivity extends AppCompatActivity {
    private String articleUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_content);

        Intent intent = getIntent();
        articleUrl = intent.getStringExtra("articleUrl");

        Helper.setActionBarTitle(this, articleUrl);
        Helper.enableActionBarBackButton(this);

        final ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar);
        final WebView articleContentWebView = (WebView)findViewById(R.id.articleContentWebView);

        // Disabled Javascript because too much ads
        WebSettings webSettings = articleContentWebView.getSettings();
        webSettings.setJavaScriptEnabled(false);

        articleContentWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress >= 100) {
                    progressBar.setVisibility(View.INVISIBLE);
                    articleContentWebView.setVisibility(View.VISIBLE);
                }
            }
        });

        articleContentWebView.loadUrl(articleUrl);
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
}
