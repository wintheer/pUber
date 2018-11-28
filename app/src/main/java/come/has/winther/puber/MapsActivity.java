package come.has.winther.puber;

import android.graphics.BitmapFactory;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private String DEBUG = "MapsActivity";

    private GoogleMap mMap;
    private ArrayList<User> locations;
    private Location mCurrentLocation;

    // For getting location of user
    private FusedLocationProviderClient mFusedLocationClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        locations = BackgroundService.getToiletsNearby(new LatLng(56.158, 10.2));
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);



        Location comparison = new Location("");
        comparison.setLatitude(56.158);
        comparison.setLongitude(10.2);

        Utilities.getClosestToilet(locations, comparison);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng denmark = new LatLng(56.158, 10.2);
        float zoomLevel = 16.0f;
        setMarkers(locations);
        mMap.addMarker(new MarkerOptions().position(denmark).title("This is you!"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(denmark, zoomLevel));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // When a marker is clicked, the title (the owner of the toilet) is selected
                String nameOfMarker = marker.getTitle();

                // Open a new fragment
                // TODO THIS IS WHERE TROELS PUTS HIS CODE TO OPEN A NEW FRAGMENT

                // Returns true so that default behavior does not occur
                // (move to the marker and an info windows appears
                return true;

            }
        });
    }

    public void setMarkers(ArrayList<User> usrs) {
        MarkerOptions options = new MarkerOptions();
        for (int i = 0; i < usrs.size(); i++) {
            User currentUser = usrs.get(i);

            options.position(new LatLng(currentUser.getLatitude(), currentUser.getLongitude()))
                    .title(currentUser.getName())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.usericon));

            mMap.addMarker(options);

            Log.d(DEBUG, "Added " + currentUser.getName() + " to map with coordinates: "
            + "(" + currentUser.getLatitude() + ", " + currentUser.getLongitude() + ")");
        }
    }
}
