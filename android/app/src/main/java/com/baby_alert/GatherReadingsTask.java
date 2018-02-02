package com.baby_alert;


import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

public class GatherReadingsTask extends AsyncTask<String, Void, Reading> {

    private ReadingsGatheredCallback<Reading> callback;

    private void setCallback(ReadingsGatheredCallback<Reading> callback) {
        this.callback = callback;
    }

    GatherReadingsTask(ReadingsGatheredCallback<Reading> callback) {
        setCallback(callback);
    }

    @Override
    protected void onPreExecute() {
        if (callback != null) {
            NetworkInfo networkInfo = callback.getActiveNetworkInfo();
            if (networkInfo == null || !networkInfo.isConnected() ||
                    (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                            && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
                // If no connectivity, cancel task and update Callback with null data.
                callback.onReadingNotGathered("The phone is not connected to network");
                cancel(true);
            }
        }
    }

    @Override
    protected Reading doInBackground(String... hosts) {
        String host = hosts[0];
        try {
            URL url = new URL(String.format("http://%s/measurements/last", host));
            JSONObject json = downloadUrl(url);
            return Reading.parseResult(json);
        } catch (Exception e) {
            return new Reading(e);
        }
    }

    @Override
    protected void onPostExecute(Reading reading) {
        if (reading.getException() != null) {
            callback.onReadingNotGathered(reading.getException().getMessage());
        }
        else {
            callback.onReadingGathered(reading);
        }
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    private JSONObject downloadUrl(URL url) throws JSONException, IOException {
        InputStream is = url.openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            return new JSONObject(jsonText);
        } finally {
            is.close();
        }
    }
}
