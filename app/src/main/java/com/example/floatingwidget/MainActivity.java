package com.example.floatingwidget;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity{
    private static final int SYSTEM_ALERT_WINDOW_PERMISSION = 2084;
    Button gotoBlinkExAct;
    AlarmManager mAlarmManager;
    Switch blinkswitch;
    Switch notifyme;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAlarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            askPermission();
        }
        TextView t1=(TextView)findViewById(R.id.bigtext);
        blinkswitch=findViewById(R.id.blinkswitch);
        notifyme=findViewById(R.id.notify_me);
        if(FloatingViewService.isRunning())
        {
            blinkswitch.setChecked(true);
        }
        if(NotifyService.isRunning())
        {
            notifyme.setChecked(true);
        }

        notifyme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    Intent i =new Intent(MainActivity.this,NotifyService.class);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(i);
                    }
                    else
                    {
                        startService(i);
                    }
                }
                else
                {
                    Intent i =new Intent(MainActivity.this,NotifyService.class);
                    stopService(i);
                }
            }
        });

        blinkswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                        startService(new Intent(MainActivity.this, FloatingViewService.class));

                    } else if (Settings.canDrawOverlays(MainActivity.this)) {
                        startService(new Intent(MainActivity.this, FloatingViewService.class));

                    } else {
                        askPermission();
                        Toast.makeText(MainActivity.this, "You need System Alert Window Permission to do this", Toast.LENGTH_SHORT).show();
                    }
                    blinkswitch.setChecked(true);

                }
                else{
                    blinkswitch.setChecked(false);
                    stopService(new Intent(MainActivity.this, FloatingViewService.class));
                }
            }
        });
        gotoBlinkExAct=findViewById(R.id.blinkexercise);
        gotoBlinkExAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("Instructions");
                dialog.setMessage("You have to close your eyes as soon as your device vibrates.");
                dialog.setPositiveButton("Start", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i=new Intent(MainActivity.this,BlinkActivty.class);
                        startActivity(i);
                    }
                });
                dialog.show();
            }
        });
    }


    private void askPermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, SYSTEM_ALERT_WINDOW_PERMISSION);
    }

}