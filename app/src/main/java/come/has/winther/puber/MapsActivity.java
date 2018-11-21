package come.has.winther.puber;

import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<LatLng> locations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locations = BackgroundService.getToiletsNearby(new LatLng(56.158, 10.2));
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        mMap.addMarker(new MarkerOptions().position(denmark).title("This is you!"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(denmark, zoomLevel));
        setMarkers(googleMap, locations);

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // When a marker is clicked, the title (the owner of the toilet) is selected
                String nameOfMarker = marker.getTitle();
                return true;

            }
        });
    }

    public void setMarkers(GoogleMap googleMap, ArrayList<LatLng> latLongs) {
        for (int i = 0; i < latLongs.size(); i++) {
            googleMap.addMarker(new MarkerOptions()
                    .position(latLongs.get(i))
                    .title("Some shithole")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.usericon)));
        }
    }
}
