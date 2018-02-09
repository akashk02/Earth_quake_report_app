package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by ashu on 13-11-2017.
 */

public class EarthquakeLoader extends AsyncTaskLoader<List<EarthquakeInfo>> {

    private String stringUrl ;

    public EarthquakeLoader(Context context,String stringUrl){
        super(context);
        this.stringUrl = stringUrl ;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<EarthquakeInfo> loadInBackground() {

        if(stringUrl.equals("") || stringUrl == null){
            return null ;
        }

        List<EarthquakeInfo> result = QueryUtils.fetchEarthquakeData(stringUrl);

        return result ;
    }
}
