package com.weather.kingtous.weatherreport.LocationProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.widget.Toast;

import com.weather.kingtous.weatherreport.MainWindow;

import java.io.IOException;
import java.util.List;

import androidx.annotation.NonNull;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class LocationFinder extends Activity {

    LocationManager manager;
    Location location;
    String outputLocation="";
    int OK=0;
    int ERROR=-1;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        getPosition();
        finish();
    }


    @SuppressLint("MissingPermission")
    private void getPosition() {

        if (manager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER) || manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            //正常
            if (manager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER))
            {
                if (getLocationBasedOnGPS()==ERROR)
                {
                    if (manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
                    {
                        //再尝试
                        if (getLocationBasedOnNetwork()==ERROR)
                        {
                            //无位置提供商，或无法获取位置
                            Toast.makeText(this,"无法获取位置,请重试",Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    else {
                        Toast.makeText(this,"无法获取位置,请重试",Toast.LENGTH_SHORT).show();
                        return;
                    }

                }
            }
            else
            {
                if (getLocationBasedOnNetwork()==ERROR)
                {
                    //无位置提供商，或无法获取位置
                    Toast.makeText(this,"无法通过网络获取位置,请尝试重新定位或开启GPS定位",Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            Geocoder geocoder=new Geocoder(this);
            try {
                List<Address> address=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                if (address.size()>0)
                {
                    outputLocation=address.get(0).getLocality();
                    Toast.makeText(this,"定位成功，城市为"+outputLocation,Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(LocationFinder.this, MainWindow.class);
                    intent.putExtra("Location",outputLocation);
                    setResult(1,intent);
                }
                else
                {
                    Toast.makeText(this,"定位失败(未获取到位置),请重试",Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                Toast.makeText(this,"定位失败(IOException)",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            finally {
                finish();
            }
        }
        else {
            Toast.makeText(this, "请开启定位功能", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
            startActivityForResult(intent,0); //此为设置完成后返回到获取界面
        }


    }


    @SuppressLint("MissingPermission")
    private int getLocationBasedOnGPS()
    {
        if (manager!=null)
        {
            Toast.makeText(this,"正通过GPS定位",Toast.LENGTH_SHORT).show();
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            criteria.setCostAllowed(true);
            criteria.setAltitudeRequired(false);
            criteria.setBearingRequired(false);
            criteria.setPowerRequirement(Criteria.POWER_LOW);
            String BestProvider=manager.getBestProvider(criteria,true);
            if (BestProvider!=null)
            {
                manager.requestLocationUpdates(manager.getBestProvider(criteria, true), 6000, 1,new locationlistener());
                location=manager.getLastKnownLocation(manager.getBestProvider(criteria, true));
            }

            if (location==null) {
                //criteria定位失败
                manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 6000, 1, new locationlistener());
                location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location==null)
                {
                    return ERROR;
                }
            }
            return OK;
        }
        return ERROR;
    }

    @SuppressLint("MissingPermission")
    private int getLocationBasedOnNetwork()
    {
        if (manager!=null)
        {
            Toast.makeText(this,"正通过网络定位",Toast.LENGTH_SHORT).show();
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 6000, 1,new locationlistener());
            location=manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(location==null)
            {
                return ERROR;
            }
            return OK;
        }
        return ERROR;
    }

    class locationlistener implements LocationListener
    {

        @Override
        public void onLocationChanged(Location location) {
            //Toast.makeText(LocationFinder.this,"地点发生变化",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Toast.makeText(LocationFinder.this,"状态发生变化",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(LocationFinder.this,"位置提供商正常",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(LocationFinder.this,"位置提供商异常",Toast.LENGTH_SHORT).show();
        }
    }




}
