package come.has.winther.puber.Fragments;

import android.app.FragmentTransaction;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
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

// Fragment implementation is based on a tutorial from https://abhiandroid.com/ui/fragment

public class DetailsFragment extends Fragment {

    DatabaseReference databaseRef;
    DatabaseReference ownerRef;

    private TextView toiletNameText, infoText, descriptionText, adressText, priceText;
    private Button seeMoreButton, writeReviewButton, requestUsage, reportButton;
    private String currentEmail, token, owner;
    public static final String TAG = "DetailsFragment";

    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        MapsActivity mapsActivity = (MapsActivity) getActivity();
        currentEmail = mapsActivity.getChosenToilet();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        // Fill textviews
        toiletNameText = view.findViewById(R.id.tw_details_toiletName);
        infoText = view.findViewById(R.id.tw_details_infoField);
        descriptionText = view.findViewById(R.id.tw_detais_description);
        adressText = view.findViewById(R.id.tw_details_adressField);
        priceText = view.findViewById(R.id.tw_details_priceField);

        //get database reference
        databaseRef = FirebaseDatabase.getInstance().getReference("toilets/" + currentEmail);
        ownerRef = FirebaseDatabase.getInstance().getReference("users/" + owner);

        //populate UI
        populateTextFields();

        // Set up button functionalities

        requestUsage = (Button) view.findViewById(R.id.button_details_requestInfo);
        requestUsage.setOnClickListener(new View.OnClickListener() {
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

        return view;
    }

    private void requestUsage() {
        Snackbar sb = Snackbar.make(getActivity().findViewById(android.R.id.content), "Usage request sent to "+toiletNameText.getText(),Snackbar.LENGTH_SHORT);
        sb.show();

        owner = databaseRef.getKey();



        ownerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                token = (String) dataSnapshot.child("token").getValue();
                Log.w(TAG,"Token: "+token);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        Log.w(TAG,ownerRef.child("token").toString());
        Log.w(TAG,"Owner of toilet is: "+owner);

    }

    private void report() {
        Snackbar sb = Snackbar.make(getActivity().findViewById(android.R.id.content), "Toilet has been reported",Snackbar.LENGTH_SHORT);
        sb.show();
    }

    private void populateTextFields() {


        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Toilet toilet = dataSnapshot.getValue(Toilet.class);
                toiletNameText.setText(toilet.getOwner());
                descriptionText.setText(toilet.getDescription());
                adressText.setText(toilet.getAddress());
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




  /*  // based on answer from Krupal Shah at https://stackoverflow.com/questions/32700818/how-to-open-a-fragment-on-button-click-from-a-fragment-in-android
    @Override
    public void onClick(View v) {
        Fragment fragment = null;
        switch (v.getId()) {
            case R.id.button_details_seeMore:
                fragment = new SeeMoreFragment();
                replaceFragment(fragment);
                break;

            case R.id.button_details_writeReview:
                fragment = new ReviewFragment();
                replaceFragment(fragment);
                break;
        }
    }

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

*/

}
