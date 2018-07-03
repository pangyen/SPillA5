package com.example.fatinnabila.spilla;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by fatin nabila on 14/5/2018.
 */

public class AlarmReceiver3 extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String state = intent.getExtras().getString("extra");
        Log.e("MyActivity3", "In the receiver with " + state);

        Intent serviceIntent = new Intent(context,RingtonePlayingService3.class);
        serviceIntent.putExtra("extra", state);

        context.startService(serviceIntent);
    }
}
