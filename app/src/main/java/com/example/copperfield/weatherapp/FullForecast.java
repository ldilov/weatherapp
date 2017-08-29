package com.example.copperfield.weatherapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.text.DateFormat;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Date;

import data.ImageHttpClient;
import data.JSONParser;
import model.Weather;

public class FullForecast extends AppCompatActivity {

    private Weather weather = null;
    private TextView cityName;
    private TextView temp;
    private ImageView iconView;
    private TextView description;
    private TextView minTemp;
    private TextView maxTemp;
    private TextView humidity;
    private TextView pressure;
    private TextView wind;
    private TextView sunset;
    private TextView sunrise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent forecast = getIntent();
        String jsonData = forecast.getStringExtra("data");
        String iconImg = forecast.getStringExtra("iconData");
        weather = JSONParser.getWeather(jsonData);


        setContentView(R.layout.activity_full_forecast);
        //views
        cityName = (TextView) findViewById(R.id.locationText);
        temp = (TextView) findViewById(R.id.tempText);
        iconView = (ImageView) findViewById(R.id.thumbnail);
        description = (TextView) findViewById(R.id.descriptionText);
        minTemp = (TextView) findViewById(R.id.mintemp);
        maxTemp = (TextView) findViewById(R.id.maxtemp);
        humidity = (TextView) findViewById(R.id.humidity);
        pressure = (TextView) findViewById(R.id.pressure);
        wind = (TextView) findViewById(R.id.wind);
        sunset = (TextView) findViewById(R.id.sunset);
        sunrise = (TextView) findViewById(R.id.sunrise);

        //Set view values
        cityName.setText(weather.place.getCity() + "," + weather.place.getCountry());
        temp.setText(weather.currentCondition.getTemperature() + " C");
        humidity.append(weather.currentCondition.getHumidity() + " %");
        description.setText(weather.currentCondition.getDescription().toUpperCase());
        minTemp.append(weather.currentCondition.getMinTemp() + " C");
        maxTemp.append(weather.currentCondition.getMaxTemp() + " C");
        pressure.append(weather.currentCondition.getPressure() + " hPa");
        wind.append(weather.wind.getSpeed() + " km/h");
        new DownloadImageTask().execute(iconImg);

        DateFormat dformat = DateFormat.getTimeInstance();
        String sunriseDate = dformat.format(new Date(weather.place.getSunrise()));
        String sunsetDate = dformat.format(new Date(weather.place.getSunset()));

        sunrise.append(sunriseDate);
        sunset.append(sunsetDate);

    }

    public class DownloadImageTask extends AsyncTask<String, Void, Bitmap>{
        private String flag = "";
        @Override
        protected void onPostExecute(Bitmap bitmap) {
                iconView.setImageBitmap(bitmap);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return downloadImage(params[0]);
        }

        private Bitmap downloadImage(String code){
            ImageHttpClient imgClient = new ImageHttpClient();
            Bitmap imgBitmap = imgClient.getImage(code);
            return imgBitmap;
        }
    }
}
