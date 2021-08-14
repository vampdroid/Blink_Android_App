package com.example.floatingwidget;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class BlinkActivty extends AppCompatActivity{
ImageView openeyes,closedeyes;
Vibrator vibrator;
int count,stopcount;
Button btnstop;
     Handler changeHandler;
    Runnable runnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blink_activty);
        openeyes = findViewById(R.id.openeyes);
        closedeyes = findViewById(R.id.closedeyes);
        count = 0;
        stopcount = 0;
        btnstop = findViewById(R.id.stopbtn);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        changeHandler= new Handler(getMainLooper());
        runnable = new Runnable() {
            @Override
            public void run() {
                if (count == 0) {
                    closedeyes.setVisibility(View.VISIBLE);
                    openeyes.setVisibility(View.INVISIBLE);
                    vibrator.vibrate(80);
                    changeHandler.postDelayed(this, 500);
                    count += 1;
                } else if (count == 1) {
                    closedeyes.setVisibility(View.INVISIBLE);
                    openeyes.setVisibility(View.VISIBLE);
                    changeHandler.postDelayed(this, 200);
                    count += 1;
                } else if (count == 2) {
                    closedeyes.setVisibility(View.VISIBLE);
                    openeyes.setVisibility(View.INVISIBLE);
                    vibrator.vibrate(80);
                    changeHandler.postDelayed(this, 600);
                    count += 1;
                } else if (count == 3) {
                    closedeyes.setVisibility(View.INVISIBLE);
                    openeyes.setVisibility(View.VISIBLE);
                    changeHandler.postDelayed(this, 100);
                    count += 1;
                } else if (count == 4) {
                    closedeyes.setVisibility(View.VISIBLE);
                    openeyes.setVisibility(View.INVISIBLE);
                    vibrator.vibrate(80);
                    changeHandler.postDelayed(this, 800);
                    count += 1;
                } else {
                    closedeyes.setVisibility(View.INVISIBLE);
                    openeyes.setVisibility(View.VISIBLE);
                    changeHandler.postDelayed(this, 200);
                    count = 0;
                }
            }
        };
        changeHandler.postDelayed(runnable,500);
        btnstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeHandler.removeCallbacks(runnable);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        changeHandler.removeCallbacks(runnable);
        finish();
    }
}
