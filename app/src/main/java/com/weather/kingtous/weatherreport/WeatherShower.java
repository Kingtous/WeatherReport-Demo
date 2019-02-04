package com.weather.kingtous.weatherreport;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TableLayout;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class WeatherShower extends Activity {

    Intent intent;
    WeatherTotal total;

    private List<String> titles;
    private List<Fragment> fragments;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weatherresult);
        intent=getIntent();
        total=new Gson().fromJson(intent.getStringExtra("Weather"),WeatherTotal.class);
        initView();

    }


    private void initView()
    {
        titles=new ArrayList<>();
        titles.add("明天");
        titles.add("后天");
        for (int i=3;i<=total.getForcastDay().size();++i)
        {
            titles.add("第"+Integer.toString(i)+"天");
        }

        fragments=new ArrayList<>();
        // 5 forcast

    }

}
