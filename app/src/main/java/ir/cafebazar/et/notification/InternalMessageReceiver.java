package ir.cafebazar.et.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class InternalMessageReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context ctx, Intent intent) {

        String msg = intent.getStringExtra("link");
        Log.i("berhan",msg);
        Toast.makeText(ctx,msg,Toast.LENGTH_SHORT).show();
    }
}