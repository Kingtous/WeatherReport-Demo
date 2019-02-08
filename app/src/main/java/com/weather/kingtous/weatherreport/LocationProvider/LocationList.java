package com.weather.kingtous.weatherreport.LocationProvider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.weather.kingtous.weatherreport.MainWindow;
import com.weather.kingtous.weatherreport.R;
import com.weather.kingtous.weatherreport.WeatherRequest.NetClient;

import net.sourceforge.pinyin4j.PinyinHelper;

import java.util.ArrayList;
import java.util.Comparator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LocationList extends Activity {

    Thread net;
    RecyclerView CityList;
    NetClient client;
    ArrayList<String> Cities;
    RecyclerView.Adapter adapter;
    int CITY_SELECT_CODE =2;

    //接口
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_listing);
        CityList = findViewById(R.id.CityRecyclerListing);
        client = new NetClient();
        net = new Thread(new Runnable() {
            @Override
            public void run() {
                Cities = client.list();
            }
        });
        net.start();
        while (net.isAlive()) {
            try {
                Thread.sleep(1500);
                Toast.makeText(this, "接收城市数据", Toast.LENGTH_SHORT).show();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        setView();
    }

    private void setView() {
        if (Cities == null) {
            Toast.makeText(this, "查询城市数据失败，请检查网络连接后重试", Toast.LENGTH_SHORT).show();
            Cities = new ArrayList<String>();
            Cities.add("空");
        }
        //可做排序，比较耗时，放到MySQL服务器上做
        //Collections.sort(Cities,new PinyinComparator());
        adapter = new mAdapter(this,Cities);
        ((mAdapter) adapter).setOnItemClickListener(new mAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent(LocationList.this,MainWindow.class);
                intent.putExtra("Location",((mAdapter) adapter).list.get(position));
                setResult(CITY_SELECT_CODE,intent);
                finish();
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        CityList.setLayoutManager(layoutManager);



        CityList.setAdapter(adapter);
        CityList.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

    }




}

