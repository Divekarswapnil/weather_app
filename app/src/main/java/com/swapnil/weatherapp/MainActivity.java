package com.swapnil.weatherapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    EditText etCity, etCountry;
    TextView tvResult;
    TextView tvTempreture;
    TextView tvWindSpeed;
    TextView tvCloudiness;
    TextView tvPressure;
    private final String url = "https://api.openweathermap.org/data/2.5/weather";
    private final String appid = "e53301e27efa0b66d05045d91b2742d3";
    DecimalFormat df = new DecimalFormat("#.##");

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etCity = findViewById(R.id.etCity);
        etCountry = findViewById(R.id.etCountry);
        tvResult = findViewById(R.id.tvResult);
        tvTempreture = findViewById(R.id.tvTempreture);
        tvWindSpeed = findViewById(R.id.tvWindSpeed);
        tvCloudiness = findViewById(R.id.tvCloudiness);
        tvPressure = findViewById(R.id.tvPressure);
    }

    public void getWeatherDetails(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }

        String tempUrl = "";
        String city = etCity.getText().toString().trim();
        String country = etCountry.getText().toString().trim();

        if (city.isEmpty()) {
            tvResult.setText("City field cannot be empty!");
            tvTempreture.setText("0");
            tvWindSpeed.setText("0");
            tvCloudiness.setText("0");
            tvPressure.setText("0");
        } else {
            if (!country.isEmpty()) {
                tempUrl = url + "?q=" + city + "," + country + "&appid=" + appid;
            } else {
                tempUrl = url + "?q=" + city + "&appid=" + appid;
            }

            Log.d("WeatherRequest", "Requesting URL: " + tempUrl);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, tempUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("WeatherResponse", "Response: " + response);
                            String output = "";

                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                                JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                                String description = jsonObjectWeather.getString("description");
                                JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                                double temp = jsonObjectMain.getDouble("temp") - 273.15;
                                double feelsLike = jsonObjectMain.getDouble("feels_like") - 273.15;
                                int pressure = jsonObjectMain.getInt("pressure");
                                int humidity = jsonObjectMain.getInt("humidity");
                                JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                                String wind = jsonObjectWind.getString("speed");
                                JSONObject jsonObjectClouds = jsonResponse.getJSONObject("clouds");
                                String clouds = jsonObjectClouds.getString("all");
                                JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
                                String countryName = jsonObjectSys.getString("country");
                                String cityName = jsonResponse.getString("name");

                                tvResult.setTextColor(Color.rgb(68, 134, 199));
                                output += "Current weather of " + cityName + " (" + countryName + ")"
                                        + "\n Temp: " + df.format(temp) + " °C"
                                        + "\n Feels Like: " + df.format(feelsLike) + " °C"
                                        + "\n Humidity: " + humidity + "%"
                                        + "\n Description: " + description
                                        + "\n Wind Speed: " + wind + " m/s (meters per second)"
                                        + "\n Cloudiness: " + clouds + "%"
                                        + "\n Pressure: " + pressure + " hPa";
                                tvResult.setText(output);
                                tvTempreture.setText(df.format(temp) + " °C");
                                tvWindSpeed.setText(wind + " m/s");
                                tvCloudiness.setText(clouds + "%");
                                tvPressure.setText(pressure + " hPa");
                            } catch (JSONException e) {
                                e.printStackTrace();
                                tvResult.setText("Error parsing weather data.");
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("WeatherError", "Error: " + error.toString());
                    Toast.makeText(getApplicationContext(), "Error fetching weather data: " + error.toString().trim(), Toast.LENGTH_SHORT).show();
                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }

}