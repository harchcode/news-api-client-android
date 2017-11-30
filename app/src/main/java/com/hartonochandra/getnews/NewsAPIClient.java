package com.hartonochandra.getnews;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class NewsAPIClient {
    private static String          baseUrl;
    private static AsyncHttpClient client;

    public static void init(String baseUrl, String apiKey) {
        NewsAPIClient.baseUrl = baseUrl;

        client = new AsyncHttpClient();
        client.addHeader("X-Api-Key", apiKey);
    }

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return baseUrl + relativeUrl;
    }

    public static void cancelAllRequests() {
        client.cancelAllRequests(false);
    }
}
