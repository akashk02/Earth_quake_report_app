package com.example.android.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * Created by ashu on 27-10-2017.
 */

public class EarthQuakeAdapter extends ArrayAdapter<EarthquakeInfo> {

    private final String LOCATION_SEPERATOR = "of";
    private String primaryLocation;
    private String locationOffset;
    private String orignalLocation;
    private String magnitude;
    private String url;


    public EarthQuakeAdapter(Context context, ArrayList<EarthquakeInfo> info) {
        super(context, 0, info);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        EarthquakeInfo obj = getItem(position);
        TextView magnitudeTextView = (TextView) listItemView.findViewById(R.id.magnitude);
        magnitude = formatMagnitude(obj.getMagnitude());
        magnitudeTextView.setText(magnitude);


        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeTextView.getBackground();
        int magnitudeColor = getMagnitudeColor(obj.getMagnitude());
        magnitudeCircle.setColor(magnitudeColor);


        orignalLocation = obj.getCountryName();
        int i = orignalLocation.indexOf(LOCATION_SEPERATOR);
        if (orignalLocation.contains(LOCATION_SEPERATOR)) {
            locationOffset = orignalLocation.substring(0, i) + LOCATION_SEPERATOR;
            primaryLocation = orignalLocation.substring(i + 2);

        } else {
            locationOffset = getContext().getString(R.string.Near_the);
            primaryLocation = orignalLocation;
        }


        TextView primaryLocationTextView = (TextView) listItemView.findViewById(R.id.primaryLocation);
        primaryLocationTextView.setText(primaryLocation);

        TextView locationOffdetTextView = (TextView) listItemView.findViewById(R.id.locationOffset);
        locationOffdetTextView.setText(locationOffset);


        Date dateObject = new Date(obj.getDate());
        String formattedDate = formatDate(dateObject);
        String formattedTime = formatTime(dateObject);
        TextView dateTextView = (TextView) listItemView.findViewById(R.id.countryDate);
        dateTextView.setText(formattedDate);

        TextView timeTextView = (TextView) listItemView.findViewById(R.id.countryTime);
        timeTextView.setText(formattedTime);

        return listItemView;

    }

    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }

    private String formatMagnitude(String number) {
        double num = Double.parseDouble(number);
        DecimalFormat formatter = new DecimalFormat("0.0");
        return formatter.format(num);

    }

    private int getMagnitudeColor(String mag) {
        int magnitudeColorResourceId;
        double magnitude = Double.parseDouble(mag);

        int magnitudeFloor = (int) Math.floor(magnitude);

        switch (magnitudeFloor) {

            case 0:
                magnitudeColorResourceId = R.color.magnitude1;
                break;

            case 1:
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;

            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;

            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;

            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;

            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;

            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;

            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;

            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;

            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }

        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);

    }


}
