/*
 * Created by Hartono Chandra
 */

package com.hartonochandra.getnews;

import android.app.Application;

/**
 * Custom Appllication class.
 * Needed to initialize the NewsAPIClient class.
 */

public class NewsAPIClientApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        NewsAPIClient.init(getString(R.string.news_api_url), getString(R.string.news_api_key));
    }
}
