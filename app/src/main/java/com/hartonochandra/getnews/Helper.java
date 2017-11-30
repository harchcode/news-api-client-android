/*
 * Created by Hartono Chandra
 */

package com.hartonochandra.getnews;

import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Helper class
 * Provides common functionality that can be called by other classes.
 */

public class Helper {
    /**
     * Sets activity's action bar's title.
     * @param activity
     * @param title
     */
    public static void setActionBarTitle(AppCompatActivity activity, String title) {
        android.app.ActionBar actionBar = activity.getActionBar();
        ActionBar supportActionBar = activity.getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle(title);
        }

        if (supportActionBar != null) {
            supportActionBar.setTitle(title);
        }
    }

    /**
     * Enables back button on the activity's action bar.
     * @param activity
     */
    public static void enableActionBarBackButton(AppCompatActivity activity) {
        android.app.ActionBar actionBar = activity.getActionBar();
        ActionBar supportActionBar = activity.getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (supportActionBar != null) {
            supportActionBar.setHomeButtonEnabled(true);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Displays simple toast.
     * @param activity
     * @param text
     */
    public static void toast(Activity activity, String text) {
        Toast.makeText(activity,
                text,
                Toast.LENGTH_SHORT).show();
    }
}
