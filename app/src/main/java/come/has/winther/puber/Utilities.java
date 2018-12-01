package come.has.winther.puber;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;

public class Utilities {

    public final static int MY_PERMISSIONS_REQUEST_LOCATION_SERVICE = 151;


    public static void checkForGPSPermission(Activity thisActivity) {
        // If the user has deactivated GPS
        if (ContextCompat.checkSelfPermission(thisActivity,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

                // Requests that the user activates (fine) Location services
                ActivityCompat.requestPermissions(thisActivity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION_SERVICE);

        } else {
            // Permission has already been granted
        }
    }
}
