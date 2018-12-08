package come.has.winther.puber;


import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

/**
 * This class runs task asynch in the background
 * Getting users from firebase
 * Posts/updates information about users
 * //new LatLng(56.158, 10.2); for testing
 */
public class BackgroundService extends Service {

    private static final String TAG = "BackgroundService";

    private final IBinder binder = new LocalBinder();

    FirebaseUser currentUser;


    public BackgroundService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }


    //create ability to bind
    public class LocalBinder extends Binder{
        public BackgroundService getService(){
            return BackgroundService.this;
        }
    }


    public void notifyUser(){
        //show notification with persons name
        currentUser.getDisplayName();
        Log.d(TAG, "Notification method called");
    }





    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy is called");
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

}
