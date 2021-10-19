package com.example.foregroundservice20072021;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button mBtnStartService, mBtnStopService;
    MyService myService;
    Handler handler;
    boolean mBound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnStartService = findViewById(R.id.buttonStartForeground);
        mBtnStopService = findViewById(R.id.buttonStopForeground);

        mBtnStartService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Song song = new Song(R.raw.yeulacuoi, "Yêu là cưới");
                Intent intent = new Intent(MainActivity.this, MyService.class);
                intent.putExtra("song", song);
                bindService(intent, serviceConnection, BIND_AUTO_CREATE);
                startService(intent);
            }
        });

        mBtnStopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unbindService(serviceConnection);
                Intent intent = new Intent(MainActivity.this, MyService.class);
                stopService(intent);
            }
        });
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("BBB", "Connect");
            MyService.MyBind myBind = (MyService.MyBind) service;
            myService = myBind.getService();
            handler = new Handler();
            handler.postDelayed(callback, 1000);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("BBB", "Disconnect");
            handler.removeCallbacks(callback);
        }
    };

    private Runnable callback = new Runnable() {
        @Override
        public void run() {
            if (myService != null && myService.mMediaPlayer != null){
                int time = myService.mMediaPlayer.getCurrentPosition();
                if (time == -1) {
                    return;
                }
                long minus = (time / 60000);
                long second = (time % 60000) / 1000;
                Log.d("BBB", "Current time song : " + "0" + minus + " : " + (second >= 10 ? second : "0" + second));
                handler.postDelayed(this, 1000);
            }else{
                handler.removeCallbacks(this);
            }
        }
    };

}