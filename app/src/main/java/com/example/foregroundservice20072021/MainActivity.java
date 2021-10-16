package com.example.foregroundservice20072021;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button mBtnStartService,mBtnStopService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnStartService = findViewById(R.id.buttonStartForeground);
        mBtnStopService = findViewById(R.id.buttonStopForeground);

        mBtnStartService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Song song = new Song(R.raw.yeulacuoi,"Yêu là cưới");
                Intent intent = new Intent(MainActivity.this,MyService.class);
                intent.putExtra("song",song);
                ContextCompat.startForegroundService(MainActivity.this,intent);
            }
        });
    }

}