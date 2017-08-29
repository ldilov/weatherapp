package utility;

import org.json.JSONException;
import org.json.JSONObject;


public class Utils {
    //city name, country code (BG)
    public static final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?q=";
    public static final String BASE_URL_HOURLY = "http://api.openweathermap.org/data/2.5/forecast?q=";
    public static final String LOCATION_URL = "http://maps.googleapis.com/maps/api/geocode/json?latlng=";
    public static final String ICON_URL = "https://openweathermap.org/img/w/";
    public static final String API_KEY = "126f9d28f08382f78863408d36f36577";

    public static JSONObject getObject(String tagName, JSONObject jsonObject) throws JSONException{
        return jsonObject.getJSONObject(tagName);
    }

    public static String getString(String tagName, JSONObject jsonObject) throws JSONException{
        return jsonObject.getString(tagName);
    }

    public static float getFloat(String tagName, JSONObject jsonObject) throws JSONException{
        return (float) jsonObject.getDouble(tagName);
    }

    public static double getDouble(String tagName, JSONObject jsonObject) throws JSONException{
        return (double)jsonObject.getDouble(tagName);
    }

    public static int getInt(String tagName, JSONObject jsonObject) throws JSONException{
        return jsonObject.getInt(tagName);
    }

}
