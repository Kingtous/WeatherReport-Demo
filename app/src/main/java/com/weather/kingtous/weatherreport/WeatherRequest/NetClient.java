package com.weather.kingtous.weatherreport.WeatherRequest;

import android.os.Looper;
import android.support.v7.app.AlertDialog;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

class NetClient {

    Connection cn;
    Statement statement;
    String MySQL_Address="jdbc:mysql://123.206.34.50:3306/Weather";
    String user="cloud";
    String passwd="jintao!123";
    ArrayList<String> OutputList=new ArrayList<String>();

    ArrayList<String> getOutput()
    {
        return OutputList;
    }

    int ConnectToCloudDataServer() {
               OutputList.clear();
               try {
                   Class.forName("com.mysql.jdbc.Driver");
                   cn = DriverManager.getConnection(MySQL_Address, user, passwd);

                   statement=cn.createStatement();
                   Looper.loop();
               } catch (ClassNotFoundException e) {
                   e.printStackTrace();
               } catch (SQLException e) {
                   e.printStackTrace();
               }

               return 0;
    }

    int Query(String QueryPlace)
    {
        try {
            ResultSet set=statement.executeQuery("select * from CityCode where CityName="+"\""+QueryPlace+"\"");
            while (set.next())
            {
                OutputList.add(set.toString());
            }
            return 0;


        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }

    }



    int CloseConnection()
    {
        try {
            cn.close();
            statement.close();
            return 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }

    }


}
