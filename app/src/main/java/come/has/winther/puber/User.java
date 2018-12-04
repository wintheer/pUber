package come.has.winther.puber;

import android.location.Location;

import com.google.firebase.database.IgnoreExtraProperties;

public class User {

    private double longitude, latitude;
    private String name;
    private Location toiletLocation;
    private String address;
    private String info;
    private float price;

    private String eMailAddress;

    public User(String name, String eMailAddress) {
        this.name = name;
        this.eMailAddress = eMailAddress;
    }

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

    public String geteMailAddress() {
        return eMailAddress;
    }

    public void seteMailAddress(String eMailAddress) {
        this.eMailAddress = eMailAddress;
    }
}
