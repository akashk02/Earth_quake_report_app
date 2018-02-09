package com.example.android.quakereport;

/**
 * Created by ashu on 29-10-2017.
 */

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {


    public static List<EarthquakeInfo> extractFeatureFromJson(String earthquakeJSON) {

        if(TextUtils.isEmpty(earthquakeJSON)){
            return null ;
        }

        List<EarthquakeInfo> earthquakes = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(earthquakeJSON);
            JSONArray featuresArray = root.getJSONArray("features");
            for (int i = 0; i < featuresArray.length(); i++) {
                JSONObject properties = featuresArray.getJSONObject(i).getJSONObject("properties");
                String magnitude = properties.getString("mag");
                String location = properties.getString("place");
                long time = properties.getLong("time");
                String url = properties.getString("url");
                earthquakes.add(new EarthquakeInfo(magnitude, location, time, url));
            }

        } catch (JSONException e) {
        }

        return earthquakes ;
    }


    public static List<EarthquakeInfo> fetchEarthquakeData(String requestUrl) {

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        URL url = createUrl(requestUrl);
        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);

        } catch (IOException e) {
        }

        List<EarthquakeInfo> earthQuake = extractFeatureFromJson(jsonResponse);

        return earthQuake ;
    }


    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
        }
        return url;
    }

    public static String makeHttpRequest(URL url) throws IOException {

        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(1000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }

        } catch (IOException e) {
        }

        finally {

            if(urlConnection != null)
            {
                urlConnection.disconnect();
            }

            if(inputStream != null)
            {
                inputStream.close();
            }

        }

        return jsonResponse ;
    }

    public static String readFromStream(InputStream inputStream) throws IOException {


        StringBuilder output = new StringBuilder();
        if(inputStream != null){

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while(line != null){
                output.append(line);
               line = reader.readLine();
            }
        }

        return output.toString();

    }






}
