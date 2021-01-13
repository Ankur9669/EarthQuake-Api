package com.example.earthquakeapi;

import android.util.Log;

import androidx.annotation.Nullable;

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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Util
{

    public List<Earthquake> fetchEarthQuakeData(String url1)
    {
        Log.i("Info", "This is loadInBackground From EarthQuake AsyncTask");
        //If Empty String is passed than return
        if (url1 == null)
        {
            return null;
        }

        //Create URL for making HttpRequest
        URL url = createUrl(url1);

        if (url == null)
        {
            return null;
        }

        String jsonRequest = "";

        try
        {
            jsonRequest = makeHttpRequest(url);
        } catch (IOException e)
        {
            Log.e("IOException", "" + e);
        }

        //The ArrayList of earthquakes From the jsonObject
        ArrayList<Earthquake> earthquakes = extractFeatureFromJson(jsonRequest);
        return earthquakes;
    }

    private URL createUrl(String url)
    {
        URL urlResponse = null;
        try
        {
            urlResponse = new URL(url);
        } catch (MalformedURLException e)
        {
            Log.e("Malformed Url", "" + e);
        }
        return urlResponse;
    }

    private String makeHttpRequest(URL url) throws IOException
    {
        String jsonResponse = "";

        if (url == null)
        {
            return jsonResponse;
        }

        HttpURLConnection urlconnection = null;
        InputStream inputStream = null;
        try
        {
            urlconnection = (HttpURLConnection) url.openConnection();
            urlconnection.setReadTimeout(10000);
            urlconnection.setConnectTimeout(15000);
            urlconnection.setRequestMethod("GET");
            urlconnection.connect();

            if (urlconnection.getResponseCode() != 200)
            {
                Log.e("URlConnection Error", "");
                return "";
            }
            inputStream = urlconnection.getInputStream();
            jsonResponse = readFromStream(inputStream);
        } catch (IOException e)
        {
            Log.e("IOException", "" + e);
        } finally
        {
            if (urlconnection != null)
            {
                urlconnection.disconnect();
            }
            if (inputStream != null)
            {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private String readFromStream(InputStream inputStream) throws IOException
    {
        StringBuilder output = new StringBuilder();
        if (inputStream != null)
        {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null)
            {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private ArrayList<Earthquake> extractFeatureFromJson(String jsonRequest)
    {

        ArrayList<Earthquake> earthquake = new ArrayList<>();
        try
        {
            //Finding the root of the json Object
            JSONObject root = new JSONObject(jsonRequest);

            //Finding the FeatureArray in the root object
            JSONArray featuresArray = root.optJSONArray("features");

            for (int i = 0; i < featuresArray.length(); i++)
            {
                JSONObject featuresArrayObject = featuresArray.optJSONObject(i);
                JSONObject propertiesObject = featuresArrayObject.optJSONObject("properties");

                //Getting the url of the EarthQuake and Storing it for later use
                String url = propertiesObject.optString("url");

                //Getting the magnitude from the properties Object
                double magnitude = propertiesObject.optDouble("mag");

                //Formatting the magnitude to only show up to 1 decimal place
                DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
                String magnitudeToDisplay = magnitudeFormat.format(magnitude);
                magnitude = Double.parseDouble(magnitudeToDisplay);

                //Getting the place from the properties Object
                String place = propertiesObject.optString("place");
                String temp[] = place.split("of");

                if (temp.length == 1)
                {
                    temp[0] = "Near " + temp[0];
                }

                //Getting the time in UNIX format from the properties Object
                long time = propertiesObject.optLong("time");

                //Formatting the Date in a human readable fashion
                Date date = new Date(time);

                //Formatting the Data in the specified format
                SimpleDateFormat dateformat = new SimpleDateFormat("DD MMM YYYY");

                //Formatting the Time in a human readable fashion
                String dateToDisplay = dateformat.format(date);

                //Formatting the Data in the specified format
                SimpleDateFormat timeformat = new SimpleDateFormat("h:mm a");

                //Adding to the Earthquake Object
                String timeToDisplay = timeformat.format(date);
                if (temp.length == 2)
                {
                    earthquake.add(new Earthquake(magnitude, dateToDisplay, timeToDisplay, temp[1], temp[0] + " of ", url));
                } else
                {
                    earthquake.add(new Earthquake(magnitude, dateToDisplay, timeToDisplay, temp[0], "", url));
                }
            }
        } catch (JSONException e)
        {
            Log.i("Msg", "Error in Mainactivity.java while passing json object");
        }
        return earthquake;
    }


}
