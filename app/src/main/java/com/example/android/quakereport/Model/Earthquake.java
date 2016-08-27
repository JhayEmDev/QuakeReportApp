package com.example.android.quakereport.Model;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Earthquake {

  private double mMagnitude;
  private String mPlace;
  private String mOffset;
  private String mLocation;
  private long mTime;
  private Date mDate;
  private String mURL;

  public Earthquake(double mag, String place, long time, String url) {
    mMagnitude = mag;
    mPlace = place;
    mTime = time;
    mDate = new Date(mTime);
    extractOffsetAndLocation();
    mURL = url;
  }

  public double getMagnitude() {
    return mMagnitude;
  }

  public String getPlace() {
    return mPlace;
  }

  public String getOffset() {
    return mOffset;
  }

  public String getLocation() {
    return mLocation;
  }

  public void setLocation(String location) {
    mLocation = location;
  }

  public String getDateDay() {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
    return simpleDateFormat.format(mDate);
  }

  public String getDateTime() {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
    return simpleDateFormat.format(mDate);
  }

  public String getURL() {
    return mURL;
  }

  // Helper methods
  private void extractOffsetAndLocation() {
    if (Character.isDigit(mPlace.charAt(0))) {
      // Assuming mPlace starts with the KM offset
      for (int z = 0; z < mPlace.length(); z++) {
        char letter = mPlace.charAt(z);
        if (letter == 'o' && mPlace.charAt(z + 1) == 'f') {
          mOffset = mPlace.substring(0, z + 2);
          mLocation = mPlace.substring(z + 3);
          break;
        }
      }
    } else {
      mOffset = "Near the";
      mLocation = mPlace;
    }
  }
}

















