package come.has.winther.puber;

import android.location.Location;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    private double longitude, latitude;
    private String name;
    private Location toiletLocation;
    private String address;

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    private String info;
    private float price;

    public User(String name, double latitude, double longitude) {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        toiletLocation = new Location(name);
        toiletLocation.setLatitude(latitude);
        toiletLocation.setLongitude(longitude);
    }

    public double getLongitude() {
        return this.longitude;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public Location getToiletLocation() {
        return toiletLocation;
    }
}
