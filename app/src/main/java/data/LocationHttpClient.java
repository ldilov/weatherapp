package data;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import utility.Utils;



public class LocationHttpClient {
    public String getLocationData(String coords){
        InputStream input = null;
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) (new URL(Utils.LOCATION_URL + coords)).openConnection();
            Log.v("----Connection-----", "Connected opened but not connected!");
            connection.setRequestMethod("GET");
            connection.setDoInput(true); //shte poluchavame prez instream
            connection.setDoInput(true);
            connection.connect();

            Log.v("----Connection-----", "Connected successfully to geolocation api!");
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
            System.err.println("Error during connection to Google Geolocation API url.");
            e.printStackTrace();
        }

        return null;
    }
}
