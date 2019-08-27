//package ir.cafebazar.et.ui.activity;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.design.widget.NavigationView;
//import androidx.core.view.GravityCompat;
//import androidx.core.widget.DrawerLayout;
//import android.support.v7.app.ActionBarDrawerToggle;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.navigation.NavController;
//import androidx.navigation.fragment.NavHostFragment;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.RequestOptions;
//import com.firebase.ui.auth.AuthUI;
//import com.google.android.gms.ads.AdListener;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;
//import com.google.android.gms.ads.InterstitialAd;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//
//import apps.cafebazaar.all.apps.R;
//import ir.cafebazar.et.AndroidHelper;
//import ir.cafebazar.et.ApplicationLoader;
//import ir.cafebazar.et.auth.SignUpActivity;
//
//
//public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
//
//    private NavController navController;
//
//    private AdView mAdView;
//
//    private InterstitialAd mInterstitialAd;
//    private Toolbar toolbar;
//
//    public static final int RC_SIGN_IN=1;
//    private FirebaseUser user;
//
//    public void showAdd(){
//        ApplicationLoader.counter++;
//        if(ApplicationLoader.counter%4!=0){
//            return;
//        }
//        if(mInterstitialAd == null || !mInterstitialAd.isLoaded()) {
//            return;
//        }
//        mInterstitialAd.show();
//    }
//
//
//    public void loadAdd(Context context){
//        mInterstitialAd = new InterstitialAd(context);
//        mInterstitialAd.setAdUnitId(ApplicationLoader.INTERSTITIAL_ID);
//        mInterstitialAd.loadAd(new AdRequest.Builder().build());
//        mInterstitialAd.setAdListener(new AdListener(){
//            @Override
//            public void onAdClosed() {
//                mInterstitialAd.loadAd(new AdRequest.Builder().build());
//            }
//
//            @Override
//            public void onAdFailedToLoad(int i) {
//                super.onAdFailedToLoad(i);
//                mInterstitialAd.loadAd(new AdRequest.Builder().build());
//            }
//          }
//
//        );
//    }
//
//
//    private void setPhoneAndEmail(NavigationView navigationView){
//        ImageView profilePhoto=navigationView.getHeaderView(0).findViewById(R.id.profile);
//        TextView name=navigationView.getHeaderView(0).findViewById(R.id.name);
//        TextView email=navigationView.getHeaderView(0).findViewById(R.id.email);
//        FrameLayout frameLayout=navigationView.findViewById(R.id.logout);
//
//
//        frameLayout.setOnClickListener(view -> AuthUI.getInstance()
//                .signOut(MainActivity.this)
//                .addOnCompleteListener(task -> {
//                    recreate();
//
//                }));
//        if(user.getDisplayName()!=null){
//            name.setText(user.getDisplayName());
//        }
//        if(user.getEmail()!=null){
//            email.setText(user.getEmail());
//        }
//
//        if(user.getPhotoUrl()!=null){
//            Glide.with(this).load(user.getPhotoUrl()).apply(RequestOptions.circleCropTransform()).into(profilePhoto);
//        }
//
//    }
//
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        user=FirebaseAuth.getInstance().getCurrentUser();
//        if(user==null){
//            startActivity(new Intent(this, SignUpActivity.class));
//            finish();
//            return;
//        }
//        setContentView(R.layout.activity_main);
//        loadAdd(this);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);
//
//        toolbar=findViewById(R.id.toolbar);
//
//
//
//        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//
//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        NavigationView navigationView = findViewById(R.id.nav_view);
//        setPhoneAndEmail(navigationView);
//
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
//                drawer,toolbar,
//                R.string.navigation_drawer_open,
//                R.string.navigation_drawer_close);
//        toggle.syncState();
//        drawer.addDrawerListener(toggle);
//
//
//        NavHostFragment host=(NavHostFragment)getSupportFragmentManager().findFragmentById(R.id.baseFragment);
//        navController=host.getNavController();
//        navigationView.setCheckedItem(R.id.nav_home);
//        navigationView.setNavigationItemSelectedListener(this);
//
//
//        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
//        });
//
//    }
//
//
//    @Override
//    public boolean onSupportNavigateUp() {
//       return navController.navigateUp();
//
//    }
//
//    private void updateToolBarTitile(String title){
//        getSupportActionBar().setTitle(title);
//    }
//
//
//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//
//
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.action_search) {
//            Intent intent=new Intent(this,
//                    SearchActivity.class);
//            startActivity(intent);
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//
//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//
//
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//        switch (id){
//            case R.id.nav_home:
//                updateToolBarTitile(getString(R.string.menu_home));
//                navController.navigate(R.id.nav_home);
//                break;
//            case R.id.nav_apps:
//                updateToolBarTitile(getString(R.string.menu_apps));
//                navController.navigate(R.id.nav_apps);
//
//                break;
//            case R.id.nav_games:
//                updateToolBarTitile(getString(R.string.menu_games));
//                navController.navigate(R.id.nav_games);
//                break;
//
//            case R.id.nav_tele_apps:
//                updateToolBarTitile(getString(R.string.menu_tele_apps));
//                navController.navigate(R.id.telegramApps);
//                break;
//            case R.id.nav_share:
//                AndroidHelper.share("https://play.google.com/store/apps/details?id=" + getPackageName() ,this);
//                break;
//            case R.id.nav_settings:
//                break;
//            case R.id.nav_google_play_top:
//                updateToolBarTitile(getString(R.string.menu_google_play_tops));
//                navController.navigate(R.id.nav_google_play_top);
//                break;
//            case R.id.nav_favorite:
//                updateToolBarTitile(getString(R.string.menu_fav));
//                navController.navigate(R.id.nav_favorite);
//                break;
//        }
//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//
//        return true;
//    }
//
//
//
//}
