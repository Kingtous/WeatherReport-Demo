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

class WeatherDetail implements Serializable {
    String date;
    float HTemper;
    float ITemper;
    String WindS;//strength
    String WindD;//direction
    String WeatherType;

    WeatherDetail(JSONObject weatherobj)
    {
        try {
            date=weatherobj.getString("date");
            String tmp=weatherobj.getString("high");

            HTemper=Float.parseFloat(tmp.substring(0,tmp.length()-1).split("\\s+")[1]);

            tmp=weatherobj.getString("low");
            ITemper=Float.parseFloat(tmp.substring(0,tmp.length()-1).split("\\s+")[1]);

            WindD=weatherobj.getString("fengxiang");

            WindS=weatherobj.getString("fengli").split("\\[|\\]")[2];

            WeatherType=weatherobj.getString("type");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}

class WeatherTotal{

    private ArrayList<WeatherDetail> forcastList;
    private String City;
    private String hint;
    private float TemperatureNow;

    WeatherTotal()
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

