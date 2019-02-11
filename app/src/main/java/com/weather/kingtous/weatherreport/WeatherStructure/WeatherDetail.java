package com.weather.kingtous.weatherreport.WeatherStructure;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class WeatherDetail implements Serializable {
    String date;
    int HTemper;
    int LTemper;
    String WindS;//strength
    String WindD;//direction
    String WeatherType;

    public WeatherDetail(JSONObject weatherobj)
    {
        try {
            date=weatherobj.getString("date");
            String tmp=weatherobj.getString("high");

            HTemper=Integer.parseInt(tmp.substring(0,tmp.length()-1).split("\\s+")[1]);

            tmp=weatherobj.getString("low");
            LTemper=Integer.parseInt(tmp.substring(0,tmp.length()-1).split("\\s+")[1]);

            WindD=weatherobj.getString("fengxiang");

            WindS=weatherobj.getString("fengli").split("\\[|\\]")[2];

            WeatherType=weatherobj.getString("type");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getDate() {
        return date;
    }

    public String getTemper() {
        return String.valueOf(LTemper)+"° ~ "+ String.valueOf(HTemper)+"°";
    }

    public String getWeatherType() {
        return WeatherType;
    }

    public String getWindD() {
        return WindD;
    }

    public String getWindS() {
        return WindS;
    }
}
