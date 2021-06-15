package com.zypko.zypko4.fcm;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.zypko.zypko4.R;
import com.zypko.zypko4.activity.ui.RandomHotelActivity;
import com.zypko.zypko4.food_tracking.util.Delivery_Boy_BroadCast_Receiver;
import com.zypko.zypko4.globals.Database;
import com.zypko.zypko4.globals.PrefEditor;
import com.zypko.zypko4.json_Objects.Json_Shared_Preference;

import java.util.Calendar;
import java.util.Date;

import static com.zypko.zypko4.notification.Notification_Class.CHANNEL_1_ID;

public class FcmInstanceIdService extends FirebaseMessagingService {

    private NotificationManagerCompat notificationManager;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);


        String recent_token = FirebaseInstanceId.getInstance().getToken();

        Log.e("FCM_TOKEN",recent_token);

        new PrefEditor(this).writeData(getString(R.string.FCM_TOKEN),recent_token);

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        notificationManager = NotificationManagerCompat.from(this);

        String title        = remoteMessage.getNotification().getTitle();
        //String title        = remoteMessage.getData().get("title");
        String message      = remoteMessage.getNotification().getBody();
        //String message      = remoteMessage.getData().get("body");
        String order_table_id  = remoteMessage.getData().get("key_1");



        //Log.e("MESSAGE_","A"+title);
        //Log.e("MESSAGE_","B"+message);
        //Log.e("MESSAGE_","lala "+order_table_id);

        Database myDB = new Database(this);
        // Update SQL for food tracking

        String name = "";

        switch (title){

            case "Order Accepted":
                name = "ORDER_ACCEPTED";
                Log.e("SHOW_SQL__","assddf");
                break;
            case "Order Cocked":
                name = "COCKING";
                break;
            case "Order Packed":
                name = "PACKING";
                break;
            case "Order Picked-Up":
                name = "PICKED_UP";
                break;
            case "Order Delivered":
                //name = "DELIVERED";
                new PrefEditor(this).writeData(Json_Shared_Preference.DELIVERED,"y");
                //Log.d("Alarm_Manager","TRIGGERED");
                //Alarm_Manager();
                break;
//            case "Order Rejected":
//                name = "";
//                break;

        }

        //myDB.update_food_tracking_details(Integer.parseInt(order_table_id),name,"y");

        Log.d("Alarm_Manager","TRIGGERED OUT SIDE");
        // Notification
        notification_Builder(title,message,1);

    }


    private void notification_Builder(String title, String message , int id){

        //String sender = message_come_from;
        //int noification_id = id;
        //String details[] = myDB.give_name_and_take_semester_roll_no_and_dept(message_come_from);

        Intent intent = new Intent(this,RandomHotelActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);


        //Toast.makeText(this, ""+details[0]+""+details[1]+""+details[2], Toast.LENGTH_LONG).show();
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder  notification = new NotificationCompat.Builder(this,CHANNEL_1_ID);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            notification.setSmallIcon(R.drawable.ic_notification_icon);
            notification.setColor(Color.BLUE);

        } else {
            notification.setSmallIcon(R.drawable.ic_notification_icon);
        }

        notification
                .setContentTitle(""+title)
                .setContentText(""+message)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_LIGHTS|Notification.DEFAULT_VIBRATE)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .setChannelId(CHANNEL_1_ID);
                //.build();


        notificationManager.notify(1,notification.build());

    }


    private void Alarm_Manager(){

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this,Delivery_Boy_BroadCast_Receiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,1,intent,0);

        long time= System.currentTimeMillis();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,time,pendingIntent);
        }

    }
}
