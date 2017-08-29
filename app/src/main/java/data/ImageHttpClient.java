package data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import utility.Utils;


public class ImageHttpClient {
    public Bitmap getImage(String code){
        HttpURLConnection connection = null;
        InputStream imgStream = null;
        int response = -1;
        Bitmap bitmap = null;

        try {
            connection = (HttpURLConnection) (new URL(Utils.ICON_URL + code + ".png")).openConnection();
            Log.v("----Connection-----", "Connected opened but not connected!");
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoInput(true);
            connection.connect();

            response = connection.getResponseCode();
            if(response == HttpURLConnection.HTTP_OK){
                imgStream = connection.getInputStream();
            } else {
                System.err.println("Error in status code: " + response);
                return null;
            }

            bitmap = BitmapFactory.decodeStream(imgStream);
            imgStream.close();
            connection.disconnect();

        } catch (IOException e){
            System.err.println("Error connecting for image download.");
            e.printStackTrace();
        }

        return bitmap;
    }
}
