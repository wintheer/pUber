package come.has.winther.puber;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

public class MyMapFragment extends Fragment  implements LocationListener{
    // Interface for passing data between activity and this fragment.
    public interface OnDataPass {
        void onDataPass(String data);
    }

    // Implementation based on Arshu's answer on https://stackoverflow.com/questions/19353255/how-to-put-google-maps-v2-on-a-fragment-using-viewpager
    private GoogleMap map;
    private ArrayList<Toilet> locations;
    private Location mCurrentLocation;
    private boolean gpsEnabled = false;
    private FusedLocationProviderClient mFusedLocationClient;

    private String DEBUG = "MapsActivity";
    private OnDataPass dataPasser;

    MapView mapView;
    public MyMapFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locations = BackgroundService.getToiletsNearby(new LatLng(56.158, 10.2));
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    // We know know the user's location
                    mCurrentLocation = location;
                }
            }
        });
    }


    /**
     * This works on API >= 23
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(DEBUG, "onAttach has been called");
        dataPasser = (OnDataPass) context;
    }


    /**
     * This works on API < 23
     */
    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        Log.d(DEBUG, "onAttach has been called");
        dataPasser = (OnDataPass) context;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();


        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }


        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap gMap) {
                map = gMap;

                //map.setMyLocationEnabled(true);

                LatLng denmark = new LatLng(56.158, 10.2);
                map.addMarker(new MarkerOptions().position(denmark).title("This is you").snippet("Why are you even here?"));
                float zoomLevel = 16.0f;
                setMarkers(locations);
                map.addMarker(new MarkerOptions().position(denmark).title("This is you!"));

                map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        // When a marker is clicked, the title (the owner of the toilet) is selected
                        String nameOfMarker = marker.getTitle();

                        // Open a new fragment
                        passData(nameOfMarker);
                        loadFragment(new DetailsFragment());

                        // Returns true so that default behavior does not occur (move to the marker and an info windows appears
                        return true;

                    }
                });
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(denmark, zoomLevel));

            }
        });

        return view;
    }


    public void setMarkers(ArrayList<Toilet> usrs) {
        MarkerOptions options = new MarkerOptions();
        for (int i = 0; i < usrs.size(); i++) {
            Toilet currentToilet = usrs.get(i);

            options.position(new LatLng(currentToilet.getLatitude(), currentToilet.getLongitude()))
                    .title(currentToilet.getName())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.usericon));

            map.addMarker(options);

            Log.d(DEBUG, "Added " + currentToilet.getName() + " to map with coordinates: "
                    + "(" + currentToilet.getLatitude() + ", " + currentToilet.getLongitude() + ")");
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void passData(String data) {
        dataPasser.onDataPass(data);
    }

    // Fragment implementation is based on a tutorial from https://abhiandroid.com/ui/fragment
    private void loadFragment(Fragment fragment) {
// create a FragmentManager
        android.app.FragmentManager fm = getFragmentManager();
// create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
// replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.fragmentFrameLayout, fragment).addToBackStack(null);
        fragmentTransaction.commit(); // save the changes
    }
}
