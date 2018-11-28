package come.has.winther.puber;

import android.location.Location;

public class User {

    private double longitude, latitude;
    private String name;
    private Location toiletLocation;

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
