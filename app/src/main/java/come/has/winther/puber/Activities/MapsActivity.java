package come.has.winther.puber.Activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Context;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.location.Location;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.util.ExtraConstants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;

import come.has.winther.puber.Fragments.MyMapFragment;
import come.has.winther.puber.NotificationService;
import come.has.winther.puber.R;
import come.has.winther.puber.Toilet;
import come.has.winther.puber.User;
import come.has.winther.puber.Utilities;

public class MapsActivity extends FragmentActivity implements MyMapFragment.OnDataPass {

    private static final String DEBUG = "MapsActivity";
    public static final String BROADCAST_DATA_CHANGED = "DATA_CHANGED";
    DatabaseReference databaseRef;
    FirebaseUser currentUser;
    private String encodedEmail, displayName, token;

    private String chosenToilet;
    private Location currentLocation;
    private DatabaseReference toiletRef;
    private ArrayList<Toilet> toilets;

    private boolean serviceBound;
    private ServiceConnection notificationServiceConnection;
    private NotificationService notificationService;

    @NonNull
    public static Intent createIntent(@NonNull Context context, @Nullable IdpResponse response) {
        return new Intent().setClass(context, MapsActivity.class)
                .putExtra(ExtraConstants.IDP_RESPONSE, response);
    }

    public FirebaseUser getCurrentUser() {
        return this.currentUser;
    }

    private void addToilet(String owner, String address, String price, String info, double latitude, double longitude) {
        Toilet toiletToAdd = new Toilet(owner, address, price, info, latitude, longitude);
        if (!toilets.isEmpty()) {
            for(int i = 0; i < toilets.size(); i++) {
                if (toilets.get(i).getOwner().equals(owner)) {
                    toilets.remove(i);
                }
            }
            Log.d("Added:", toiletToAdd.toString());
            toilets.add(toiletToAdd);
        } else {
            Log.d("Added:", toiletToAdd.toString());
            toilets.add(toiletToAdd);
        }

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(BROADCAST_DATA_CHANGED);

        sendBroadcast(broadcastIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toilets = new ArrayList<>();
        setContentView(R.layout.activity_maps);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if(currentUser == null){
            startActivity(LoginActivity.createIntent(this));
            finish();
            return;
        }

        toiletRef = FirebaseDatabase.getInstance().getReference("toilets");

        toiletRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String toiletOwner = ds.getKey();
                    String toiletAddress = (String) ds.child("address").getValue();
                    String toiletPrice = (String) ds.child("price").getValue();
                    String info = (String) ds.child("info").getValue();
                    double toiletLatitude = (double) ds.child("latitude").getValue();
                    double toiletLongitude = (double) ds.child("longitude").getValue();

                    addToilet(toiletOwner, toiletAddress, toiletPrice, info, toiletLatitude, toiletLongitude);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("BUM", "NO PERMISSION");
            }
        });

        Log.d(DEBUG,"FCM: "+ FirebaseInstanceId.getInstance().getToken());

        addUserToDb();

        setupConnectionToShareService();
        bindToSNotificationService();

        loadFragment(new MyMapFragment());
    }

    public ArrayList<Toilet> getToilets() {
        return this.toilets;
    }

    private void addUserToDb() {
        final FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();

        Log.d(DEBUG, fbUser.getEmail());

        displayName = fbUser.getDisplayName();
        encodedEmail = Utilities.encodeUserEmail(fbUser.getEmail());
        token = FirebaseInstanceId.getInstance().getToken();

        final User dbUser = new User(displayName, token, fbUser.getEmail());

        databaseRef = FirebaseDatabase.getInstance().getReference("users");

        databaseRef.child(encodedEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // The user already exists
                    Log.d(DEBUG, fbUser.getEmail() + "Already exists");
                    notificationService.setLoggedInUser(encodedEmail);
                } else {
                    // The user does not exist, so lets create him!!
                    databaseRef.child(encodedEmail).setValue(dbUser);
                    Log.d(DEBUG, "Created user with ID: " + encodedEmail);
                    notificationService.setLoggedInUser(encodedEmail);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Shhh. Don't mind this
            }
        });
    }

    // Fragment implementation is based on a tutorial from https://abhiandroid.com/ui/fragment
    private void loadFragment(Fragment fragment) {
        // create a FragmentManager
        FragmentManager fm = getFragmentManager();
        // create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        // replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.fragmentFrameLayout, fragment).addToBackStack(null);

        fragmentTransaction.commit(); // save the changes
    }

    /**
     * For passing user email
     */
    @Override
    public void onDataPass(String data) {
        Log.d(DEBUG, "data received from fragment: " + data);
        // If the data contains an @ it is an email address
        if (data.contains("@"))
            this.chosenToilet = data;
    }

    /**
     * For passing current location
     */
    @Override
    public void onDataPass(Location location) {
        Log.d(DEBUG, "data received from fragment: " + location.toString());
        // If the data contains an @ it is an email address
            this.currentLocation = location;

    }

    public String getChosenToilet() {
        return chosenToilet;
    }

    public Location getCurrentLocation() {
        return this.currentLocation;
    }

    private void setupConnectionToShareService() {
        notificationServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                notificationService = ((NotificationService.NotificationServiceBinder)service).getService();
                serviceBound = true;

                Log.d("EditActivity", "ShareService connected!");
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                notificationService = null;
                Log.d("EditActivity", "ShareService disconnected!");
            }
        };
    }

    public void bindToSNotificationService() {
        bindService(new Intent(MapsActivity.this, NotificationService.class),
                notificationServiceConnection, Context.BIND_AUTO_CREATE);
        Log.d(DEBUG, "bound to service.");
    }
}
