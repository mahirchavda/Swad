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

        String orderid , name, mute, message;
        int ordertime;

        if(remoteMessage.getData() != null){
            Log.d(TAG, "Message data payload: " + remoteMessage.getData().get("name"));
            orderid = remoteMessage.getData().get("orderid");

            ordertime = new Long((Long.parseLong(remoteMessage.getData().get("ordertime")) - new Long("1523211000000"))).intValue();
            name = remoteMessage.getData().get("name");
            mute = remoteMessage.getData().get("mute");
            message = remoteMessage.getData().get("message");

            sendNotification(ordertime,name, message, mute);
        }

    }

    private void sendNotification(int notificationId,String notificationTitle, String notificationBody, String mute) {

        long[] v = {500,1000};

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.food)
                .setContentTitle(notificationTitle)
                .setContentText(notificationBody)
                .setAutoCancel(true);

        if(mute.compareToIgnoreCase("false") == 0){
            notificationBuilder.setSound(defaultSoundUri);
            notificationBuilder.setVibrate(v);
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(1 , notificationBuilder.build());

    }
}