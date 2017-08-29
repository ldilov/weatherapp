package data;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;


public class CitySettings {
    SharedPreferences prefs;

    public CitySettings(Activity activity){
        prefs = activity.getPreferences(Activity.MODE_PRIVATE);
    }

    public String getCity() {
        return prefs.getString("city", "Sofia,BG");
    }

    public void setCity(String city){
        prefs.edit().putString("city", city).commit();
    }
}
