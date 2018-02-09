package com.example.android.quakereport;

/**
 * Created by ashu on 27-10-2017.
 */


public class EarthquakeInfo {

    private String mMagnitude;
    private String mCountryName;
    private long mDate;
    private String mUrl;


    public EarthquakeInfo(String magnitude, String countryName, long date, String url) {
        mMagnitude = magnitude;
        mCountryName = countryName;
        mDate = date;
        mUrl = url;
    }

    public String getMagnitude() {
        return mMagnitude;
    }

    public String getCountryName() {
        return mCountryName;
    }

    public long getDate() {
        return mDate;
    }

    public String getUrl() {
        return mUrl;
    }
}
