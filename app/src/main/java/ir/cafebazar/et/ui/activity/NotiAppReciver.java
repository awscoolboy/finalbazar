package ir.cafebazar.et.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import ir.cafebazar.et.AndroidHelper;

public class NotiAppReciver extends Activity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();


        if(intent.getExtras()!=null && intent.getExtras().getString("link")!=null){
            AndroidHelper.openAppInGooglePlay(intent.getStringExtra("link"),this);
            finish();
        }
    }



}
