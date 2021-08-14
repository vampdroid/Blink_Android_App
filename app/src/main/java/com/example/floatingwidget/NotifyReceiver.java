package com.example.floatingwidget;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.net.URI;

public class NotifyReceiver extends BroadcastReceiver {
    public final String CHANNEL_ID3="3";
    public final String CHANNEL_NAME3="REST_NOTIFICATION";


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel(NotificationManager nm) {
        NotificationChannel nc=new NotificationChannel(CHANNEL_ID3,CHANNEL_NAME3, NotificationManager.IMPORTANCE_DEFAULT);
        nc.enableVibration(true);
        nm.createNotificationChannel(nc);
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i=new Intent(context,MainActivity.class);
        NotificationManager nm=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(nm);
        }
        PendingIntent pi=PendingIntent.getActivity(context,0,i,0);
        NotificationCompat.Builder n= new NotificationCompat.Builder(context,CHANNEL_ID3)
                .setContentTitle("Hang On!")
                .setContentText("Give Some Rest To Your Eyes")
                .setContentIntent(pi)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
                .setSmallIcon(R.drawable.ic_remove_red_eye_black_24dp);
        nm.notify(3,n.build());
    }
}
