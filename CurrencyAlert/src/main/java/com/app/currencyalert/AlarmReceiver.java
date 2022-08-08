package com.app.currencyalert;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.app.currencyalert.interfaces.CustomCallback;
import com.app.currencyalert.jsonResponse.GetData;


public class AlarmReceiver extends BroadcastReceiver {

    private NotificationManager notifManager;


    @Override
    public void onReceive(Context context, Intent intent) {
        String btc = intent.getStringExtra("value");

        System.out.println("?????????????????>>>>>>>>>>>>>>>>>>>>>>>> received !!!!!! " + btc);

        createNotification("Current value ", context, btc);
    }

    public void createNotification(String aMessage, Context context, String btc) {
        final int NOTIFY_ID = 0; // ID of notification
        String id = "123"; // default_channel_id
        String title = "Home Recipe"; // Default Channel
        Intent intent;
        PendingIntent pendingIntent;
        NotificationCompat.Builder builder;
        if (notifManager == null) {
            notifManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notifManager.getNotificationChannel(id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, title, importance);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notifManager.createNotificationChannel(mChannel);
            }
            builder = new NotificationCompat.Builder(context, id);
            intent = new Intent(context, MainActivity.class);
            pendingIntent = PendingIntent.getActivity(context, 123, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            builder.setContentTitle(aMessage + btc)                            // required message
                    .setSmallIcon(android.R.drawable.ic_popup_reminder)   // required
                    .setContentText(context.getString(R.string.app_name)) // required this is mainly title
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setTicker(aMessage)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        }
        else {
            builder = new NotificationCompat.Builder(context, id);
            intent = new Intent(context, MainActivity.class); //which class it should go
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(context, 123, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            builder.setContentTitle(aMessage + btc)                            // required
                    .setSmallIcon(android.R.drawable.ic_popup_reminder)   // required
                    .setContentText(context.getString(R.string.app_name)) // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setTicker(aMessage)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setSound(Settings.System.DEFAULT_RINGTONE_URI);
        }
        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_INSISTENT;
        notifManager.notify(NOTIFY_ID, notification);


    }
}
