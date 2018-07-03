package com.example.fatinnabila.spilla;

/**
 * Created by fatin nabila on 4/5/2018.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class RingtonePlayingService2 extends Service {

    private boolean isRunning;
    private Context context;
    MediaPlayer mMediaPlayer;
    private int startId;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("MyActivity", "ALARM HERE");
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {

        final NotificationManager mNM = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        Intent intent1 = new Intent(this.getApplicationContext(), MainBox2.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent1, 0);

        // createNotification1();

        Notification mNotify  = new Notification.Builder(this)
                .setContentTitle("Its time to take Your Pill" + "!")
                .setContentText("Click to view!")
                .setSmallIcon(R.drawable.ic_pills2)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .build();

        String state = intent.getExtras().getString("extra");

        // Log.e("what is going on here  ", state);

        assert state != null;
        switch (state) {
            case "no":
                startId = 0;
                break;
            case "yes":
                startId = 1;

                break;
            default:
                startId = 0;
                break;
        }


        if(!this.isRunning && startId == 1) {

            mMediaPlayer = MediaPlayer.create(this, R.raw.alarm_ring);
            // createNotification1();
            mMediaPlayer.start();
            //createNotification1();


            mNM.notify(0, mNotify);

            this.isRunning = true;
            this.startId = 0;

        }
        else if (!this.isRunning && startId == 0){
            Log.e("if there was not sound ", " and you want end");

            this.isRunning = false;
            this.startId = 0;

        }

        else if (this.isRunning && startId == 1){
            Log.e("if there is sound ", " and you want start");

            this.isRunning = true;
            this.startId = 0;

        }
        else {
            Log.e("if there is sound ", " and you want end");

            mMediaPlayer.stop();
            mMediaPlayer.reset();

            this.isRunning = false;
            this.startId = 0;
        }


        Log.e("MyActivity", "In the service");

        return START_NOT_STICKY;

    }


    @Override
    public void onDestroy() {
        Log.e("JSLog", "on destroy called");
        super.onDestroy();

        this.isRunning = false;
    }



    public void createNotification1() {
        // Prepare intent which is triggered if the
        // notification is selected
        Intent notification_intent = new Intent(this,RingtonePlayingService1.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,notification_intent,0);

        // Build notification
        // Actions are just fake
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setSmallIcon(R.drawable.ic_save_white_24dp);
        notificationBuilder.setContentTitle("Its time to take your medicine");
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setSound(defaultSoundUri);
        notificationBuilder.setContentIntent(pendingIntent);
        //Notification noti = new Notification.Builder(this)
        //.setContentTitle("New mail from " + "test@gmail.com")
//                .setContentText("Subject").setSmallIcon(R.drawable.icon)
//                .setContentIntent(pendingIntent)
//                .addAction(R.drawable.icon, "Call", pendingIntent)
//                .addAction(R.drawable.icon, "More", pendingIntent)
//                .addAction(R.drawable.icon, "And more", pendingIntent).build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // hide the notification after its selected
        // notificationBuilder.flags |= Notification.FLAG_AUTO_CANCEL;

        //notificationManager.notify(0, noti);

        //NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,notificationBuilder.build());


//         Create the NotificationChannel, but only on API 26+ because
//         the NotificationChannel class is new and not in the support library

    }



}
