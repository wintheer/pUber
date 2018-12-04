package come.has.winther.puber.Fragments;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
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
import come.has.winther.puber.Fragments.InfoFragment;
import come.has.winther.puber.Fragments.ReviewFragment;
import come.has.winther.puber.Fragments.SeeMoreFragment;
import come.has.winther.puber.R;
import come.has.winther.puber.Toilet;

// Fragment implementation is based on a tutorial from https://abhiandroid.com/ui/fragment

public class DetailsFragment extends Fragment implements View.OnClickListener {

    DatabaseReference databaseRef;

    private TextView toiletNameText, infoText, descriptionText, adressText, priceText;
    private Button seeMoreButton, writeReviewButton, requestInfoButton, reportButton;
    private String currentEmail;

    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

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

        //Kasper code starts here:
        populateTextFields();

        // Set up button functionalities
        seeMoreButton = (Button) view.findViewById(R.id.button_details_seeMore);
        seeMoreButton.setOnClickListener(this);

        writeReviewButton = (Button) view.findViewById(R.id.button_details_writeReview);
        writeReviewButton.setOnClickListener(this);

        requestInfoButton = (Button) view.findViewById(R.id.button_details_requestInfo);
        requestInfoButton.setOnClickListener(this);

        reportButton = (Button) view.findViewById(R.id.button_details_report);
        reportButton.setOnClickListener(this);

        return view;
    }

    private void populateTextFields() {

        databaseRef = FirebaseDatabase.getInstance().getReference("toilets/" + currentEmail);

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Toilet toilet = dataSnapshot.getValue(Toilet.class);
                toiletNameText.setText(toilet.getName());
                descriptionText.setText(toilet.getDescription());
                adressText.setText(toilet.getAddress());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button_details_seeMore:
            replaceFragment(new SeeMoreFragment());
                break;
            case R.id.button_details_writeReview:
            replaceFragment(new ReviewFragment());
                break;
            case R.id.button_details_requestInfo:
            replaceFragment(new InfoFragment());
                break;
            case R.id.button_details_report:
                //TODO make a report fragment or something

                break;
        }

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
