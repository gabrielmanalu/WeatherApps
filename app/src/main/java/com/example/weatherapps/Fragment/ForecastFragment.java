package com.example.weatherapps.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.weatherapps.Adapter.WeatherForecastAdapter;
import com.example.weatherapps.Common.Common;
import com.example.weatherapps.Model.WeatherForecastResult;
import com.example.weatherapps.R;
import com.example.weatherapps.Retrofit.OpenWeatherInterface;
import com.example.weatherapps.Retrofit.RetrofitClient;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;


public class ForecastFragment extends Fragment {

    CompositeDisposable compositeDisposable;
    OpenWeatherInterface mService;

    TextView txtCity;
    RecyclerView recyclerForecast;


    static ForecastFragment instance;

    public static ForecastFragment getInstance(){
        if(instance == null){
            instance = new ForecastFragment();
        }
        return instance;
    }


    public ForecastFragment() {
        Retrofit retrofit = RetrofitClient.getInstance();
        mService = retrofit.create(OpenWeatherInterface.class);
        compositeDisposable = new CompositeDisposable();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View itemView = inflater.inflate(R.layout.fragment_forecast, container, false);

        txtCity = itemView.findViewById(R.id.txtCityName);

        recyclerForecast = itemView.findViewById(R.id.recyclerForecast);
        recyclerForecast.setHasFixedSize(true);

        recyclerForecast.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        getForecastWeatherInformation();
        Log.w("POSITION", Common.current_location.getLatitude() + " " + Common.current_location.getLongitude());

        return itemView;
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



    private void getForecastWeatherInformation() {
        Log.w("TEST", "asdsfadsa");

        compositeDisposable.add(mService.getForecastWeatherByLatLng(
                String.valueOf(Common.current_location.getLatitude()),
                String.valueOf(Common.current_location.getLongitude()),
                Common.APP_ID,
                "metric")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumerCaller(), throwable -> Log.w("ERROR", "" + throwable.getMessage()))
        );
        Log.w("TEST", "asdakdsa");
    }

    private void displayForecastWeather(WeatherForecastResult weatherForecastResult) {
        Log.w("TEST", "dadsa");

        txtCity.setText(new StringBuilder(weatherForecastResult.city.name));
//        txtGeoCoord.setText(new StringBuilder(weatherForecastResult.city.coord.toString()));

        WeatherForecastAdapter adapter = new WeatherForecastAdapter(getContext(), weatherForecastResult);
        recyclerForecast.setAdapter(adapter);
    }

    private Consumer consumerCaller(){
        Consumer consumer = new Consumer<WeatherForecastResult>() {
            @Override
            public void accept(WeatherForecastResult weatherForecastResult) throws Exception {
                displayForecastWeather(weatherForecastResult);
                Log.w("TEST", "asdadsa");
            }
        };
        return consumer;

    }
}