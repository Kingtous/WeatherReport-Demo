package com.weather.kingtous.weatherreport;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.weather.kingtous.weatherreport.LocationProvider.LocationFinder;
import com.weather.kingtous.weatherreport.LocationProvider.LocationList;
import com.weather.kingtous.weatherreport.WeatherRequest.NetClient;
import com.weather.kingtous.weatherreport.WeatherRequest.Query;
import com.weather.kingtous.weatherreport.WeatherRequest.WeatherShower;
import com.weather.kingtous.weatherreport.WeatherRequest.WeatherTotal;


public class MainWindow extends AppCompatActivity {

    //ui
    Query query;
    MenuItem settings;
    Button QueryButton;
    Button LocateButton;
    Button CityListButton;
    EditText cityText;
    Toolbar toolbar;
    FloatingActionButton fab;
    //RequestCode
    int CITY_SELECT_CODE =2;
    int LOCATE_CODE=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_window);

        //Intialize
        query=new Query();
        settings = findViewById(R.id.action_settings);
        QueryButton=findViewById(R.id.QueryButton);
        LocateButton=findViewById(R.id.Locate);
        CityListButton=findViewById(R.id.CityList);
        cityText=findViewById(R.id.CityText);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(MainWindow.this)
                        .setTitle("查询的城市为度.")
                        .setPositiveButton("好的", null)
                        .show();
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //       .setAction("Action", null).show();
            }
        });



        QueryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        WeatherTotal weather=query.Query(cityText.getText().toString());
                        if (weather.getCity()!=null)
                        {
                            //查询成功
                            Intent intent=new Intent(MainWindow.this, WeatherShower.class);
                            intent.putExtra("Weather",new Gson().toJson(weather));
                            startActivity(intent);
                        }
                        else
                        {
                            new AlertDialog.Builder(MainWindow.this).setTitle("输入名称有误.")
                                .setPositiveButton("好",null)
                                .show();
                        }
                        Looper.loop();
                    }
                }).start();
            }
        });

        LocateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        Intent intent=new Intent(MainWindow.this,LocationFinder.class);
                        startActivityForResult(intent,LOCATE_CODE);
                    }
        });

        CityListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainWindow.this, LocationList.class);
                startActivityForResult(intent,CITY_SELECT_CODE);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_window, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {


            new AlertDialog.Builder(MainWindow.this).setTitle("想点设置？")
                    .setPositiveButton("是的", null)
                    .setNegativeButton("不是", new SettingListener())
                    .show();


            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==LOCATE_CODE || requestCode==CITY_SELECT_CODE)//定位数据
        {
            String cityTMP= null;
            if (data != null) {
                cityTMP = data.getStringExtra("Location");
            }
            if (cityTMP!=null)
            {
                cityText.setText(cityTMP);
            }
        }
    }

    class SettingListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            new AlertDialog.Builder(MainWindow.this).setTitle("你点了不是，which值为" + which)
                    .setPositiveButton("是的", null)
                    .show();
        }
    }


}

