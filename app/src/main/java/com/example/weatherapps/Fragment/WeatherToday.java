package com.example.weatherapps.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherapps.Common.Common;
import com.example.weatherapps.MainActivity;
import com.example.weatherapps.Model.Main;
import com.example.weatherapps.Model.WeatherResult;
import com.example.weatherapps.R;
import com.example.weatherapps.Retrofit.OpenWeatherInterface;
import com.example.weatherapps.Retrofit.RetrofitClient;
import com.squareup.picasso.Picasso;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;


public class WeatherToday extends Fragment {

    ImageView imgWeather;
    TextView txtCityName, txtHumidity, txtSunrise, txtSunset, txtPressure, txtTemperature, txtDesc, txtDateTime, txtWind, txtGeoCoords;
    LinearLayout weatherPanel;
    ProgressBar loading;
    Double lat, lng, a, b;
    CompositeDisposable compositeDisposable;
    OpenWeatherInterface mService;

    static WeatherToday instance;

    public static WeatherToday getInstance(){
        return instance == null ? new WeatherToday() : null;
    }


    public WeatherToday() {
        compositeDisposable = new CompositeDisposable();
        Retrofit retrofit = RetrofitClient.getInstance();
        mService = retrofit.create(OpenWeatherInterface.class);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        View view = inflater.inflate(R.layout.fragment_weather_today, container, false);

        try {
            b = getArguments().getDouble("b");
            a = getArguments().getDouble("a");
        }catch (Exception e)
        {
            e.getMessage();
        }
        if(a == null && b == null){
            return view;
        }

        lat = a;
        lng = b;
        init(view);
        Log.w("logg", lat + " " + lng);

        compositeDisposable.add(mService.getWeatherByLatLng(
                lat, lng, Common.APP_ID,
                "metric")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherResult>() {
                    @Override
                    public void accept(WeatherResult weatherResult) throws Exception {

                        //Load image
                        Picasso.get().load(new StringBuilder("https://openweathermap.org/img/wn/")
                                .append(weatherResult.getWeather().get(0).getIcon())
                                .append("@2x.png")
                                .toString())
                                .into(imgWeather);

                        //Load Info
                        txtCityName.setText(weatherResult.getName());
                        txtDesc.setText(new StringBuilder(String.valueOf(weatherResult.getWeather().get(0).getDescription())));
                        txtTemperature.setText(new StringBuffer(String.valueOf(weatherResult.getMain().getTemp()))
                                .append("°C")
                                .toString());
                        txtDateTime.setText(Common.convertUnixToDate(weatherResult.getDt()));
                        txtPressure.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getPressure()))
                                .append(" hpa")
                                .toString());
                        txtHumidity.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getHumidity()))
                                .append(" %")
                                .toString());
                        txtSunrise.setText(Common.convertUnixToHour(weatherResult.getSys().getSunrise()));
                        txtSunset.setText(Common.convertUnixToHour(weatherResult.getSys().getSunset()));
                        txtGeoCoords.setText(new StringBuilder(weatherResult.getCoord().toString())
                                .toString());

                        //Display panel
                        weatherPanel.setVisibility(View.VISIBLE);
                        loading.setVisibility(View.GONE);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(getActivity(), "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
        );

        return view;
    }



    private void init(View view) {
        imgWeather = view.findViewById(R.id.imgWeather);
        txtCityName = view.findViewById(R.id.txtCityName);
        txtHumidity = view.findViewById(R.id.txtHumidity);
        txtSunrise = view.findViewById(R.id.txtSunrise);
        txtSunset = view.findViewById(R.id.txtSunset);
        txtPressure = view.findViewById(R.id.txtPressure);
        txtTemperature = view.findViewById(R.id.txtTemperature);
        txtDesc = view.findViewById(R.id.txtDesc);
        txtDateTime = view.findViewById(R.id.txtDateTime);
        txtWind = view.findViewById(R.id.txtWind);
        txtGeoCoords = view.findViewById(R.id.txtGeoCoords);

        weatherPanel = view.findViewById(R.id.weatherPanel);
        loading = view.findViewById(R.id.loading);

    }


    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    public void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }
}