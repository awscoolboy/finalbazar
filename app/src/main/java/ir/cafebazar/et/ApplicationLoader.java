package ir.cafebazar.et;

import android.annotation.TargetApi;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;

import com.google.android.gms.ads.MobileAds;


public class ApplicationLoader extends Application {

    public static ApplicationLoader applicationContext;
    public static volatile Handler applicationHandler;

    public static final String  APP_ID="ca-app-pub-9793090354879392~4364601941";
    public static final String  INTERSTITIAL_ID="ca-app-pub-9793090354879392/2859948588";


    public static final String NEW_APP_NOTIFICATION_CHANNEL = "ir.cafebazar.new.NEW_APP_NOTIFICATION_CHANNEL";


    public static int counter;

    @Override
    public void onCreate() {
        super.onCreate();

        applicationContext=this;
        AndroidHelper.setDisplaySize(applicationContext);
        applicationHandler = new Handler(applicationContext.getMainLooper());
        MobileAds.initialize(this, APP_ID);
        initChannels();
        counter=0;

    }



    @TargetApi(26)
    private void initChannels() {
        if(Build.VERSION.SDK_INT < 26) {
            return;
        }
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        CharSequence channelName = "Bazaar Notifications";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel notificationChannel = new NotificationChannel(NEW_APP_NOTIFICATION_CHANNEL, channelName, importance);
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.RED);
        notificationChannel.enableVibration(false);
        notificationManager.createNotificationChannel(notificationChannel);
    }


    public static  String getStr(int res){
        return applicationContext.getResources().getString(res);
    }



}
