package come.has.winther.puber.Fragments;

import android.app.Fragment;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import come.has.winther.puber.Activities.MapsActivity;
import come.has.winther.puber.R;
import come.has.winther.puber.Toilet;
import come.has.winther.puber.Utilities;

public class ProfileFragment extends Fragment {

    private Button buttonSaveValues;
    private String currentEmail, descriptionText, addressText;
    private DatabaseReference databaseRefToilet;
    TextView tw_address, tw_description;

    private MapsActivity mapsActivity;

    private Toilet currentToilet;

    private String DEBUG = "ProfileFragment";
    private Location currentLocation;
    private FirebaseUser currentUser;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        View view =  inflater.inflate(R.layout.fragment_your_toilet, container, false);

        mapsActivity = (MapsActivity) getActivity();
        currentUser = mapsActivity.getCurrentUser();
        currentEmail = Utilities.encodeUserEmail(currentUser.getEmail());
        currentLocation = mapsActivity.getCurrentLocation();

        buttonSaveValues = view.findViewById(R.id.btn_profile_saveValues);
        buttonSaveValues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tw_address.getText().toString().isEmpty() || tw_description.getText().toString().isEmpty()) {
                    // You should fill these!
                    Toast.makeText(mapsActivity, getResources().getString(R.string.fillOutInformation), Toast.LENGTH_SHORT).show();
                } else {
                    if (currentToilet == null) {
                        saveData();
                    } else {
                        updateData();
                    }
                }
            }
        });

        tw_address = view.findViewById(R.id.tw_profile_address);
        tw_description = view.findViewById(R.id.tw_profile_description);

        populateTextFields();

        return view;
    }



    /**
     * Method for getting data of the logged in user's toilet.
     */
    private void populateTextFields() {

        databaseRefToilet = FirebaseDatabase.getInstance().getReference("toilets/" + currentEmail);

        databaseRefToilet.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentToilet = dataSnapshot.getValue(Toilet.class);
                if (currentToilet != null) {
                    Log.d(DEBUG, "Toilet is not null");
                    tw_description.setText(currentToilet.getInfo());
                    tw_address.setText(currentToilet.getAddress());
                }
                Log.d(DEBUG, "toilet is null.");
                if (currentToilet == null)
                    buttonSaveValues.setText(getResources().getString(R.string.addToilet));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void updateData() {
        FirebaseDatabase.getInstance().getReference("toilets/").child(currentEmail).child("address").setValue(tw_address.getText().toString());
        FirebaseDatabase.getInstance().getReference("toilets/").child(currentEmail).child("info").setValue(tw_description.getText().toString());
    }

    private void saveData() {
        Toilet toilet = new Toilet(currentUser.getDisplayName(), tw_address.getText().toString(), "1", tw_description.getText().toString(), currentLocation.getLatitude(), currentLocation.getLongitude());
        FirebaseDatabase.getInstance().getReference("toilets/").child(currentEmail).setValue(toilet);
    }
}
