package com.example.foregroundservice20072021;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Binder;
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
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class MyService extends Service {

    NotificationManager notificationManager;
    Notification notification;
    MediaPlayer mMediaPlayer;
    Song song;
    Handler handler;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBind();
    }

    class MyBind extends Binder {
        public MyService getService() {
            return MyService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            song = intent.getParcelableExtra("song");
            startMp3(song);
            notification = createNotification(this, 0, song.name);
            startForeground(1, notification);
            handler = new Handler();
            handler.postDelayed(runnable,1000);
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopMp3();
    }


    private void startMp3(Song song) {
        if (mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer.create(this, song.resourceMp3);
        } else if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = MediaPlayer.create(this, song.resourceMp3);
        }
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });
    }

    private void stopMp3() {
        if (mMediaPlayer == null) {
            return;
        }
        mMediaPlayer.stop();
        mMediaPlayer.release();
        mMediaPlayer = null;
        handler.removeCallbacks(runnable);
    }

    private Notification createNotification(Context context, long duration, String title) {

        Intent intent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "CHANNEL_ID");
        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
        builder.setContentTitle(title);
        builder.addAction(R.mipmap.ic_launcher, "Open App", pendingIntent);

        long minus = (duration / 60000);
        long second = (duration % 60000) / 1000;
        builder.setContentText("Current time song : " + "0" + minus + " : " + (second >= 10 ? second : "0" + second));
        builder.setShowWhen(true);
        builder.setSound(null);
        builder.setPriority(Notification.PRIORITY_DEFAULT);

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("CHANNEL_ID", "CHANNEL_NAME", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setSound(null, null);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        return builder.build();
    }
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (mMediaPlayer != null){
                if (song != null && song.name.length() > 0){
                    notification = createNotification(MyService.this,mMediaPlayer.getCurrentPosition(),song.name);
                    notificationManager.notify(1,notification);
                    handler.postDelayed(this,1000);
                }
            }
        }
    };
}
