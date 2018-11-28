package come.has.winther.puber;


import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class BackgroundService {


    //new LatLng(56.158, 10.2);

    public static ArrayList<LatLng> getToiletsNearby(LatLng ownLocation) {
        ArrayList<LatLng> locationsToReturn = new ArrayList<>();

        LatLng locationOne = new LatLng(56.157085, 10.207051);
        LatLng locationTwo = new LatLng(56.158599, 10.203087);
        LatLng locationThree = new LatLng(56.158898, 10.197226);
        LatLng locationFour = new LatLng(56.157148, 10.197934);
        LatLng locationFive = new LatLng(56.158961, 10.200341);
        LatLng locationSix = new LatLng(56.157179, 10.200171);

        locationsToReturn.add(locationOne);
        locationsToReturn.add(locationTwo);
        locationsToReturn.add(locationThree);
        locationsToReturn.add(locationFour);
        locationsToReturn.add(locationFive);
        locationsToReturn.add(locationSix);

        return locationsToReturn;
    }
}
