package come.has.winther.puber;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import come.has.winther.puber.Activities.MapsActivity;

public class AnswerActivity extends AppCompatActivity {

    private String usernameOfRequest;

    private Button acceptButton, declineButton;
    private TextView descriptionTextView;
    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        usernameOfRequest = savedInstanceState.getString("username");

        descriptionTextView = findViewById(R.id.tw_answer_description);
        acceptButton = findViewById(R.id.btn_answer_accept);
        declineButton = findViewById(R.id.btn_answer_decline);

        databaseRef = FirebaseDatabase.getInstance().getReference("users").child(usernameOfRequest)
                .child("notification").child("acceptedUsername");

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
