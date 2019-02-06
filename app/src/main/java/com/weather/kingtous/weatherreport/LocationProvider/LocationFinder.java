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

public class LocationFinder extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    LocationManager manager;
    Location location;
    String outputLocation="";

    int GPS_PERMISSION_CODE=1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        openGPSSettings();
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

    @SuppressLint("MissingPermission")
    private void openGPSSettings() {

        if (manager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            //正常
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            criteria.setCostAllowed(true);
            criteria.setAltitudeRequired(false);
            criteria.setBearingRequired(false);
            criteria.setPowerRequirement(Criteria.POWER_LOW);
            if (!EasyPermissions.hasPermissions(this,Manifest.permission.ACCESS_FINE_LOCATION)) {
                String[] permissions={Manifest.permission.ACCESS_FINE_LOCATION};
                EasyPermissions.requestPermissions(this,"用于城市定位",GPS_PERMISSION_CODE,permissions);
            }
            String BestProvider=manager.getBestProvider(criteria,true);
            if (BestProvider!=null)
            {
                manager.requestLocationUpdates(manager.getBestProvider(criteria, true), 6000, 1,new locationlistener());
                location=manager.getLastKnownLocation(manager.getBestProvider(criteria, true));
            }

            if (location==null)
            {
                //criteria定位失败
                manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 6000, 1,new locationlistener());
                location=manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location==null)
                {
                    manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 6000, 1,new locationlistener());
                    location=manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if(location==null)
                    {
                        Toast.makeText(this,"获取位置失败，请检查GPS信号",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(LocationFinder.this, MainWindow.class);
                        setResult(99,intent);
                        return;
                    }
                }
            }

            Geocoder geocoder=new Geocoder(this);
            try {
                List<Address> address=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                if (address.size()>0)
                {
                    outputLocation=address.get(0).getLocality();

                    Intent intent=new Intent(LocationFinder.this, MainWindow.class);
                    intent.putExtra("Location",outputLocation);
                    setResult(1,intent);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            return;
        }

        Toast.makeText(this, "请开启GPS！", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
        startActivityForResult(intent,0); //此为设置完成后返回到获取界面

    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Toast.makeText(this, "GPS权限授权成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Toast.makeText(this, "GPS权限授权失败，请打开权限", Toast.LENGTH_SHORT).show();
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }


    class locationlistener implements LocationListener
    {

        @Override
        public void onLocationChanged(Location location) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }




}
