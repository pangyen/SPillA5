package com.example.fatinnabila.spilla;

/**
 * Created by fatin nabila on 4/5/2018.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class AlarmReceiver1 extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String state = intent.getExtras().getString("extra");
        Log.e("MyActivity", "In the receiver with " + state);

        Intent serviceIntent = new Intent(context,RingtonePlayingService1.class);
        serviceIntent.putExtra("extra", state);

        context.startService(serviceIntent);
    }
}
