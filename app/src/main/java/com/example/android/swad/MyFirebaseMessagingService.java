package com.example.android.swad;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "okokok";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        //Log.d(TAG, "From: " + remoteMessage.getFrom());

        String notificationTitle = null;
        String notificationBody = null;
        String notificationId = null;
        int quantity,remaining;
        String mute = null;


        if(remoteMessage.getData() != null){
            Log.d(TAG, "Message data payload: " + remoteMessage.getData().get("name"));
            notificationId = remoteMessage.getData().get("orderid");
            notificationTitle = remoteMessage.getData().get("name");
            mute = remoteMessage.getData().get("mute");
            remaining = Integer.parseInt(remoteMessage.getData().get("remaining"));
            quantity = Integer.parseInt(remoteMessage.getData().get("quantity"));
            if (quantity - remaining - 1 <= 0){
                notificationBody = "Preparing Started";
            } else if(mute.compareToIgnoreCase("false") == 0) {
                notificationBody = "Order is Completed";
            } else {
                notificationBody = "" + (quantity-remaining - 1) + " of " + quantity + " completed";
            }
        }
        sendNotification(notificationId,notificationTitle, notificationBody, mute);
    }

    private void sendNotification(String notificationId,String notificationTitle, String notificationBody, String mute) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("title", "title");
        intent.putExtra("message", "message");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        long[] v = {500,1000};

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.food)
                .setContentTitle(notificationTitle)
                .setContentText(notificationBody)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        if(mute.compareToIgnoreCase("false") == 0){
            notificationBuilder.setSound(defaultSoundUri);
            notificationBuilder.setVibrate(v);
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(1 , notificationBuilder.build());

    }
}