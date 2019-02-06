package com.weather.kingtous.weatherreport.WeatherFragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.weather.kingtous.weatherreport.R;
import com.weather.kingtous.weatherreport.WeatherRequest.WeatherDetail;

public class WeatherFragment extends Fragment {

    WeatherDetail detail;

    TextView Date;
    TextView WeatherType;
    TextView Temperature;
    TextView WindStrength;
    TextView WindDirection;
    ImageView WeatherImage;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setContent();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.weatherfragment,container,false);
        Date=view.findViewById(R.id.Date);
        WeatherType=view.findViewById(R.id.WeatherType);
        Temperature=view.findViewById(R.id.Temperature);
        WindStrength=view.findViewById(R.id.WindStrength);
        WindDirection=view.findViewById(R.id.WindDirection);
        WeatherImage=view.findViewById(R.id.WeatherImage);
        return view;
    }

    public void setDetail(WeatherDetail detail)
    {
        this.detail=detail;
    }


    public void setContent()
    {
        Date.setText(detail.getDate());
        WeatherType.setText(detail.getWeatherType());
        Temperature.setText(detail.getTemper());
        WindStrength.setText(detail.getWindS());
        WindDirection.setText(detail.getWindD());
    }

}
