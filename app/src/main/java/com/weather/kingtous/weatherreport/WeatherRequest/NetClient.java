package com.weather.kingtous.weatherreport.WeatherRequest;

import android.app.AlertDialog;
import android.os.Looper;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class NetClient {

    private Connection cn;
    private Statement statement;
    private String MySQL_Address="jdbc:mysql://123.206.34.50:3306/Weather";
    private String user="cloud";
    private String passwd="jintao!123";
    private ArrayList<String> OutputList=new ArrayList<String>();

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
               } catch (ClassNotFoundException e) {
                   e.printStackTrace();
               } catch (SQLException e) {
                   e.printStackTrace();
               }

               return 0;
    }

    public ArrayList<String> list()
    {
        ConnectToCloudDataServer();

        try {
            if (cn == null)
            {
                throw new SQLException();
            }
            ResultSet set=statement.executeQuery("select CityName from CityCode limit 0,3000");
            if (set == null)
            {
                throw new SQLException();
            }
            while (set.next())
            {
                OutputList.add(set.getString("CityName"));
            }
            return OutputList;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        finally {
            CloseConnection();
        }

    }



    void CloseConnection()
    {
        try {
            if (cn!=null)
            {
                cn.close();
            }
            if (statement!=null)
            {
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
