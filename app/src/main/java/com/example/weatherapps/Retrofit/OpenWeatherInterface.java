package com.example.weatherapps.Retrofit;

import com.example.weatherapps.Model.WeatherForecastResult;
import com.example.weatherapps.Model.WeatherResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherInterface {
    @GET("weather")
    Observable<WeatherResult> getWeatherByLatLng(@Query("lat") Double lat,
                                                 @Query("lon") Double lng,
                                                 @Query("appid") String appid,
                                                 @Query("units") String units);

    @GET("weather")
    Observable<WeatherResult> getWeatherByCityName(@Query("q") String cityName,
                                                   @Query("appid") String appid,
                                                   @Query("units") String units);



    @GET("forecast")
    Observable<WeatherForecastResult> getForecastWeatherByLatLng(@Query("lat") String lat,
                                                                 @Query("lon") String lng,
                                                                 @Query("appid") String appid,
                                                                 @Query("units") String units);
}
