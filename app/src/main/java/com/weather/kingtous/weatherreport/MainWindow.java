package com.weather.kingtous.weatherreport;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.weather.kingtous.weatherreport.LocationProvider.LocationFinder;
import com.weather.kingtous.weatherreport.LocationProvider.LocationList;
import com.weather.kingtous.weatherreport.WeatherRequest.Query;
import com.weather.kingtous.weatherreport.WeatherRequest.WeatherShower;
import com.weather.kingtous.weatherreport.WeatherStructure.WeatherTotal;

import java.util.List;

public class MainWindow extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{

    //ui
    Query query;
    MenuItem about;
    Button QueryButton;
    Button LocateButton;
    Button CityListButton;
    EditText cityText;
    Toolbar toolbar;
    //RequestCode
    int CITY_SELECT_CODE =2;
    int LOCATE_CODE=1;
    int GPS_PERMISSION_CODE=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_window);

        //Intialize
        query=new Query();
        about = findViewById(R.id.about);
        QueryButton=findViewById(R.id.QueryButton);
        LocateButton=findViewById(R.id.Locate);
        CityListButton=findViewById(R.id.CityList);
        cityText=findViewById(R.id.CityText);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                LocateTask task=new LocateTask();
                task.execute();
            }
        });

        CityListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainWindow.this, LocationList.class);
                startActivityForResult(intent,CITY_SELECT_CODE);
            }
        });

        getPermission();
    }


    private void getPermission()
    {
        //检查权限
        if (!EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            String[] permissions={Manifest.permission.ACCESS_FINE_LOCATION};
            EasyPermissions.requestPermissions(MainWindow.this,"为了不影响您的使用，请允许应用申请相应权限.",GPS_PERMISSION_CODE,permissions);
        }

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
        if (id == R.id.about) {
            //关于
            TextView view=new TextView(this);
            SpannableString s=new SpannableString("  天气预报Demo\n  作者:Kingtous\n  项目地址:\n  https://github.com/Kingtous/WeatherReport-Demo");
            Linkify.addLinks(s,Linkify.WEB_URLS);
            view.setText(s);
            view.setMovementMethod(LinkMovementMethod.getInstance());

            new AlertDialog.Builder(MainWindow.this).setTitle("关于")
                    .setView(view)
                    .setPositiveButton("好", null)
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

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Toast.makeText(this, "权限授权成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Toast.makeText(this, "权限授权失败", Toast.LENGTH_SHORT).show();
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==GPS_PERMISSION_CODE)
        {
            EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this);
        }
    }


    private class LocateTask extends AsyncTask<Void,Void,Void>
    {
        ProgressDialog dialog=new ProgressDialog(MainWindow.this);
        @Override
        protected void onPreExecute() {
            dialog.setMessage("正在定位");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Intent intent=new Intent(MainWindow.this,LocationFinder.class);
            startActivityForResult(intent,LOCATE_CODE);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            dialog.dismiss();
        }
    }

}

