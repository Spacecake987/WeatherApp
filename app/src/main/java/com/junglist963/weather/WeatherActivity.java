package com.junglist963.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.junglist963.weather.WeatherModels.OpenWeatherMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherActivity extends AppCompatActivity {

    TextView txtTempWeather, txtWeatherConditionWeather, txtCityWeather, txtHumidityWeather, txtMinTempWeather, txtMaxTempWeather, txtPressureWeather, txtWindWeather;
    ImageView imgWeather;
    EditText edtTxtCityName;
    Button btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        initViews();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String cityName = edtTxtCityName.getText().toString();
                getWeatherData(cityName);

                edtTxtCityName.setText("");

            }
        });
    }

    private void initViews() {
        txtTempWeather =findViewById(R.id.txtTempWeather);
        txtWeatherConditionWeather =findViewById(R.id.txtWeatherConditionWeather);
        txtCityWeather =findViewById(R.id.txtCityWeather);
        txtHumidityWeather =findViewById(R.id.txtHumidityWeather);
        txtMinTempWeather =findViewById(R.id.txtMinTempWeather);
        txtMaxTempWeather =findViewById(R.id.txtMaxTempWeather);
        txtPressureWeather =findViewById(R.id.txtPressureWeather);
        txtWindWeather =findViewById(R.id.txtWindWeather);
        imgWeather =findViewById(R.id.imgWeather);
        edtTxtCityName =findViewById(R.id.edtTxtCityName);
        btnSearch =findViewById(R.id.btnSearch);
    }

    public void getWeatherData(String city){
        WeatherAPI weatherAPI = RetrofitWeather.getClient().create(WeatherAPI.class);
        Call<OpenWeatherMap> call = weatherAPI.getWeatherWithCity(city);
        call.enqueue(new Callback<OpenWeatherMap>() {
            @Override
            public void onResponse(Call<OpenWeatherMap> call, Response<OpenWeatherMap> response) {

                if (response.isSuccessful()){ txtCityWeather.setText(response.body().getName()+" , " + response.body().getSys().getCountry());
                    txtTempWeather.setText(response.body().getMain().getTemp()+" °C");
                    txtWeatherConditionWeather.setText(response.body().getWeather().get(0).getDescription());
                    txtHumidityWeather.setText(" : "+response.body().getMain().getHumidity()+"%");
                    txtMaxTempWeather.setText(" : "+response.body().getMain().getTempMax()+" °C");
                    txtMinTempWeather.setText(" : "+response.body().getMain().getTempMin()+" °C");
                    txtPressureWeather.setText(" : "+response.body().getMain().getPressure());
                    txtWindWeather.setText(" : "+response.body().getWind().getSpeed());

                    String iconCode = response.body().getWeather().get(0).getIcon();
                    Glide.with(WeatherActivity.this).asBitmap().load("https://openweathermap.org/img/wn/10d@2x.png").into(imgWeather);
                }else {
                    Toast.makeText(WeatherActivity.this, "City not found, please try again.", Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<OpenWeatherMap> call, Throwable t) {

            }
        });
    }
}