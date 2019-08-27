package ir.cafebazar.et.notification;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import apps.cafebazaar.all.apps.R;
import ir.cafebazar.et.ApplicationLoader;
import ir.cafebazar.et.ui.activity.NotiAppReciver;


public class MyFirebaseMessagingService extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getNotification() != null && remoteMessage.getData().size() > 0) {


            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            Map<String, String> payload = remoteMessage.getData();
            String url = "";
            String image = "";
            if(payload!=null){
               url =  payload.get("link");
               if(payload.containsKey("image")){
                   image = payload.get("image");
               }
            }



            Intent notifyIntent = new Intent(this, NotiAppReciver.class);
            notifyIntent.putExtra("link",url);
            notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent notifyPendingIntent = PendingIntent.getActivity(this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);


            if(image==null || image.isEmpty()){
                Drawable myDrawable = getResources().getDrawable(R.mipmap.ic_launcher);
                Bitmap anImage      = ((BitmapDrawable) myDrawable).getBitmap();
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, ApplicationLoader.NEW_APP_NOTIFICATION_CHANNEL);
                builder.setContentIntent(notifyPendingIntent);
                builder.setSmallIcon(R.drawable.ic_small_noti)
                        .setContentTitle(title)
                        .setLargeIcon(anImage)
                        .setContentText(body)
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                notificationManager.notify(0, builder.build());

                return;
            }



            Glide.with(ApplicationLoader.applicationContext)
                    .asBitmap()
                    .load(image.trim())
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                            NotificationCompat.Builder builder = new NotificationCompat.Builder(MyFirebaseMessagingService.this, ApplicationLoader.NEW_APP_NOTIFICATION_CHANNEL);
                                    builder.setContentIntent(notifyPendingIntent)
                                    .setSmallIcon(R.drawable.ic_small_noti)
                                    .setContentTitle(title)
                                    .setLargeIcon(resource)
                                    .setContentText(body)
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);


                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MyFirebaseMessagingService.this);
                            notificationManager.notify(0, builder.build());
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });





        }
    }


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
    }
}

