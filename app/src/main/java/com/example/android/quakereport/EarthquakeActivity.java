/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<EarthquakeInfo>> {

    private static final int EARTHQUAKE_LOADER_ID = 1;
    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query";
    private EarthQuakeAdapter mAdapter;
    private ProgressBar progressBar;
    private TextView textView;
    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        progressBar = (ProgressBar) findViewById(R.id.indeterminateBar);
        textView = (TextView) findViewById(R.id.empty_list_item);
        listView = (ListView) findViewById(R.id.list);

        ListView earthquakeListView = (ListView) findViewById(R.id.list);
        mAdapter = new EarthQuakeAdapter(this, new ArrayList<EarthquakeInfo>());
        earthquakeListView.setAdapter(mAdapter);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                EarthquakeInfo earthQuake = mAdapter.getItem(i);

                String url = earthQuake.getUrl();

                CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
                        .addDefaultShareMenuItem()
                        .setToolbarColor(EarthquakeActivity.this.getResources().getColor(R.color.colorPrimary))
                        .setShowTitle(true)
                        .setStartAnimations(EarthquakeActivity.this, R.anim.slide_in_right, R.anim.slide_out_left)
                        .setExitAnimations(EarthquakeActivity.this, R.anim.slide_in_left, R.anim.slide_out_right)
                        .build();

                customTabsIntent.launchUrl(EarthquakeActivity.this, Uri.parse(url));
            }
        });


        LoaderManager loaderManager = getLoaderManager();

        if (checkInternetConnection()) {
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
        } else {
            progressBar.setVisibility(View.GONE);
            listView.setVisibility(View.GONE);
            textView.setText(R.string.No_Internet_Connection);
        }

    }


    @Override
    public Loader<List<EarthquakeInfo>> onCreateLoader(int i, Bundle bundle) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String minMagnitude = sharedPrefs.getString(getString(R.string.settings_min_magnitude_key), getString(R.string.settings_min_magnitude_default));

        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));


        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", "10");
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", orderBy);

        return new EarthquakeLoader(this, uriBuilder.toString());

    }


    @Override
    public void onLoadFinished(Loader<List<EarthquakeInfo>> loader, List<EarthquakeInfo> earthquakeInfos) {

        mAdapter.clear();

        if (earthquakeInfos != null && !earthquakeInfos.isEmpty()) {
            mAdapter.addAll(earthquakeInfos);
        }

        progressBar.setVisibility(View.GONE);

        listView.setEmptyView(textView);
    }

    @Override
    public void onLoaderReset(Loader<List<EarthquakeInfo>> loader) {
        mAdapter.clear();
    }


    public boolean checkInternetConnection() {
        ConnectivityManager cm =
                (ConnectivityManager) EarthquakeActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
