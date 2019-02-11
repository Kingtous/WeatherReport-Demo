package com.weather.kingtous.weatherreport.WeatherStructure;

import java.util.ArrayList;

public class WeatherTotal{

    private ArrayList<WeatherDetail> forcastList;
    private String City;
    private String hint;
    private float TemperatureNow;

    public WeatherTotal()
    {
        forcastList=new ArrayList<WeatherDetail>();
        forcastList.clear();
    }

    public void setForcastDay(ArrayList<WeatherDetail> ForcastList)
    {
        forcastList=ForcastList;
    }
    public void addForcastDay(WeatherDetail detail)
    {
        forcastList.add(detail);
    }
    public ArrayList<WeatherDetail> getForcastDay()
    {
        return forcastList;
    }
    public void setCity(String city)
    {
        City=city;
    }
    public String getCity()
    {
        return City;
    }
    public void setHint(String Hint)
    {
        hint=Hint;
    }
    public String getHint()
    {
        return hint;
    }
    public void setTemperatureNow(Float temperatureNow)
    {
        TemperatureNow=temperatureNow;
    }
    public float getTemperatureNow()
    {
        return TemperatureNow;
    }
}
