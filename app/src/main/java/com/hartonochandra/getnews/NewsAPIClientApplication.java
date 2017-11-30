package com.hartonochandra.getnews;

import android.app.Application;

public class NewsAPIClientApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        NewsAPIClient.init(getString(R.string.news_api_url), getString(R.string.news_api_key));
    }
}
