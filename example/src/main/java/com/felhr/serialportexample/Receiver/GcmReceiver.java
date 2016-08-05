package com.felhr.serialportexample.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;

/**
 * Created by Taha on 04/08/2016.
 */
public class GcmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(2000);

            Bundle bundle = intent.getExtras();
            String title = bundle.getString("title");
            String m = bundle.getString("m");

            Intent i = new Intent();
            i.setClassName("com.felhr.serialportexample", "com.felhr.serialportexample.MainActivity");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("Value",m);
            context.startActivity(i);
        }
        catch (Exception ex)
        {
            Log.e(ex.toString(), "Error : ");
        }
    }
}
