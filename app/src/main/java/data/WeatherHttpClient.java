package data;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import utility.Utils;


public class WeatherHttpClient {
    public String getWeatherData(String place, boolean hourly){
        InputStream input = null;
        HttpURLConnection connection = null;
        try {
            if(hourly) {
                connection = (HttpURLConnection) (new URL(Utils.BASE_URL_HOURLY + place)).openConnection();
            } else {
                connection = (HttpURLConnection) (new URL(Utils.BASE_URL + place)).openConnection();
            }
            Log.v("----Connection-----", "Connected opened but not connected!");
            connection.setRequestMethod("GET");
            connection.setDoInput(true); //shte poluchavame prez instream
            connection.setDoInput(true);
            connection.connect();

            Log.v("----Connection-----", "Connected successfully!");
            //response stored as characters (unicode)
            StringBuffer stringBuffer = new StringBuffer();
            input = connection.getInputStream();
            //InputStreamReader za da chetem characters
            BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(input));
            String line = null;
            while((line = bufferedReader.readLine()) != null){
                stringBuffer.append(line + "\r\n");
            }

            input.close();
            connection.disconnect();
            return stringBuffer.toString();

        } catch (IOException e){
            System.err.println("Error during connection to Openweather API url.");
            e.printStackTrace();
        }

        return null;
    }
}
