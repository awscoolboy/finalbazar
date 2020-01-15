package ir.cafebazar.et.ui.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import apps.cafebazaar.all.apps.R;
import ir.cafebazar.et.ApplicationLoader;

public class MainTwoActivity extends AppCompatActivity {

    private NavController navController;
    private InterstitialAd mInterstitialAd;

    public void showAdd(){
        ApplicationLoader.counter++;
        if(ApplicationLoader.counter%4!=0){
            return;
        }
        if(mInterstitialAd == null || !mInterstitialAd.isLoaded()) {
            return;
        }
        mInterstitialAd.show();
    }

    public void loadAdd(){
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(ApplicationLoader.INTERSTITIAL_ID);
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }});
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_two);
        BottomNavigationView navView = findViewById(R.id.bottomNav);
        NavHostFragment host = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.baseFragment);
        if (host != null) {
            navController= host.getNavController();
        }

        NavigationUI.setupWithNavController(navView, navController);
        //loadAdd();
    }


}
