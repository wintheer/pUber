package come.has.winther.puber;


import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class BackgroundService {


    //new LatLng(56.158, 10.2);

    public static ArrayList<User> getToiletsNearby(LatLng ownLocation) {
        ArrayList<User> locationsToReturn = new ArrayList<>();

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

        return locationsToReturn;
    }
}
