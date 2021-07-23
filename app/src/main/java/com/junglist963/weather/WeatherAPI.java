package com.junglist963.weather;

import com.junglist963.weather.WeatherModels.OpenWeatherMap;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherAPI {

    @GET("weather?appid=521ebf783ef974ffe2c96d3cb442dfcd&units=metric")
    Call<OpenWeatherMap>getWeatherWithLocation(@Query("lat") double lat,@Query("lon") double lon);

    @GET("weather?appid=521ebf783ef974ffe2c96d3cb442dfcd&units=metric")
    Call<OpenWeatherMap>getWeatherWithCity(@Query("q") String name);


}
