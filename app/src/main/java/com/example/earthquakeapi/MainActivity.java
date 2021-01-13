package com.example.earthquakeapi;


import androidx.appcompat.app.AppCompatActivity;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


import java.util.ArrayList;

import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<Earthquake>>
{

    private boolean isConnected;
    private TextView emptyStateTextView;
    private ListAdapter adapter;
    private static final String USGS_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Checking the Network Connection
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (isConnected == false)
        {
            ProgressBar spinner = findViewById(R.id.progress_bar);
            spinner.setVisibility(View.INVISIBLE);
            emptyStateTextView = findViewById(R.id.empty_view);
            emptyStateTextView.setText("NO INTERNET CONNECTION");
            return;
        }

        ArrayList<Earthquake> earthquakes = new ArrayList<>();

        ListView view = findViewById(R.id.listview);
        emptyStateTextView = findViewById(R.id.empty_view);
        view.setEmptyView(emptyStateTextView);

        view.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                //Retriving the url from the Json object
                String url = earthquakes.get(position).getUrl();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });

        adapter = new ListAdapter(getApplicationContext(), 0, earthquakes);

        view.setAdapter(adapter);

        //Intializing the loaderManger
        LoaderManager loaderManager = getLoaderManager();

        Log.i("Info", "This is init Loader");
        loaderManager.initLoader(1, null, this);
    }

    @Override
    public Loader<List<Earthquake>> onCreateLoader(int id, Bundle args)
    {
        Log.i("Info", "This is OnCreate Loader");

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String minMagnitude = sharedPreferences.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));

        String orderBy = sharedPreferences.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        Uri baseUri = Uri.parse(USGS_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", "1000");
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", orderBy);
        Log.i("URL", uriBuilder.toString());
        return new EarthQuakeAsyncTaskLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> data)
    {
        ProgressBar spinner = findViewById(R.id.progress_bar);
        spinner.setVisibility(View.INVISIBLE);

        Log.i("Info", "This is OnLoadFinished");
        adapter.clear();
        if (data != null && !data.isEmpty())
        {
            adapter.addAll(data);
            adapter.notifyDataSetChanged();
        } else
        {
            emptyStateTextView.setText("NO EARTHQUAKE FOUND");
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader)
    {
        Log.i("Info", "This is OnLoadReset");
        adapter.clear();
    }

    //Inflating the Menu when Creating the Activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
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