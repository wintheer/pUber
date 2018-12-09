package come.has.winther.puber.Fragments;

import android.app.FragmentTransaction;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import come.has.winther.puber.Activities.MapsActivity;
import come.has.winther.puber.R;
import come.has.winther.puber.Toilet;
import come.has.winther.puber.Utilities;

// Fragment implementation is based on a tutorial from https://abhiandroid.com/ui/fragment

public class DetailsFragment extends Fragment {

    DatabaseReference databaseRef;
    DatabaseReference ownerRef;

    private TextView toiletNameText, infoText, descriptionText, addressText, priceText;
    private Button requestUsageButton, reportButton;
    private String currentEmail, token, owner;
    public static final String TAG = "DetailsFragment";
    private String loggedInUser;

    public DetailsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        MapsActivity mapsActivity = (MapsActivity) getActivity();
        currentEmail = mapsActivity.getChosenToilet();
        loggedInUser = Utilities.encodeUserEmail(mapsActivity.getCurrentUser().getEmail());

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        // Fill textviews
        toiletNameText = view.findViewById(R.id.tw_details_toiletName);
        infoText = view.findViewById(R.id.tw_details_infoField);
        addressText = view.findViewById(R.id.tw_details_adressField);
        priceText = view.findViewById(R.id.tw_details_priceField);


        //get database reference
        databaseRef = FirebaseDatabase.getInstance().getReference("toilets/" + currentEmail);
        owner = databaseRef.getKey();

        ownerRef = FirebaseDatabase.getInstance().getReference("users/" + currentEmail);

        ownerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                toiletNameText.setText((String) dataSnapshot.child("name").getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        // Set up button functionalities

        requestUsageButton = (Button) view.findViewById(R.id.button_details_requestInfo);
        requestUsageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestUsage();
            }
        });

        reportButton = view.findViewById(R.id.button_details_report);
        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                report();
            }
        });
        reportButton.setVisibility(View.GONE);

        DatabaseReference acceptedRef = ownerRef.child("notification").child("acceptedUsername");

        acceptedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue().toString().equals(loggedInUser)) {
                    populateTextFields();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    private void requestUsage() {
        Snackbar sb = Snackbar.make(getActivity().findViewById(android.R.id.content), getResources().getString(R.string.requestSentTo)
                + toiletNameText.getText().toString(),Snackbar.LENGTH_SHORT);
        sb.show();

        ownerRef.child("notification").child("messageWaiting").setValue(true);
        ownerRef.child("notification").child("usernameWhoRequestedToilet").setValue(loggedInUser);

        priceText.setText(getResources().getString(R.string.awaitingAcceptance));
        infoText.setText(getResources().getString(R.string.awaitingAcceptance));
        addressText.setText(getResources().getString(R.string.awaitingAcceptance));
    }

    private void report() {
        Snackbar sb = Snackbar.make(getActivity().findViewById(android.R.id.content), getResources().getString(R.string.toiletReported),Snackbar.LENGTH_SHORT);
        sb.show();
    }

    private void populateTextFields() {
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Toilet toilet = dataSnapshot.getValue(Toilet.class);
                infoText.setText(toilet.getInfo());
                addressText.setText(toilet.getAddress());
                priceText.setText(toilet.getPrice());
                reportButton.setVisibility(View.VISIBLE);
                requestUsageButton.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentFrameLayout, fragment );
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
