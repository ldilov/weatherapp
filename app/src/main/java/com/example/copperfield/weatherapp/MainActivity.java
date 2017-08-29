package com.example.copperfield.weatherapp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import data.CitySettings;
import data.ImageHttpClient;
import data.JSONParser;
import data.LocationHttpClient;
import data.WeatherHttpClient;
import model.GeoLocation;
import model.Weather;
import utility.Utils;

public class MainActivity extends AppCompatActivity {
    private TextView cityName;
    private TextView temp;
    private ImageView iconView;
    private TextView description;
    private TextView temp3h;
    private ImageView iconView3h;
    private TextView description3h;
    private TextView temp6h;
    private ImageView iconView6h;
    private TextView description6h;
    private TextView day;

    public static String jsonData = null;
    public static String icon = null;

    public static String cityNameLoc = "Unknown";
    public static String countryCode = "N\\A";

    Weather weather = new Weather();
    Weather weather3h = new Weather();
    Weather weather6h = new Weather();

    GeoLocation geoLocation = new GeoLocation();

    private CitySettings cityPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityPreference = new CitySettings(MainActivity.this);
        //Request permission
        if (!canAccessLocation()) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1337);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                System.err.println("Thread interrupted exception in requesting permission!");
                e.printStackTrace();
            }
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast toast = Toast.makeText(MainActivity.this, "Моля предоставете необходимите права за извличане на локация", Toast.LENGTH_LONG);
            cityNameLoc = "Sofia";
            countryCode = "BG";
            toast.show();

        } else {
            LocationManager locationManager = (LocationManager) getSystemService(MainActivity.this.LOCATION_SERVICE);
            LocationListener locationListener = new MyLocationListener();
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, locationListener);
        }

        cityName = (TextView) findViewById(R.id.locationText);
        iconView = (ImageView) findViewById(R.id.thumbnail);
        temp = (TextView) findViewById(R.id.tempText);
        description = (TextView) findViewById(R.id.descriptionText);
        iconView3h = (ImageView) findViewById(R.id.thumbnail3h);
        temp3h = (TextView) findViewById(R.id.tempText3h);
        description3h = (TextView) findViewById(R.id.descriptionText3h);
        iconView6h = (ImageView) findViewById(R.id.thumbnail6h);
        temp6h = (TextView) findViewById(R.id.tempText6h);
        description6h = (TextView) findViewById(R.id.descriptionText6h);
        day = (TextView) findViewById(R.id.day);

        // get Location
        Log.e("<LOCATION>", cityNameLoc);
        renderWeatherData(cityPreference.getCity());
    }

    public void renderWeatherData(String city){
        Log.v("---", "Opening task");
        WeatherTask weatherTask = new WeatherTask();
        WeatherHourlyTask weatherHourlyTask = new WeatherHourlyTask();
        Log.v("---", "Weather task started");
        Log.v("---", new String(city + "&appid=" + Utils.API_KEY + "&units=metric"));
        weatherTask.execute(new String[]{city + "&appid=" + Utils.API_KEY + "&units=metric&lang=bg"});
        weatherHourlyTask.execute(new String[]{city + "&appid=" + Utils.API_KEY + "&units=metric&lang=bg"});
    }

    public class DownloadImageTask extends AsyncTask<String, Void, Bitmap>{
        private String flag = "";
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if(flag.equalsIgnoreCase("3h")){
                iconView3h.setImageBitmap(bitmap);
            } else if(flag.equalsIgnoreCase("6h")){
                iconView6h.setImageBitmap(bitmap);
            } else {
                iconView.setImageBitmap(bitmap);
            }
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            if(params[1].equalsIgnoreCase("3h") || params[1].equalsIgnoreCase("6h")){
                flag = params[1];
            }
            return downloadImage(params[0]);
        }

        private Bitmap downloadImage(String code){
            icon = code;
            ImageHttpClient imgClient = new ImageHttpClient();
            Bitmap imgBitmap = imgClient.getImage(code);
            return imgBitmap;
        }
    }

    private class GeolocationTask extends  AsyncTask<String, Void, GeoLocation>{
        @Override
        protected void onPostExecute(GeoLocation geoLocation) {
            super.onPostExecute(geoLocation);
            cityNameLoc = geoLocation.getCity();
            countryCode = geoLocation.getCountryCode();
            cityPreference.setCity(cityNameLoc + "," + countryCode);
            renderWeatherData(cityPreference.getCity());
        }

        @Override
        protected GeoLocation doInBackground(String... params) {
            String data = new LocationHttpClient().getLocationData(params[0]);
            geoLocation = JSONParser.getLocationData(data);

            return geoLocation;
        }
    }


    private class WeatherHourlyTask extends AsyncTask<String, Void, Weather[]> {
        @Override
        protected void onPostExecute(Weather[] weatherArr) {
            super.onPostExecute(weatherArr);
            temp3h.setText(weatherArr[0].currentCondition.getTemperature() + " C");
            description3h.setText(weatherArr[0].currentCondition.getDescription().toUpperCase());
            temp6h.setText(weatherArr[1].currentCondition.getTemperature() + " C");
            description6h.setText(weatherArr[1].currentCondition.getDescription().toUpperCase());
            // Log.v("--- DOwnload Image--- ", weather.iconData);
            weatherArr[0].iconData = weatherArr[0].currentCondition.getIcon(); //icon code
            weatherArr[1].iconData = weatherArr[1].currentCondition.getIcon(); //icon code
            new DownloadImageTask().execute(weatherArr[0].iconData, "3h");
            new DownloadImageTask().execute(weatherArr[1].iconData, "6h");
        }

        @Override
        protected Weather[] doInBackground(String... params) {
            String data = new WeatherHttpClient().getWeatherData(params[0], true);

            weather3h = JSONParser.getWeatherByHour(data, 0);
            weather6h = JSONParser.getWeatherByHour(data, 1);
            return new Weather[]{weather3h, weather6h};
        }
    }

    private class WeatherTask extends AsyncTask<String, Void, Weather> {
        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
            Date d = new Date();
            String dayOfTheWeek = sdf.format(d);
            day.setText(dayOfTheWeek);
            cityName.setText(weather.place.getCity() + "," + weather.place.getCountry());
            temp.setText(weather.currentCondition.getTemperature() + " C");
            description.setText(weather.currentCondition.getDescription().toUpperCase());
           // Log.v("--- DOwnload Image--- ", weather.iconData);
            weather.iconData = weather.currentCondition.getIcon(); //icon code
            new DownloadImageTask().execute(weather.iconData, "");
        }

        @Override
        protected Weather doInBackground(String... params) {
            String data = new WeatherHttpClient().getWeatherData(params[0], false);
            jsonData = data;
            weather = JSONParser.getWeather(data);

            return weather;
        }
    }

    public void fullForecast(View v){
        Intent forecast = new Intent(this, FullForecast.class);
        forecast.putExtra("data", jsonData);
        forecast.putExtra("iconData", icon);
        startActivity(forecast);
    }
    // Initiating Menu XML file (menu.xml)
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.settings) {
            showInputDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {
        Log.v("<LOCATION LISTENER>", "Location changed!");
        /*------- To get city name from coordinates -------- */
            Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
            System.err.println(" Coords: " + loc.getLatitude() + " " + loc.getLongitude());
            new GeolocationTask().execute(new String[]{loc.getLatitude()+ "," + loc.getLongitude() + "&sensor=true"});
        }

        @Override
        public void onProviderDisabled(String provider) { Log.e("Locationlistener", "Provider disabled");}

        @Override
        public void onProviderEnabled(String provider) { Log.e("Locationlistener", "Provider enabled");}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    }

    private boolean canAccessLocation() {
        return(PackageManager.PERMISSION_GRANTED==checkSelfPermission((Manifest.permission.ACCESS_FINE_LOCATION)));
    }

    private void showInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Change City");

        final EditText cityInput = new EditText(MainActivity.this);
        cityInput.setInputType(InputType.TYPE_CLASS_TEXT);
        cityInput.setHint("Sofia,BG");
        builder.setView(cityInput);
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener(){
            @Override
            public void onClick (DialogInterface dialog, int which){
                CitySettings citySettings = new CitySettings(MainActivity.this);
                citySettings.setCity(cityInput.getText().toString());

                String newCity = citySettings.getCity();

                renderWeatherData(newCity);
            }
        });
        builder.show();
    }

}
