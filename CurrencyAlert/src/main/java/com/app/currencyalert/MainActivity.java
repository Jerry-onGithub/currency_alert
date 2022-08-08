package com.app.currencyalert;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.app.currencyalert.interfaces.CustomCallback;
import com.app.currencyalert.jsonResponse.GetData;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private TextView btc, date;
    private ImageView reload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        setContentView(R.layout.activity_main);
        btc=findViewById(R.id.bitcoin_val);
        date=findViewById(R.id.dateTextView);
        reload=findViewById(R.id.reload);

        getData();
        //executeTask();

        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
            }
        });
    }

    private void executeTask() {
        Handler handler = new Handler();
        // Define the code block to be executed
        Runnable runnableCode = new Runnable() {
            @Override
            public void run() {
                // Do something here on the main thread
                Log.d("Handlers", "Called on main thread");
                               
                GetData rs = new GetData();
                rs.getResponse(new CustomCallback(){
                    @Override
                    public void onSuccess(Btc bitcoin){
                        String num=String.format("%,d", Math.round(Float.valueOf(bitcoin.getPrice())));                 

                        int val = Integer.valueOf(Math.round(Float.valueOf(bitcoin.getPrice())));
                        if(val < 26500){
                            //reminderNotification(num);
                          
                            Intent intent = new Intent(MainActivity.this,NotificationService.class);
                            intent.putExtra("value", "$"+num);
                            startService(intent);
                        }
                    }
                    @Override
                    public void onFailure(String val) {

                    }
                });
                handler.postDelayed(this, 600000);
            }
        };
        // Start the initial runnable task by posting through the handler
        handler.post(runnableCode);
    }

    private void getData() {
        GetData rs = new GetData();
        rs.getResponse(new CustomCallback(){
            @Override
            public void onSuccess(Btc bitcoin){                
                String num=String.format("%,d", Math.round(Float.valueOf(bitcoin.getPrice())));

                date.setText(bitcoin.getTime());
                btc.setText("$"+ num);
            }
            @Override
            public void onFailure(String val) {

            }
        });
    }

    private void reminderNotification(String num) {

        Calendar date = Calendar.getInstance();
        System.out.println("Current Date and TIme : " + date.getTime());
        long timeInSecs = date.getTimeInMillis();
        Intent intent = new Intent(MainActivity.this,AlarmReceiver.class);
        intent.putExtra("value", "$"+num);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this,123,intent,0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,timeInSecs/* + (1 * 60 * 1000)*/, pendingIntent);     //modify this according to your time interval you'd need it to check     
        }
    }

    @Override
    protected void onStop() {
        super.onStop() ;
        //executeTask();
        startService(new Intent(this, NotificationService. class )) ;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        startService(new Intent(this, NotificationService. class )) ;
    }
}
