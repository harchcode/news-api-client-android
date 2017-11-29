package com.hartonochandra.getnews;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

public class ArticleContentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_content);

        initActionBar();

        Intent intent = getIntent();
        String articleUrl = intent.getStringExtra("articleUrl");

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

    void initActionBar() {
        android.app.ActionBar actionBar = getActionBar();
        ActionBar supportActionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle("Content");
        }
        supportActionBar.setTitle("Content");
    }
}
