package come.has.winther.puber;

import android.location.Location;

public class User {

    private double longitude, latitude;
    private String name;
    private Location toiletLocation;
    private String address;
    private String info;
    private float price;
    private String token;


    private Notification notification;

    private String eMailAddress;

    public User(String name, String token, String eMailAddress) {
        this.name = name;
        this.token = token;
        this.eMailAddress = eMailAddress;
        notification = new Notification(false, "none", "no");
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
