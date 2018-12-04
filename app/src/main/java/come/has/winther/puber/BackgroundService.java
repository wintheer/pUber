package come.has.winther.puber;


import android.app.IntentService;
import android.content.Intent;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * This class runs task asynch in the background
 * Getting users from firebase
 * Posts/updates information about users
 * //new LatLng(56.158, 10.2); for testing
 */
public class BackgroundService extends IntentService {

    //private DatabaseHandler rootRef = FirebaseDatabase.getInstance().getReference();
    //private conditionRef


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public BackgroundService(String name) {
        super(name);
    }


    public static ArrayList<Toilet> getToiletsNearby(LatLng ownLocation) {
        ArrayList<Toilet> locationsToReturn = new ArrayList<>();

        Toilet t1 = new Toilet("kasperps95@gmail.com","kasperps95@gmail,com","You will like this","Åbogade 34, 8200 Aarhus N","$2","Don't poop near the cat",56.157085,10.207051);
        /*
        User userOne = new User("Kasper", 56.157085, 10.207051);
        User userTwo = new User("Troels", 56.158599, 10.203087);
        User userThree = new User("Abdul", 56.158898, 10.197226);
        User userFour = new User("Adolf", 56.157148, 10.197934);
        User userFive = new User("Eva", 56.158961, 10.200341);
        User userSix = new User("Frederik", 56.157179, 10.200171);

        locationsToReturn.add(userOne);
        locationsToReturn.add(userTwo);
        locationsToReturn.add(userThree);
        locationsToReturn.add(userFour);
        locationsToReturn.add(userFive);
        locationsToReturn.add(userSix);
        */

        locationsToReturn.add(t1);

        return locationsToReturn;
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        String dataString = intent.getDataString();
    }




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
