package come.has.winther.puber.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Fragment;

import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import come.has.winther.puber.Activities.MapsActivity;
import come.has.winther.puber.R;
import come.has.winther.puber.Toilet;

public class MyMapFragment extends Fragment {

    private LocationManager locationManager;
    private LocationListener locationListener;
    private BroadcastReceiver broadcastReceiver;

    // Interface for passing data between activity and this fragment.
    public interface OnDataPass {
        void onDataPass(String data);

        void onDataPass(Location location);
    }

    // Implementation based on Arshu's answer on https://stackoverflow.com/questions/19353255/how-to-put-google-maps-v2-on-a-fragment-using-viewpager
    private GoogleMap map;
    private ArrayList<Toilet> locations;
    private Location mCurrentLocation;

    float zoomLevel = 14;
    Marker ownMarker;
    private FusedLocationProviderClient mFusedLocationClient;

    private String DEBUG = "MyMapFragment";
    private OnDataPass dataPasser;

    private ArrayList<Marker> addedMarkers;
    private FirebaseUser loggedInUser;

    private Button buttonGetNearest, buttonProfile;
    private FusedLocationProviderClient getmFusedLocationClient;

    MapView mapView;
    MapsActivity mapsActivity;

    public MyMapFragment() {

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        addedMarkers = new ArrayList<>();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        mFusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // Successfully received the last known location
                if (location != null) {
                    mCurrentLocation = location;
                    changeLocation(mCurrentLocation);
                }
            }
        });

        mapsActivity = (MapsActivity) getActivity();
        loggedInUser = mapsActivity.getCurrentUser();
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("Location:", location.toString());
                mCurrentLocation = location;
                changeLocation(location);
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
        };

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(DEBUG, "User did not give location permissions!");
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }

    private void changeLocation(Location location) {
        if (ownMarker != null) {
            ownMarker.remove();
        }
        dataPasser.onDataPass(location);
        double dLatitude = location.getLatitude();
        double dLongitude = location.getLongitude();

        if (map != null) {
            ownMarker = map.addMarker(new MarkerOptions().position(new LatLng(dLatitude, dLongitude)).
                    title(getResources().getString(R.string.yourLocation))
                    .icon(BitmapDescriptorFactory.defaultMarker()));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(dLatitude, dLongitude), zoomLevel));
        }
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

        locations = mapsActivity.getToilets();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                setMarkers(mapsActivity.getToilets());
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Log.d(DEBUG, "User did accept Location services!");
                    Toast.makeText(getActivity(), getResources().getString(R.string.notEnoughPermissions), Toast.LENGTH_SHORT).show();
                    return;
                }
                //map.setMyLocationEnabled(true);

                map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        // When a marker is clicked, the title (the owner of the toilet) is selected
                        String nameOfMarker = marker.getTitle();

                        // If the user presses himself, nothing will happen
                        if (!nameOfMarker.equals(getResources().getString(R.string.yourLocation))) {
                            // Open a new fragment
                            passData(nameOfMarker);
                            passData(mCurrentLocation);
                            loadFragment(new DetailsFragment());
                        }

                        // Returns true so that default behavior does not occur (move to the marker and an info windows appears
                        return true;
                    }
                });
            }
        });

        buttonProfile = view.findViewById(R.id.btn_maps_ownProfile);
        buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open fragment for profile
                loadFragment(new ProfileFragment());
            }
        });
        buttonGetNearest = view.findViewById(R.id.btn_maps_nearestToilet);
        buttonGetNearest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }


    public void setMarkers(ArrayList<Toilet> usrs) {
        MarkerOptions options = new MarkerOptions();
        for (int i = 0; i < usrs.size(); i++) {
            Toilet currentToilet = usrs.get(i);
            if (!addedMarkers.isEmpty()) {
                // This code will slow down the app a lot when there are many users on pUber
                // Sees if a marker with the same name is added to the map, if so, remove him from the map
                for (int j = 0; j < addedMarkers.size(); j++) {
                    if (usrs.get(i).getOwner().equals(addedMarkers.get(j).getTitle())) {
                        // remove the marker
                        addedMarkers.get(j).remove();
                    }
                }
            }
            options.position(new LatLng(currentToilet.getLatitude(), currentToilet.getLongitude()))
                    .title(currentToilet.getOwner())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.usericon));

            Marker m = map.addMarker(options);

            Log.d(DEBUG, "Added " + currentToilet.getOwner() + " to map with coordinates: "
                    + "(" + currentToilet.getLatitude() + ", " + currentToilet.getLongitude() + ")");
            // List for removing duplicates
            addedMarkers.add(m);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapView != null)
            mapView.onResume();

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(MapsActivity.BROADCAST_DATA_CHANGED)) {
                    // Received message that toilet has been added to list
                    Log.d(DEBUG, "Received broadcast data.");
                    locations = mapsActivity.getToilets();
                    setMarkers(locations);
                }
            }
        };
        IntentFilter filter = new IntentFilter(MapsActivity.BROADCAST_DATA_CHANGED);
        getActivity().registerReceiver(broadcastReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mapView != null)
            mapView.onPause();

        getActivity().unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mapView != null)
            mapView.onDestroy();

        locationManager.removeUpdates(locationListener);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView != null)
            mapView.onLowMemory();
    }

    public void passData(String data) {
        dataPasser.onDataPass(data);
    }

    public void passData(Location location) {
        dataPasser.onDataPass(location);
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
