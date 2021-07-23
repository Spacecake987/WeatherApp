package com.junglist963.weather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.junglist963.weather.WeatherModels.OpenWeatherMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TextView txtTemp, txtWeatherCondition, txtCity,  txtHumidity, txtMinTemp, txtMaxTemp,
            txtPressure, txtWind;
    ImageView img;
    FloatingActionButton fab;

    LocationManager locationManager;
    LocationListener locationListener;
    double lat, lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        getPermissions();



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
                startActivity(intent);

            }
        });
    }

    private void getPermissions() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,500,50,locationListener);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1 && permissions.length > 0 && ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,500,50,locationListener);
        }
    }

    private void initViews() {
        txtTemp = findViewById(R.id.txtTemp);
        txtWeatherCondition = findViewById(R.id.txtWeatherCondition);
        txtCity = findViewById(R.id.txtCity);
        txtHumidity = findViewById(R.id.txtHumidity);
        txtMinTemp = findViewById(R.id.txtMinTemp);
        txtMaxTemp = findViewById(R.id.txtMaxTemp);
        txtPressure = findViewById(R.id.txtPressure);
        txtWind = findViewById(R.id.txtWind);
        img = findViewById(R.id.img);
        fab = findViewById(R.id.floatingActionButton);

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {

                lat = location.getLatitude();
                lon = location.getLongitude();

                Log.e("lat", String.valueOf(lat));
                Log.e("lan", String.valueOf(lon));

                getWeatherData(lat, lon);
            }
        };


    }

    public void getWeatherData(double lat, double lon){
        WeatherAPI weatherAPI = RetrofitWeather.getClient().create(WeatherAPI.class);
        Call<OpenWeatherMap> call = weatherAPI.getWeatherWithLocation(lat,lon);
        call.enqueue(new Callback<OpenWeatherMap>() {
            @Override
            public void onResponse(Call<OpenWeatherMap> call, Response<OpenWeatherMap> response) {

                txtCity.setText(response.body().getName()+" , " + response.body().getSys().getCountry());
                txtTemp.setText(response.body().getMain().getTemp()+" °C");
                txtWeatherCondition.setText(response.body().getWeather().get(0).getDescription());
                txtHumidity.setText(" : "+response.body().getMain().getHumidity()+"%");
                txtMaxTemp.setText(" : "+response.body().getMain().getTempMax()+" °C");
                txtMinTemp.setText(" : "+response.body().getMain().getTempMin()+" °C");
                txtPressure.setText(" : "+response.body().getMain().getPressure());
                txtWind.setText(" : "+response.body().getWind().getSpeed());

                String iconCode = response.body().getWeather().get(0).getIcon();
                Glide.with(MainActivity.this).asBitmap().load("https://openweathermap.org/img/wn/10d@2x.png").into(img);

            }

            @Override
            public void onFailure(Call<OpenWeatherMap> call, Throwable t) {

            }
        });
    }
}