package come.has.winther.puber;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.firebase.client.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import come.has.winther.puber.Activities.AnswerActivity;

import static android.app.Notification.VISIBILITY_PUBLIC;

/**
 * This class runs task asynch in the background
 * Getting users from firebase
 * Posts/updates information about users
 * //new LatLng(56.158, 10.2); for testing
 */
public class NotificationService extends Service {

    private final IBinder mBinder = new NotificationServiceBinder();
    private ValueEventListener notificationListener;
    private DatabaseReference databaseRef;
    private int DEFAULT_WAIT = 5000; //

    private boolean running;
    private String loggedInUser;

    public class NotificationServiceBinder extends Binder {
        public NotificationService getService() {
            return NotificationService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        /**
        Runnable run = new Runnable() {
            @Override
            public void run() {

                running = true;
                while (running) {
                    // Update each share every two minutes

                    try {
                        Log.d("ShareService", "Running update code.");


                        // Loops through the entire list and updates current price and time

                        Thread.sleep(DEFAULT_WAIT);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        Thread thread = new Thread(run);
        thread.start(); */

    }


    public void setLoggedInUser(String username) {
        this.loggedInUser = Utilities.encodeUserEmail(username);
        listenToNotification();
        Log.d("NotificationService", "received username:" + loggedInUser);
    }

    private void listenToNotification() {
        databaseRef = FirebaseDatabase.getInstance().getReference("users/" + loggedInUser);

        databaseRef.child("notification").addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("messageWaiting").getValue().toString().equals("true")) {
                    String title = getResources().getString(R.string.requestToilet);
                    String message = (String) dataSnapshot.child("usernameWhoRequestedToilet").getValue();
                    sendNotification(title, message);
                    FirebaseDatabase.getInstance().getReference("users/" + loggedInUser).child("notification").child("messageWaiting").setValue(false);
                    Log.d("NotificationService", title + ", " + message);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void sendNotification(String title, String messageBody) {
        Intent intent = new Intent(this, AnswerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("username", loggedInUser);
        intent.putExtra("request", messageBody);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setContentTitle(title)
                        .setContentText(messageBody + getResources().getString(R.string.hasRequestedAccessToYourToilet))
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setContentIntent(pendingIntent)
                        .setVisibility(VISIBILITY_PUBLIC)
                        .setSmallIcon(R.drawable.puberappicon);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
