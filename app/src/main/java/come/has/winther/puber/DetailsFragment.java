package come.has.winther.puber;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

// Fragment implementation is based on a tutorial from https://abhiandroid.com/ui/fragment

public class DetailsFragment extends Fragment implements View.OnClickListener {

    private TextView toiletNameText, infoText, descriptionText, adressText, priceText;

    public DetailsFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_details, container, false);

        // Fill textviews
        toiletNameText = view.findViewById(R.id.tw_details_toiletName);
        infoText = view.findViewById(R.id.tw_details_info);
        descriptionText = view.findViewById(R.id.tw_detais_description);
        adressText = view.findViewById(R.id.tw_details_adressField);
        priceText = view.findViewById(R.id.tw_details_priceField);

        //Kasper code starts here:
        populateTextFields();

        // Set up button functionalities
        Button seeMoreButton = (Button) view.findViewById(R.id.button_details_seeMore);
        seeMoreButton.setOnClickListener(this);

        Button writeReviewButton = (Button) view.findViewById(R.id.button_details_writeReview);
        writeReviewButton.setOnClickListener(this);

        Button requestInfoButton = (Button) view.findViewById(R.id.button_details_requestInfo);
        requestInfoButton.setOnClickListener(this);

        Button reportButton = (Button) view.findViewById(R.id.button_details_report);
        reportButton.setOnClickListener(this);

        return view;
    }

    private void populateTextFields() {

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
