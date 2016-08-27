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
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.quakereport.Adapters.EarthquakesAdapter;
import com.example.android.quakereport.Model.Earthquake;


import org.json.JSONException;

import java.util.ArrayList;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

  public static final String LOG_TAG = EarthquakeActivity.class.getName();

  // Fields
  private String randomString = "Test";

  private String haha = "The day I understand this is the day I become a pro developer";

  // Misc
  EarthquakesAdapter mEarthquakesAdapter;
  EarthquakeQueryUtil mEarthquakeQueryUtil;

  // Views
  ListView earthquakeListView;
  ProgressBar progressBar;
  TextView emptyTextView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.earthquake_activity);

    // Initialize Views
    earthquakeListView = (ListView) findViewById(R.id.list);
    progressBar = (ProgressBar) findViewById(R.id.progressBar);
    emptyTextView = (TextView) findViewById(R.id.emptyTextView);

    // Initialize Fields


    // Initialize Misc
    mEarthquakesAdapter = new EarthquakesAdapter(this, new ArrayList<Earthquake>());
    earthquakeListView.setEmptyView(emptyTextView);

    earthquakeListView.setAdapter(mEarthquakesAdapter);
    earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        //((Earthquake) parent.getItemAtPosition(position)).setLocation("JM Was here");
        //((EarthquakesAdapter) parent.getAdapter()).notifyDataSetChanged();

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(((Earthquake) parent.getItemAtPosition(position)).getURL()));
        startActivity(intent);
      }
    });
    mEarthquakeQueryUtil = new EarthquakeQueryUtil();
    if (checkNetworkState()) {
      getLoaderManager().initLoader(1, null, this);
    } else {
      emptyTextView.setText(getResources().getString(R.string.no_internet_connection));
      progressBar.setVisibility(View.GONE);
    }


  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.action_settings) {
      Intent settingsIntent = new Intent(this, SettingsActivity.class);
      startActivity(settingsIntent);
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  /* Start LoaderManager.LoaderCallbacks<String> Interface methods*/
  @Override
  public Loader<String> onCreateLoader(int id, Bundle args) {

    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

    String minMagnitude = sharedPreferences.getString(getString(R.string.settings_min_magnitude_key), getString(R.string.settings_min_magnitude_default));
    String orderBy = sharedPreferences.getString(getString(R.string.settings_order_by_key), getString(R.string.settings_order_by_default));

    mEarthquakeQueryUtil.setLimit("50");
    mEarthquakeQueryUtil.setMinMagnitude(minMagnitude);
    mEarthquakeQueryUtil.setOrderBy(orderBy);

    return mEarthquakeQueryUtil.createNewLoader(this);
  }

  @Override
  public void onLoadFinished(Loader<String> loader, String jsonResponse) {
    try {
      emptyTextView.setText(getResources().getString(R.string.empty_list));
      Log.i("EarthquakeActivity:", "onLoadFinished()");
      progressBar.setVisibility(View.GONE);
      mEarthquakesAdapter.setEarthquakes(EarthquakeQueryUtil.extractEarthquakes(jsonResponse));
      mEarthquakesAdapter.notifyDataSetChanged();

    } catch (JSONException e) {
      Log.i("onLoadFinished:", "Error during jsonParsing");
      e.printStackTrace();
    }
  }

  @Override
  public void onLoaderReset(Loader<String> loader) {
    Log.i("EarthquakeActivity", "onLoaderReset()");
  }
  /* End LoaderManager.LoaderCallbacks<String> Interface methods*/


  // Helper methods
  private boolean checkNetworkState() {
    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
    return networkInfo != null && networkInfo.isConnectedOrConnecting();
  }
}





















