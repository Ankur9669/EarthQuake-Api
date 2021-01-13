package com.example.earthquakeapi;


public class Earthquake
{
    private String url;

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    private double magnitude;
    private String city;
    private String date;
    private String time;
    private String location;

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        location = location;
    }

    public String getTime()
    {
        return time;
    }

    public void setTime(String time)
    {
        this.time = time;
    }

    public Earthquake(double magnitude, String date, String time, String city, String location, String url)
    {
        this.url = url;
        this.magnitude = magnitude;
        this.city = city;
        this.date = date;
        this.time = time;
        this.location = location;
    }

    public void setMagnitude(double magnitude)
    {
        this.magnitude = magnitude;
    }

    public double getMagnitude()
    {
        return magnitude;
    }


    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }


    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }


}
