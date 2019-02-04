package com.weather.kingtous.weatherreport;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;


public class MainWindow extends AppCompatActivity {

    Query query;
    MenuItem settings;
    Button QueryButton;
    EditText cityText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_window);

        //Intialize
        query=new Query();
        settings = findViewById(R.id.action_settings);
        QueryButton=findViewById(R.id.QueryButton);
        cityText=findViewById(R.id.CityText);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
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
                            Intent intent=new Intent(MainWindow.this,WeatherShower.class);
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


        //刷新城市
        new Thread(new Runnable() {
            @Override
            public void run() {
                query.update_city();
            }
        }).start();
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

    class SettingListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            new AlertDialog.Builder(MainWindow.this).setTitle("你点了不是，which值为" + which)
                    .setPositiveButton("是的", null)
                    .show();
        }
    }


}

