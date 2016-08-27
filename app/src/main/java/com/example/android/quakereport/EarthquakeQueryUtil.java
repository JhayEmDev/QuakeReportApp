package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.android.quakereport.Model.Earthquake;

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

public class EarthquakeQueryUtil {

  private static final String USGS_BASE_URL = "http://earthquake.usgs.gov/fdsnws/event/1/query";

  // Fields
  private String mFormat;
  private String mLimit;
  private String mMinMagnitude;
  private String mOrderBy;



  public EarthquakeQueryUtil() {
    mFormat = "geojson";
    mOrderBy = "time";
  }

  public void setFormat(String format) {
    mFormat = format;
  }

  public void setLimit(String limit) {
    mLimit = limit;
  }

  public void setMinMagnitude(String minMagnitude) {
    mMinMagnitude = minMagnitude;
  }

  public void setOrderBy(String order) {
    mOrderBy = order;
  }

  public URL buildURL() {
    Uri baseUri = Uri.parse(EarthquakeQueryUtil.USGS_BASE_URL);
    Uri.Builder uriBuilder= baseUri.buildUpon();

    uriBuilder.appendQueryParameter("format", mFormat);
    uriBuilder.appendQueryParameter("limit", mLimit);
    uriBuilder.appendQueryParameter("minmag", mMinMagnitude);
    uriBuilder.appendQueryParameter("orderby", mOrderBy);

    try {
      return new URL(uriBuilder.toString());
    } catch (MalformedURLException e) {
      e.printStackTrace();
      return null;
    }
  }

  // Helper Methods
  public static String makeHttpRequest(URL url) {
    String jsonResponse; // The main response from the server as JSON

    // Create HttpURLConnection from URL created
    // Get inputStream from HttpURLConnection
    // Wrap inputStream with inputStreamReader
    HttpURLConnection urlConnection = null;
    InputStream inputStream = null;
    StringBuilder streamAsString = new StringBuilder();
    try {
      urlConnection = (HttpURLConnection) url.openConnection();
      urlConnection.setRequestMethod("GET");
      urlConnection.setReadTimeout(10000); // To test
      urlConnection.setConnectTimeout(15000); // To test also
      urlConnection.connect();
      inputStream = urlConnection.getInputStream();
      InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
      BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
      String line = bufferedReader.readLine();
      while (line != null) {
        streamAsString.append(line);
        line = bufferedReader.readLine();

      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (urlConnection != null) urlConnection.disconnect();
      if (inputStream != null) {
        try {
          inputStream.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    jsonResponse = streamAsString.toString();
    return jsonResponse;
  }

  /**
   * Static helper method used to extract an ArrayList<Earthquake> from a string JSON response
   *
   * @param earthquakeDataJSON String; to be parsed
   * @return ArrayList<Earthquake>
   */
  public static ArrayList<Earthquake> extractEarthquakes(String earthquakeDataJSON) throws JSONException {
    ArrayList<Earthquake> tempEarthquakeArrayList = new ArrayList<>();
    JSONObject primaryJSONResponse = new JSONObject(earthquakeDataJSON);
    JSONArray arrayOfEarthquakes = primaryJSONResponse.optJSONArray("features");
    if (arrayOfEarthquakes != null) {
      int numberOfEarthquakes = arrayOfEarthquakes.length();
      for (int z = 0; z < numberOfEarthquakes; z++) {
        JSONObject anEarthquake = arrayOfEarthquakes.optJSONObject(z);
        if (anEarthquake != null) {
          JSONObject earthquakeProperties = anEarthquake.optJSONObject("properties");
          tempEarthquakeArrayList.add(new Earthquake(
              earthquakeProperties.getDouble("mag"),
              earthquakeProperties.getString("place"),
              earthquakeProperties.getLong("time"),
              earthquakeProperties.getString("url")
          ));
        } else {
          Log.i("extractEarthquakes()", "anEarthquake == null");
        }
      }
    } else {
      Log.i("extractEarthquakes()", "arrayOfEarthquakes is null");
    }

    return tempEarthquakeArrayList;
  }

  public EarthquakeAsyncTaskLoader createNewLoader(Context context) {
    return new EarthquakeAsyncTaskLoader(context, buildURL());
  }

  public static class EarthquakeAsyncTaskLoader extends AsyncTaskLoader<String> {

    private URL mBuiltURL;

    public EarthquakeAsyncTaskLoader(Context context, URL url) {
      super(context);
      mBuiltURL = url;
    }



    @Override
    protected void onStartLoading() {
      Log.i("EarthquakeQueryUtil", "onStartLoading()");
      super.onStartLoading();
      forceLoad();
    }

    @Override
    public String loadInBackground() {
      Log.i("EarthQueryUtil", "loadInBackground()");
      return EarthquakeQueryUtil.makeHttpRequest(mBuiltURL);
    }
  }

}