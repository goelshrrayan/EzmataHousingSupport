package ezmata.housing.project.visito;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private final String ADMIN_CHANNEL_ID ="admin_channel";
    String notifications[];
    Context context;
    NotificationManager mNotificationManager;
    String ns = Context.NOTIFICATION_SERVICE;
    Intent intent1;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        mNotificationManager = (NotificationManager) getSystemService(ns);
        Log.i("MY MESSAGE",remoteMessage.getData().get("title"));
        final Intent intent = new Intent(this, MainActivity.class);
         intent1=new Intent(this,MainActivity.class);
         context = getApplicationContext();
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationID = new Random().nextInt(3000);




      /*
        Apps targeting SDK 26 or above (Android O) must implement notification channels and add its notifications
        to at least one of them. Therefore, confirm if version is Oreo or higher, then setup notification channel
      */
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            setupChannels(notificationManager);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),
                R.drawable.icon);
        Log.i("Remote Message",remoteMessage.getData()+"");
        if(remoteMessage.getData()!=null) {
           notifications = remoteMessage.getData().get("title").split(",");
            SharedPreferences sharedPreferences = getSharedPreferences("Details", Context.MODE_PRIVATE);
            String s = sharedPreferences.getString("Flat", "");
            if (s.trim().equals(notifications[0].trim())) {

                Log.i("Intent","Worked");
                intent1.putExtra("Allowance",1);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent1.putExtra("Id", notifications[1].trim());
                intent1.putExtra("Parent","notification");
                startActivity(intent1);

                intent.putExtra("Allowance",1);
                intent.putExtra("Id", notifications[1].trim());
                intent.putExtra("Parent","notification");
                Log.i("id-mymessaging", notifications[1]);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                        PendingIntent.FLAG_CANCEL_CURRENT);
                Uri notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                        .setSmallIcon(R.mipmap.home_icon)
                        .setLargeIcon(largeIcon)
                        .setContentTitle(notifications[0])
                        .setContentText(remoteMessage.getData().get("message"))
                        .setAutoCancel(true)
                        .setSound(notificationSoundUri)
                        .setContentIntent(pendingIntent);

                //Set notification color to match your app color template
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    notificationBuilder.setColor(getResources().getColor(R.color.colorPrimaryDark));
                }
              //  notificationManager.notify(notificationID, notificationBuilder.build());

                intent1.putExtra("Allowance",1);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent1.putExtra("Id", notifications[1].trim());
                intent1.putExtra("Parent","notification");
                startActivity(intent1);


            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels(NotificationManager notificationManager){
        CharSequence adminChannelName = "New notification";
        String adminChannelDescription = "Device to devie notification";

        NotificationChannel adminChannel;
        adminChannel = new NotificationChannel(ADMIN_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_HIGH);
        adminChannel.setDescription(adminChannelDescription);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(adminChannel);
        }

//        int notifyID = 1;
//        String CHANNEL_ID = "my_channel_01";// The id of the channel.
//        CharSequence name = "Channel";// The user-visible name of the channel.
//        int importance = NotificationManager.IMPORTANCE_HIGH;
//        NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
//        Intent notificationIntent = new Intent(this, MainActivity.class);
//        notificationIntent.putExtra("Allowance",1);
//        notificationIntent.putExtra("Id", notifications[1].trim());
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
//        Notification notification =
//                new NotificationCompat.Builder(this)
//                        .setSmallIcon(R.drawable.icon)
//                        .setContentTitle(notifications[0])
//                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
//                                R.drawable.icon))
//                        .setContentIntent(contentIntent)
//                        .setChannelId(CHANNEL_ID).build();
//
//        mNotificationManager.createNotificationChannel(mChannel);
//        mNotificationManager.notify(notifyID , notification);
    }
}
