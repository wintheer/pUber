package come.has.winther.puber.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import come.has.winther.puber.R;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LOGIN: ";

    private SignInButton signInButton;
    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;
    private final static int RC_SIGN_IN = 100;

    //this class and its methods have been inspired by this


    //this get called from MapsActivity if the user is not signed in
    @NonNull
    public static Intent createIntent(@NonNull Context context) {
        return new Intent(context, LoginActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signIn();
    }

    public void signIn(){
        AuthUI.SignInIntentBuilder builder = AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(getSelectedProviders());
        startActivityForResult(builder.build(), RC_SIGN_IN);
    }


    //start map activity when login-check reports success
    private void startSignedInActivity(@Nullable IdpResponse response) {
        //implement the createIntent method in MapsActivity
        startActivity(MapsActivity.createIntent(this, response));
    }



    //when the app opens, it check whether the user is already logged in
    @Override
    protected void onResume() {
        super.onResume();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            startSignedInActivity(null);
            finish();
        }
    }

    //creates and returns a list of available providers (Google and email)
    private List<AuthUI.IdpConfig> getSelectedProviders() {
        List<AuthUI.IdpConfig> selectedProviders = new ArrayList<>();


        selectedProviders.add(new AuthUI.IdpConfig.GoogleBuilder().build());

        selectedProviders.add(new AuthUI.IdpConfig.EmailBuilder()
                    .setRequireName(true)
                    .setAllowNewAccounts(true)
                    .build());

        return selectedProviders;
    }
}


