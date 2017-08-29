package data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import model.GeoLocation;
import model.Location;
import model.Temperature;
import model.Weather;
import model.Wind;
import utility.Utils;


public class JSONParser {
    public static GeoLocation getLocationData(String data){
        GeoLocation geoloc = new GeoLocation();

        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray resultsArr = jsonObject.getJSONArray("results");
            JSONObject resultsObj = resultsArr.getJSONObject(0);
            JSONArray addressArr = resultsObj.getJSONArray("address_components");
            JSONObject locationObj = addressArr.getJSONObject(1);
            JSONObject countryObj = addressArr.getJSONObject(3);

            geoloc.setCity(Utils.getString("long_name", locationObj));
            geoloc.setCountryCode(Utils.getString("short_name", countryObj));

            return geoloc;

        } catch (JSONException e) {
            System.err.println("Error during getting geolocation data in json.");
            e.printStackTrace();
        }

        return null;
    }

    public static Weather getWeather(String data){
        Weather weather = new Weather();

        //create json object ot stringa
        try {
            JSONObject jsonObject = new JSONObject(data);
            Location place = new Location();

            JSONObject coordObj = Utils.getObject("coord", jsonObject);
            place.setLatitude(Utils.getFloat("lat", coordObj));
            place.setLongitude(Utils.getFloat("lon", coordObj));

            //get sys object from OpenWeather api
            JSONObject sysObj = Utils.getObject("sys", jsonObject);
            place.setCountry(Utils.getString("country", sysObj));
            place.setSunrise(Utils.getInt("sunrise", sysObj));
            place.setSunset(Utils.getInt("sunset", sysObj));
            place.setCity(Utils.getString("name", jsonObject));
            weather.place = place;

            //get weather info
            JSONObject mainObj = Utils.getObject("main", jsonObject);
            Temperature temp = new Temperature();
            temp.setMaxTemp(Utils.getFloat("temp_max", mainObj));
            temp.setMinTemp(Utils.getFloat("temp_min", mainObj));
            temp.setTemp(Utils.getFloat("temp", mainObj));
            weather.currentCondition.setTemperature(temp.getTemp());
            weather.currentCondition.setHumidity(Utils.getInt("humidity", mainObj));
            weather.currentCondition.setMaxTemp(temp.getMaxTemp());
            weather.currentCondition.setMinTemp(temp.getMinTemp());
            weather.currentCondition.setPressure(Utils.getFloat("pressure", mainObj));
            weather.temperature = temp;
            
            Wind wind = new Wind();
            JSONObject windObj = Utils.getObject("wind", jsonObject);
            //wind.setDegree(Utils.getFloat("deg", windObj));
            wind.setSpeed(Utils.getFloat("speed", windObj));

            JSONArray jsonArray = jsonObject.getJSONArray("weather");
            JSONObject weatherObj = jsonArray.getJSONObject(0);
            weather.currentCondition.setWeatherId(Utils.getInt("id", weatherObj));
            weather.currentCondition.setDescription(Utils.getString("description", weatherObj));
            weather.currentCondition.setCondition(Utils.getString("main", weatherObj));
            weather.currentCondition.setIcon(Utils.getString("icon", weatherObj));

            JSONObject cloudObj = Utils.getObject("clouds", jsonObject);
            weather.clouds.setPrecipitation(Utils.getInt("all", cloudObj));

            return weather;

        } catch (JSONException e) {
            System.err.println("Error during creation of json object from fetched data.");
            e.printStackTrace();
            return null;
        }

    }

    public static Weather getWeatherByHour(String data, int index){
        Weather weather = new Weather();

        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            JSONObject hourObj = jsonArray.getJSONObject(index);
            JSONObject mainObj = Utils.getObject("main", hourObj);
            weather.currentCondition.setTemperature(Utils.getFloat("temp", mainObj));
            jsonArray = hourObj.getJSONArray("weather");
            JSONObject weatherObj = jsonArray.getJSONObject(0);
            weather.currentCondition.setDescription(Utils.getString("description", weatherObj));
            weather.currentCondition.setIcon(Utils.getString("icon", weatherObj));

            return weather;

        } catch(JSONException e) {
            System.err.println("Error during creation of json week object from fetched data.");
            e.printStackTrace();
            return null;
        }
    }
}
