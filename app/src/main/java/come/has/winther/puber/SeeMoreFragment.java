package come.has.winther.puber;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

// Fragment implementation is based on a tutorial from https://abhiandroid.com/ui/fragment

public class SeeMoreFragment extends Fragment {

    public SeeMoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_see_more, container, false);
    }


}
