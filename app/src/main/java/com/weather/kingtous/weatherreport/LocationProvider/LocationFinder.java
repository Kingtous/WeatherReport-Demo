package com.weather.kingtous.weatherreport.LocationProvider;

import android.Manifest;
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
import androidx.core.app.ActivityCompat;

import android.widget.Toast;

import com.weather.kingtous.weatherreport.MainWindow;

import java.io.IOException;
import java.util.List;

import androidx.annotation.NonNull;
import pub.devrel.easypermissions.EasyPermissions;

public class LocationFinder extends Activity {

    LocationManager manager;
    Location location;
    String outputLocation="";


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
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this);
    }

    private void openGPSSettings() {

        if (manager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            //正常
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            criteria.setCostAllowed(true);
            criteria.setAltitudeRequired(false);
            criteria.setBearingRequired(false);
            criteria.setPowerRequirement(Criteria.POWER_LOW);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                Toast.makeText(this, "请开启定位权限", Toast.LENGTH_SHORT).show();

                String[] permissions={Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};
                EasyPermissions.requestPermissions(this,"用于城市定位",100,permissions);
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
            }


            manager.requestLocationUpdates(manager.getBestProvider(criteria, true), 2000, 1, new LocationListener() {
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
            });
            location=manager.getLastKnownLocation(manager.getBestProvider(criteria, true));

            if (location==null)
            {
                Toast.makeText(this,"获取位置失败，请检查GPS信号",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(LocationFinder.this, MainWindow.class);
                setResult(99,intent);
                return;
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


}
