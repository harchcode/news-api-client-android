package com.hartonochandra.getnews;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class Helper {
    public static String httpGet(String urlString) {
        String result = "";

        URL url;
        HttpsURLConnection urlConnection = null;

        try {
            url = new URL(urlString);
            urlConnection = (HttpsURLConnection)url.openConnection();

            InputStream in = urlConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);

            int data = reader.read();

            while (data != -1) {
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
}
