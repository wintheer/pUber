package come.has.winther.puber;

import android.location.Location;

public class Toilet {

    private String owner, name, description, address, price, info;
    private double longitude, latitude;
    private Location toiletLocation;



    public Toilet(String owner, String name, String description, String address, String price, String info, double latitude, double longitude) {
        this.owner = owner;
        this.name = name;
        this.description = description;
        this.address = address;
        this.price = price;
        this.info = info;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Toilet(){
        //this is a no-argument constructor. It is needed
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
