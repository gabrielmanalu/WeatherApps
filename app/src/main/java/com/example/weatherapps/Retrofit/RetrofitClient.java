package com.example.weatherapps.Retrofit;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit instance;
    public static Retrofit getInstance(){
        return instance == null ?
                new Retrofit.Builder()
                        .baseUrl("https://api.openweathermap.org/data/2.5/")
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build() : instance;
    }
}
