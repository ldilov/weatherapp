package model;


public class Location {
    private double longitude;
    private double latitude;
    private long sunset;
    private long sunrise;
    private String country;
    private String city;

    public double getLongitude() {
        return longitude;
    }

    public long getSunrise() {
        return sunrise;
    }

    public long getSunset() {
        return sunset;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setSunrise(long sunrise) {
        this.sunrise = sunrise;
    }

    public void setSunset(long sunset) {
        this.sunset = sunset;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setCity(String city) {
        this.city = city;
    }
}