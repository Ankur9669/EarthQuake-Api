package com.example.earthquakeapi;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class EarthQuakeAsyncTaskLoader extends AsyncTaskLoader<List<Earthquake>>
{

    private String url1 = "a";

    public EarthQuakeAsyncTaskLoader(@NonNull Context context, String url)
    {
        super(context);
        this.url1 = url;
    }

    @Nullable
    @Override
    public List<Earthquake> loadInBackground()
    {
        Log.i("Info", "This is loadInBackground From EarthQuake AsyncTask");
        Util obj = new Util();
        List<Earthquake> earthquakes = obj.fetchEarthQuakeData(url1);
        return earthquakes;
    }

    @Override
    protected void onStartLoading()
    {
        Log.i("Info", "This is OnStartLoading From EarthQuake AsyncTask");
        //To forceLoad the loader This is a good practice
        forceLoad();
    }
}
