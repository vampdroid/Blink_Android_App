package com.example.floatingwidget;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;

public class FloatingViewService extends Service implements View.OnClickListener {
Handler handler=new Handler();
    private WindowManager mWindowManager;
    private NotificationManager mNoticationManager;
    private View mFloatingView;
    private View collapsedView;
    private View expandedView;
    public final String CHANNEL_ID1="1";
    public final String CHANNEL_NAME1="FLOATING_SERVICE";
    ImageView collapsed,expanded;
    public static boolean isRunning=false;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mNoticationManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.layout_floating_widget, null);
        final WindowManager.LayoutParams params;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
        {
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        }
        else
        { params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        }
//        params.x=1;
//        params.y=80;
        //image loading
        collapsed=mFloatingView.findViewById(R.id.collapsed_iv);
        expanded=mFloatingView.findViewById(R.id.expanded_iv);
        Glide
                .with(this)
                .load(R.drawable.tenor)
                .into(collapsed);
        Glide
                .with(this)
                .load(R.drawable.tenor)
                .into(expanded);
        //getting windows services and adding the floating view to it
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mFloatingView, params);
       //adding blink count
        final TextView textView =mFloatingView.findViewById(R.id.bigtext);
        textView.post(new Runnable() {
            int i = 0;
            int j=0;
            @Override
            public void run() {
                textView.setText(j+" times");
                i++;
                if (i ==3)
                    i=0;
                    j+=1;
                handler.postDelayed(this, 3120);
            }
        });
        //getting the collapsed and expanded view from the floating view
        collapsedView = mFloatingView.findViewById(R.id.layoutCollapsed);
        expandedView = mFloatingView.findViewById(R.id.layoutExpanded);

        expandedView.setOnClickListener(this);

        //adding an touchlistener to make drag movement of the floating widget
        mFloatingView.findViewById(R.id.relativeLayoutParent).setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;

                    case MotionEvent.ACTION_UP:
                        //when the drag is ended switching the state of the widget
                        collapsedView.setVisibility(View.GONE);
                        expandedView.setVisibility(View.VISIBLE);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        //this code is helping the widget to move around the screen with fingers
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        mWindowManager.updateViewLayout(mFloatingView, params);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        super.onStartCommand(intent, flags, startId);
        isRunning=true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }
        Intent i=new Intent(this,MainActivity.class);
        PendingIntent pi=PendingIntent.getActivity(this,0,i,0);
        NotificationCompat.Builder n= new NotificationCompat.Builder(this,CHANNEL_ID1)
                .setContentTitle("Blink Reminder")
                .setContentText("Eye Blink Reminder Is Floating On Your Screen")
                .setContentIntent(pi)
                .setSmallIcon(R.drawable.ic_remove_red_eye_black_24dp);
        startForeground(1,n.build());

        return START_STICKY;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        NotificationChannel nc=new NotificationChannel(CHANNEL_ID1,CHANNEL_NAME1, NotificationManager.IMPORTANCE_LOW);
        mNoticationManager.createNotificationChannel(nc);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning=false;
        if (mFloatingView != null) mWindowManager.removeView(mFloatingView);
    }
    public static boolean isRunning()
    {
        return isRunning;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layoutExpanded:
                //switching views
                collapsedView.setVisibility(View.VISIBLE);
                expandedView.setVisibility(View.GONE);
                break;
        }
    }
}
