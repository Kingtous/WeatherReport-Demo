package com.weather.kingtous.weatherreport;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.weather.kingtous.weatherreport.WeatherFragment.WeatherFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WeatherShower extends AppCompatActivity {

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

        //去掉阴影
        Objects.requireNonNull(getSupportActionBar()).setElevation(0);

        initView();
        initData();
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
        for (int times=0;times<5;++times)
        {
            WeatherFragment fragment=new WeatherFragment();
            fragment.setDetail(total.getForcastDay().get(times));
            fragments.add(fragment);
        }
    }


    private void initData()
    {
        tabLayout = findViewById(R.id.tb_home);
        viewPager = findViewById(R.id.vp_home);
        viewPager.setOffscreenPageLimit(fragments.size());

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return fragments.get(i);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return titles.get(position);
            }
        });

        tabLayout.setupWithViewPager(viewPager);
    }



}
