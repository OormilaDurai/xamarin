package xamarin.testapp.com.xamarin;

import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    myDbHelper xdb;
    Spinner countryS;
    EditText msgI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //for color
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1FBB82")));;

        xdb = new myDbHelper(getApplicationContext());
        //xdb.deleteXamarinData();
        Spinner cntryVal = findViewById(R.id.cntryVal);

        ArrayList<String> cntryvalue = new ArrayList<String>();

        cntryvalue.add("FRANCE");
        cntryvalue.add("LONDON");
        cntryvalue.add("INDIA");

        final ArrayAdapter<String> cntryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cntryvalue);
        cntryVal.setAdapter(cntryAdapter);

        try {

            Cursor xamarinData = xdb.getXamarinData();
            msgI = (EditText)findViewById(R.id.editText);
            if (xamarinData != null && xamarinData.moveToFirst()) {
                do {
                    String country = xamarinData.getString(xamarinData.getColumnIndex("COUNTRY"));
                    String msg = xamarinData.getString(xamarinData.getColumnIndex("MSG"));
                    String val = xamarinData.getString(xamarinData.getColumnIndex("VALUE"));
                    Log.d("values", "country"+country + " msg" + msg + " val" + val);

                    if (country != null) {
                        int cntryPos = cntryAdapter.getPosition(country);
                        cntryVal.setSelection(cntryPos);
                    }
                    msgI.setText(msg);

                } while (xamarinData.moveToNext());
            }
            xamarinData.close();
        }
        catch (NullPointerException e){
            Log.d("values", "country");
        }

        Button yes = (Button)findViewById(R.id.yes);
        Button no = (Button)findViewById(R.id.no1);
        Button send = (Button)findViewById(R.id.send);
        xdb = new myDbHelper(this);
        countryS = (Spinner)findViewById(R.id.cntryVal);

        //setting values by clicking button
        yes.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String cntry = countryS.getSelectedItem().toString();
                String val = msgI.getText().toString();
                xdb.insertXamarinData(cntry,val,"yes");
            }
        });
        no.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String cntry = countryS.getSelectedItem().toString();
                String val = msgI.getText().toString();
                xdb.insertXamarinData(cntry,val,"no");
            }
        });
        send.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String cntry = countryS.getSelectedItem().toString();
                String val = msgI.getText().toString();
                xdb.insertXamarinData(cntry,val,"send");
            }
        });

        class MyTimerTask extends TimerTask {
            public void run() {
                generateNotification(getApplicationContext(), "");
            }
        }

        //setting time interval for showing notifications
        MyTimerTask myTask = new MyTimerTask();
        Timer myTimer = new Timer();

        myTimer.schedule(myTask, 30000, 30000);


    }
    //function to generate notification
    private void generateNotification(Context context, String message) {

        int icon = R.drawable.abc;
        long when = System.currentTimeMillis();
        String appname = context.getResources().getString(R.string.app_name);
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        Notification notification;

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, MainActivity.class), 0);


        // To support 2.3 os, we use "Notification" class and 3.0+ os will use
        // "NotificationCompat.Builder" class.
        if (currentapiVersion < android.os.Build.VERSION_CODES.HONEYCOMB) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(
                    context);
            //getting database values
            //setOngoing function is used so that user cannot dismiss the reminder without entering the input
            try {
                xdb = new myDbHelper(getApplicationContext());
                Cursor xamarinData = xdb.getXamarinData();
                if (xamarinData != null && xamarinData.moveToFirst()) {
                    do {
                        String country = xamarinData.getString(xamarinData.getColumnIndex("COUNTRY"));
                        String msg = xamarinData.getString(xamarinData.getColumnIndex("MSG"));
                        String val = xamarinData.getString(xamarinData.getColumnIndex("VALUE"));
                        Log.d("values", "country"+country + " msg" + msg + " val" + val);
                        notification = builder.setContentIntent(contentIntent)
                                .setSmallIcon(icon).setTicker(appname).setWhen(0)
                                .setOngoing(true).setContentTitle(val+" "+msg + " " + country).setAutoCancel(true)
                                .setContentText(message).build();

                        notificationManager.notify((int) when, notification);
                    } while (xamarinData.moveToNext());
                }
                xamarinData.close();
            }
            catch (NullPointerException e){
                Log.d("values", "country");
            }

        } else {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(
                    context);
            try {
                xdb = new myDbHelper(getApplicationContext());
                Cursor xamarinData = xdb.getXamarinData();
                if (xamarinData != null && xamarinData.moveToFirst()) {
                    do {
                        String country = xamarinData.getString(xamarinData.getColumnIndex("COUNTRY"));
                        String msg = xamarinData.getString(xamarinData.getColumnIndex("MSG"));
                        String val = xamarinData.getString(xamarinData.getColumnIndex("VALUE"));
                        Log.d("values", "country"+country + " msg" + msg + " val" + val);
                        notification = builder.setContentIntent(contentIntent)
                                .setSmallIcon(icon).setTicker(appname).setWhen(0)
                                .setOngoing(true).setContentTitle(val+" "+msg + " " + country).setAutoCancel(true)
                                .setContentText(message).build();

                        notificationManager.notify((int) when, notification);
                    } while (xamarinData.moveToNext());
                }
                xamarinData.close();
            }
            catch (NullPointerException e){
                Log.d("values", "country");
            }


        }
    }
}
