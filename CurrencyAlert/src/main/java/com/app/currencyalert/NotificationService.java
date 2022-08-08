package com.app.currencyalert;

import android.app.AlarmManager;
import android.app.NotificationChannel ;
import android.app.NotificationManager ;
import android.app.PendingIntent;
import android.app.Service ;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent ;
import android.content.IntentFilter;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.Handler ;
import android.os.IBinder ;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log ;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import com.app.currencyalert.interfaces.CustomCallback;
import com.app.currencyalert.jsonResponse.GetData;
import java.util.Calendar;


public class NotificationService extends Service {
    protected Handler handler;

    private final LocalBinder mBinder = new LocalBinder();
    int reqCode = 1;

    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    private final static String default_notification_channel_id = "default";


    public class LocalBinder extends Binder {
        public NotificationService getService() {
            return NotificationService.this;
        }
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                // write your code to post content on server
                // Do something here on the main thread
                Log.d("Handlers", "Called on main thread");
                // Repeat this the same runnable code block again another 2 seconds
                // 'this' is referencing the Runnable object
                GetData rs = new GetData();
                rs.getResponse(new CustomCallback(){
                    @Override
                    public void onSuccess(Btc bitcoin){
                        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>> "+Math.round(Float.valueOf(bitcoin.getPrice())));
                        String num=String.format("%,d", Math.round(Float.valueOf(bitcoin.getPrice())));

                        //System.out.println(" -------------------------------- "+Integer.valueOf(Math.round(Float.valueOf(bitcoin.getPrice()))));

                        int val = Integer.valueOf(Math.round(Float.valueOf(bitcoin.getPrice())));
                        if(val < 26500){
                            System.out.println("?????????????????>>>>>>>>>>>>>>>>>>>>>>>>");
                            reminderNotification(num);
                        }
                    }
                    @Override
                    public void onFailure(String val) {

                    }
                });
                handler.postDelayed(this, 60000);
            }
        });
        return android.app.Service.START_STICKY;
    }

    private void reminderNotification(String num) {

        Calendar date = Calendar.getInstance();
        System.out.println("Current Date and TIme : " + date.getTime());
        long timeInSecs = date.getTimeInMillis();
        Intent intent = new Intent(NotificationService.this,AlarmReceiver.class);
        intent.putExtra("value", "$"+num);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(NotificationService.this,123,intent,0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,timeInSecs/* + (1 * 60 * 1000)*/,pendingIntent);
            System.out.println("?????????????????>>>>>>>>>>>>>>>>>>>>>>>>");
        }
    }


    public void showNotification(Context context, String title, String message, Intent intent, int reqCode) {
        //SharedPreferenceManager sharedPreferenceManager = SharedPreferenceManager.getInstance(context);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, reqCode, intent, PendingIntent.FLAG_ONE_SHOT);
        String CHANNEL_ID = "channel_name";// The id of the channel.
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Channel Name";// The user-visible name of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationManager.createNotificationChannel(mChannel);
        }
        notificationManager.notify(reqCode, notificationBuilder.build()); // 0 is the request code, it should be unique id

        Log.d("showNotification", "showNotification: " + reqCode);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("Service Destroyed");

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        //here you will get call when app close.
        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());

        PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 1000,
                restartServicePendingIntent);

        super.onTaskRemoved(rootIntent);
    }

}