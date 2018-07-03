package com.example.fatinnabila.spilla;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by fatin nabila on 6/5/2018.
 */

public class AlarmReceiver2 extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String state = intent.getExtras().getString("extra");
        //String day = intent.getExtras().getString("day");
        Log.e("MyActivity2", "In the receiver with " + state);

        Intent serviceIntent = new Intent(context,RingtonePlayingService2.class);
        serviceIntent.putExtra("extra", state);
        //serviceIntent.putExtra("day", day);

        context.startService(serviceIntent);
    }
}
