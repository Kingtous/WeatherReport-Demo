package com.weather.kingtous.weatherreport;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

class Query {

    private String url="http://wthrcdn.etouch.cn/weather_mini?city=";

    private String sendRequest(String CityName)
    {
        return PureNetUtil.get(url+CityName);
    }

    //解析
    private ArrayList<WeatherDetail> parseForcast(JSONArray forcastArray)
    {
        ArrayList<WeatherDetail> tmp= new ArrayList<>();
        tmp.clear();
        for(int i=0;i<forcastArray.length();++i)
        {
            try {
                JSONObject object=forcastArray.getJSONObject(i);
                tmp.add(new WeatherDetail(object));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return tmp;

    }


    //Query入口
    public WeatherTotal Query(String CityName)
    {
        String Data=sendRequest(CityName);
        WeatherTotal total=new WeatherTotal();
        try {
            JSONObject obj=new JSONObject(Data);
            String StatusSign=(String) obj.get("desc");
            if (StatusSign.equals("OK"))
            {
                JSONObject data=obj.getJSONObject("data");
                total.setCity((String)data.get("city"));
                total.setHint((String)data.get("ganmao"));
                total.setTemperatureNow(Float.parseFloat((String)data.get("wendu")));
                total.setForcastDay(parseForcast(data.getJSONArray("forecast")));
            }
            else
            {
                throw new JSONException("Network failure.");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return total;
    }

    //刷新城市列表
    public void update_city()
    {

    }


}

