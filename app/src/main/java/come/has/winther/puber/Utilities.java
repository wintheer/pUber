package come.has.winther.puber;

import android.location.Location;

import java.util.ArrayList;

public class Utilities {


    /**
     * Returns the username who is cloests to the given location
     */
    public static String getClosestToilet(ArrayList<User> users, Location currentLocation) {

        String usernameToReturn = "";
        float lowestDistance = 999999999;

        for (User u: users) {
            float distance = currentLocation.distanceTo(u.getToiletLocation());
            if (distance < lowestDistance) {
                lowestDistance = distance;
                usernameToReturn = u.getName();
            }
        }
        return usernameToReturn;
    }
}
