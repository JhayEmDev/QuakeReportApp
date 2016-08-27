package com.example.android.quakereport.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.android.quakereport.Model.Earthquake;
import com.example.android.quakereport.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

public class EarthquakesAdapter extends BaseAdapter {

  // Fields
  private ArrayList<Earthquake> mEarthquakes;

  // Misc
  Context mContext;
  LayoutInflater mLayoutInflater;
  DecimalFormat mDecimalFormatter;

  public EarthquakesAdapter(Context context, ArrayList<Earthquake> earthquakes) {
    mEarthquakes = earthquakes;
    mContext = context;
    mLayoutInflater = ((Activity) mContext).getLayoutInflater();
    mDecimalFormatter = new DecimalFormat("0.0");
  }

  public void setEarthquakes(ArrayList<Earthquake> earthquakes) {
    mEarthquakes = earthquakes;
  }

  @Override
  public int getCount() {
    return mEarthquakes.size();
  }

  @Override
  public Object getItem(int position) {
    return mEarthquakes.get(position);
  }

  @Override
  public long getItemId(int position) {
    return 0;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder viewHolder;

    if (convertView == null) {
      convertView = mLayoutInflater.inflate(R.layout.list_item, parent, false);

      viewHolder = new ViewHolder();
      viewHolder.textViewMagnitude = (TextView) convertView.findViewById(R.id.textViewMagnitude);
      viewHolder.textViewOffset = (TextView) convertView.findViewById(R.id.textViewOffset);
      viewHolder.textViewLocation = (TextView) convertView.findViewById(R.id.textViewPlace);
      viewHolder.textViewDate = (TextView) convertView.findViewById(R.id.textViewDate);
      viewHolder.textViewTime = (TextView) convertView.findViewById(R.id.textViewTime);

      convertView.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) convertView.getTag();
    }

    Earthquake earthquakeHolder = (Earthquake) getItem(position);
    viewHolder.textViewMagnitude.setText(String.format(Locale.getDefault(), "%s", mDecimalFormatter.format(earthquakeHolder.getMagnitude())));
    viewHolder.textViewOffset.setText(String.format(Locale.getDefault(), "%s", earthquakeHolder.getOffset()));
    viewHolder.textViewLocation.setText(String.format(Locale.getDefault(), "%s", earthquakeHolder.getLocation()));
    viewHolder.textViewDate.setText(String.format(Locale.getDefault(), "%s", earthquakeHolder.getDateDay()));
    viewHolder.textViewTime.setText(String.format(Locale.getDefault(), "%s", earthquakeHolder.getDateTime()));

    GradientDrawable magnitudeCircle = (GradientDrawable) viewHolder.textViewMagnitude.getBackground();
    magnitudeCircle.setColor(getMagnitudeColor(earthquakeHolder.getMagnitude()));

    return convertView;
  }

  private static class ViewHolder {
    TextView textViewMagnitude;
    TextView textViewLocation;
    TextView textViewOffset;
    TextView textViewDate;
    TextView textViewTime;
  }

  // Helper method
  private int getMagnitudeColor(double magnitude) {
    int magnitudeColor;

    int magnitudeFloor = (int) Math.floor(magnitude);
    switch (magnitudeFloor) {
      case 0:
      case 1:
        magnitudeColor = R.color.magnitude1;
        break;
      case 2:
        magnitudeColor = R.color.magnitude2;
        break;
      case 3:
        magnitudeColor = R.color.magnitude3;
        break;
      case 4:
        magnitudeColor = R.color.magnitude4;
        break;
      case 5:
        magnitudeColor = R.color.magnitude5;
        break;
      case 6:
        magnitudeColor = R.color.magnitude6;
        break;
      case 7:
        magnitudeColor = R.color.magnitude7;
        break;
      case 8:
        magnitudeColor = R.color.magnitude8;
        break;
      case 9:
        magnitudeColor = R.color.magnitude9;
        break;
      default:
        magnitudeColor = R.color.magnitude10plus;
    }
    return ContextCompat.getColor(mContext, magnitudeColor);
  }
}
