package com.example.floatingwidget;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class  NotifyService extends Service {
    public static boolean isRunning = false;
    public final String CHANNEL_ID2="2";
    public final String CHANNEL_NAME2="NOTIFY_SERVICE";
    private NotificationManager mNoticationManager;
    private AlarmManager mAlarmManager;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mNoticationManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        mAlarmManager=(AlarmManager) getSystemService(ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        NotificationChannel nc=new NotificationChannel(CHANNEL_ID2,CHANNEL_NAME2, NotificationManager.IMPORTANCE_HIGH);
        mNoticationManager.createNotificationChannel(nc);
    }

    public static boolean isRunning() {
        return isRunning;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        isRunning=true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }
        Intent i=new Intent(this,MainActivity.class);
        PendingIntent pi=PendingIntent.getActivity(this,0,i,0);
        NotificationCompat.Builder n= new NotificationCompat.Builder(this,CHANNEL_ID2)
                .setContentTitle("Eye Rest Reminder")
                .setContentText("I Will Notify You Every Twenty Minutes To Take Rest")
                .setContentIntent(pi)
                .setSmallIcon(R.drawable.ic_remove_red_eye_black_24dp);
        Intent ii = new Intent(this,NotifyReceiver.class);
        PendingIntent pii = PendingIntent.getBroadcast(this,1,ii,0);
        mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),20 * 60 * 1000,pii);
        startForeground(2,n.build());
        return START_STICKY;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning=false;
        Intent i = new Intent(this,NotifyReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this,1,i,0);
        mAlarmManager.cancel(pi);
    }
}
