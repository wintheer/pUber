package come.has.winther.puber;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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


