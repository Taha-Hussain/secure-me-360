package com.felhr.serialportexample.Service;


import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.felhr.serialportexample.MainActivity;
import com.felhr.serialportexample.MyNotification;
import com.felhr.serialportexample.R;

public class GcmService extends IntentService {
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    Context context = this;

    public GcmService() {
        super("GCMservice");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Bundle mBundle = intent.getExtras();

        String strTtile = mBundle.getString("title");
        String strMessage = mBundle.getString("m");
        //String strnotificaton_id = mBundle.getString("notification_id");
        int NOTIFICATION_ID =1; //Integer.parseInt(strnotificaton_id);
        sendNotification(strMessage, strTtile, NOTIFICATION_ID);

    }

    private void sendNotification(String msg, String title, int nofication_id) {


        MyNotification mnotification = new MyNotification();
        mnotification.setMsg(msg);
        mnotification.setTitle(title);
        Intent mintent = new Intent(this, MainActivity.class);
        mintent.putExtra("mynotification", mnotification);
        mintent.setAction(Long.toString(System.currentTimeMillis()));

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, mintent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setSound(uri).setContentText(msg)
                .setAutoCancel(true);
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(nofication_id, mBuilder.build());

    }

}
