package com.weather.kingtous.weatherreport.LocationProvider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import android.widget.Toast;

import com.weather.kingtous.weatherreport.MainWindow;
import com.weather.kingtous.weatherreport.R;
import com.weather.kingtous.weatherreport.WeatherRequest.NetClient;

import java.util.ArrayList;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

public class LocationList extends AppCompatActivity {

    RecyclerView CityList;
    NetClient client;
    ArrayList<String> Cities;
    RecyclerView.Adapter adapter;
    int CITY_SELECT_CODE =2;
    SearchView CitySearchView;
    ProgressDialog Pdialog;

    MyAsyncTask task;

    //接口
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_listing);
        CityList = findViewById(R.id.CityRecyclerListing);
        client = new NetClient();
        CitySearchView=findViewById(R.id.SearchToolBarView);
        Pdialog=new ProgressDialog(LocationList.this);
        Pdialog.setMessage("正在获取城市数据");
        task=new MyAsyncTask(Pdialog);

        task.execute();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        CityList.setLayoutManager(layoutManager);
        CityList.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        CitySearchView.setSubmitButtonEnabled(true);
        CitySearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ((mAdapter) adapter).setFilter(filter(Cities,query));
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ((mAdapter) adapter).setFilter(filter(Cities,newText));
                return true;
            }
        });
    }



    private void CallInternet()
    {
        Cities = client.list();
    }

    private void setView() {
        if (Cities == null) {
            Toast.makeText(this, "查询城市数据失败，请检查网络连接后重试", Toast.LENGTH_SHORT).show();
            Cities = new ArrayList<String>();
            Cities.add("空");
        }

        //可做排序，比较耗时，放到MySQL服务器上做
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
        CityList.setAdapter(adapter);
    }

    private ArrayList<String> filter(ArrayList<String> cities,String text)
    {
        if (text.equals(""))
        {
            return cities;
        }
        else
        {
            ArrayList<String> arrTmp=new ArrayList<>();
            for (String city : cities)
            {
                if (city.contains(text))
                {
                    //contains不包含相同
                    arrTmp.add(city);
                }

            }
            return arrTmp;
        }
    }



    class MyAsyncTask extends AsyncTask<Void,Void,Void>{

        ProgressDialog dialog;

        MyAsyncTask(ProgressDialog dialog)
        {
            this.dialog = dialog;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            publishProgress();
            CallInternet();
            return null;
        }

        @Override
        protected void onPreExecute() {
            dialog.show();
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            setView();
            dialog.dismiss();
            if (Cities!=null)
            {
                //防止异常
                if (Cities.size()!=1)
                {
                    //为"空"(当做一个textview插入进去了)
                    Toast.makeText(LocationList.this,"检索到"+Cities.size()+"个城市信息，上方搜索框可进行检索.",Toast.LENGTH_LONG).show();
                }
            }
        }
    }


}

