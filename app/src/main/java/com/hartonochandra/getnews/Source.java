package com.hartonochandra.getnews;

import org.json.JSONException;
import org.json.JSONObject;

public class Source {
    public String id;
    public String name;
    public String description;
    public String url;
    public String category;
    public String language;
    public String country;

    public static Source fromJSONObject(JSONObject jsonObject) {
        Source source = new Source();

        try {
            source.id = jsonObject.getString("id");
            source.name = jsonObject.getString("name");
            source.description = jsonObject.getString("description");
            source.url = jsonObject.getString("url");
            source.category = jsonObject.getString("category");
            source.language = jsonObject.getString("language");
            source.country = jsonObject.getString("country");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return source;
    }
}
