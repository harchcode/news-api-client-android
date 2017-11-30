/*
 * Created by Hartono Chandra
 */

package com.hartonochandra.getnews;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Article model class
 */

public class Article {
    public String author;
    public String title;
    public String description;
    public String url;
    public String urlToImage;
    public String publishedAt;

    /**
     * @param jsonObject
     * @return new Article object from the given JSON Object
     */
    public static Article fromJSONObject(JSONObject jsonObject) {
        Article article = new Article();

        try {
            article.author = jsonObject.getString("author");
            article.title = jsonObject.getString("title");
            article.description = jsonObject.getString("description");
            article.url = jsonObject.getString("url");
            article.urlToImage = jsonObject.getString("urlToImage");
            article.publishedAt = jsonObject.getString("publishedAt");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return article;
    }
}
