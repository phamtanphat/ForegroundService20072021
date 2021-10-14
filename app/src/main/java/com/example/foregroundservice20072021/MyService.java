package com.example.foregroundservice20072021;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class MyService extends Service {

    NotificationManager notificationManager;
    Notification notification;
    Thread thread;
    Handler handler;
    Looper looper;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notification = createNotification();
        startForeground(1,notification);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                looper = Looper.myLooper();
                handler = new Handler(looper){
                    @Override
                    public void handleMessage(@NonNull Message msg) {
                        super.handleMessage(msg);
                        switch (msg.what){
                            case 0 :
                                Toast.makeText(MyService.this, msg.arg1 + "", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        looper.quit();
                    }
                };
                for (int i = 0; i < 50; i++) {
                    Log.d("BBB",i + "");
                }
                Message message = handler.obtainMessage();
                message.what = 0;
                message.arg1 = 10000000;
                handler.sendMessage(message);
                Looper.loop();

                Log.d("BBB","The end");
            }
        });
        thread.start();


        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();
    }
    private Notification createNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"CHANNEL_ID");
        builder.setSmallIcon(android.R.drawable.star_on);
        builder.setShowWhen(true);
        builder.setContentTitle("Thông báo!!");
        builder.setPriority(Notification.PRIORITY_HIGH);
        builder.setContentText("Ứng dụng có phiên bản mới");

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(
                    "CHANNEL_ID",
                    "demo",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            notificationManager.createNotificationChannel(notificationChannel);
        }
        return builder.build();
    }
}
