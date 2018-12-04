package come.has.winther.puber.Activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.Context;
import android.content.res.Configuration;
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

import come.has.winther.puber.Fragments.MyMapFragment;
import come.has.winther.puber.R;
import come.has.winther.puber.User;
import come.has.winther.puber.Utilities;

public class MapsActivity extends FragmentActivity implements MyMapFragment.OnDataPass {

    private static final String DEBUG = "MapsActivity";
    DatabaseReference databaseRef;
    FirebaseUser currentUser;
    private String encodedEmail, displayName;

    private String chosenToilet;

    @NonNull
    public static Intent createIntent(@NonNull Context context, @Nullable IdpResponse response) {
        return new Intent().setClass(context, MapsActivity.class)
                .putExtra(ExtraConstants.IDP_RESPONSE, response);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if(currentUser == null){
            startActivity(LoginActivity.createIntent(this));
            finish();
            return;
        }
        addUserToDb();


        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // The phone is in landscape mode, load the two fragments next to one another
            Log.d(DEBUG, "Phone is in landscape mode.");
        }
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // The phone is in portrait mode, load one fragment
            Log.d(DEBUG, "Phone is in portrait mode.");
            loadFragment(new MyMapFragment());
        }

    }

    private void addUserToDb() {
        final FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();

        Log.d(DEBUG, fbUser.getEmail());

        displayName = fbUser.getDisplayName();
        encodedEmail = Utilities.encodeUserEmail(fbUser.getEmail());

        final User dbUser = new User(displayName, fbUser.getEmail());

        databaseRef = FirebaseDatabase.getInstance().getReference("users");

        databaseRef.child(encodedEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // The user already exists
                    Log.d(DEBUG, fbUser.getEmail() + "Already exists");
                } else {
                    // The user does not exist, so lets create him!!
                    databaseRef.child(encodedEmail).setValue(dbUser);
                    Log.d(DEBUG, "Created user with ID: " + encodedEmail);
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


    @Override
    public void onDataPass(String data) {
        Log.d(DEBUG, "data received from fragment: " + data);
        // If the data contains an @ it is an email address
        if (data.contains("@"))
            this.chosenToilet = data;
    }

    public String getChosenToilet() {
        return chosenToilet;
    }
}
