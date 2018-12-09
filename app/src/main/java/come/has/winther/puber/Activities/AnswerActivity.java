package come.has.winther.puber.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import come.has.winther.puber.Activities.MapsActivity;
import come.has.winther.puber.R;

public class AnswerActivity extends AppCompatActivity {

    private String usernameOfRequest;

    private Button acceptButton, declineButton;
    private TextView descriptionTextView;
    private DatabaseReference databaseRef;
    private String descriptionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        descriptionTextView = findViewById(R.id.tw_answer_description);
        acceptButton = findViewById(R.id.btn_answer_accept);
        declineButton = findViewById(R.id.btn_answer_decline);

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptRequest();
            }
        });
        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                declineRequest();
            }
        });

        onNewIntent(getIntent());

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle extras = intent.getExtras();
        if (extras != null) {
            Log.d("AnswerActivity", "There were extras!");
            usernameOfRequest = extras.getString("request");
            descriptionText = extras.getString("username");

            String descriptionToShow = descriptionText + " " + getResources().getString(R.string.requestedToSeeYourInformation);

            descriptionTextView.setText(descriptionToShow);

            databaseRef = FirebaseDatabase.getInstance().getReference("users").child(usernameOfRequest)
                    .child("notification").child("acceptedUsername");
        }
    }

    private void declineRequest() {
        databaseRef.setValue("no");
        Intent i = new Intent(this, MapsActivity.class);
        startActivity(i);
    }

    private void acceptRequest() {
        databaseRef.setValue(usernameOfRequest);
        Intent i = new Intent(this, MapsActivity.class);
        startActivity(i);
    }
}
