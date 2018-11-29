package come.has.winther.puber;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.util.ExtraConstants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class TestMapsActivity extends AppCompatActivity {

    private TextView name;

    @NonNull
    public static Intent createIntent(@NonNull Context context, @Nullable IdpResponse response) {
        return new Intent().setClass(context, TestMapsActivity.class)
                .putExtra(ExtraConstants.IDP_RESPONSE, response);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_maps);

        name = findViewById(R.id.nameField);

        //checks if user is logged in. If not, LoginActivity will be started
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            startActivity(LoginActivity.createIntent(this));
            finish();
            return;
        }

        IdpResponse response = getIntent().getParcelableExtra(ExtraConstants.IDP_RESPONSE);

        FirebaseFirestore db  = FirebaseFirestore.getInstance();

        populateUI(response);

    }

    private void populateUI(@Nullable IdpResponse response) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        name.setText(user.getDisplayName());


    }
}
