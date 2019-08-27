package ir.cafebazar.et.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.List;

import apps.cafebazaar.all.apps.R;
import ir.cafebazar.et.AndroidHelper;
import ir.cafebazar.et.ApplicationLoader;
import ir.cafebazar.et.Models.BazaarApps;
import ir.cafebazar.et.Models.collections.CollApp;
import ir.cafebazar.et.network.BaseApiController;
import ir.cafebazar.et.ui.cells.GridSpacingItemDecoration;


public class ColListActivity extends AppCompatActivity implements BaseApiController.ApiCallBack{


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



    private RecyclerView listView;
    private ProgressBar progressBar;
    private GridAppAdapter adapter;
    private List<CollApp> apps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more_app_layou);

        loadAdd();
        ((AdView)findViewById(R.id.adView)).loadAd(new AdRequest.Builder().build());


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_home);
        toolbar.setOnMenuItemClickListener(item -> {
            if(item.getItemId()==R.id.action_search){
                Intent intent = new Intent(ColListActivity.this,SearchActivity.class);
                startActivity(intent);
            }
            return false;
        });

        toolbar.setNavigationOnClickListener(v -> ColListActivity.this.onBackPressed());

        String title=getIntent().getStringExtra("header");
        String subCollId=getIntent().getStringExtra("sub_coll_id");
        toolbar.setTitle(AndroidHelper.foramtString(title));
        listView=findViewById(R.id.listView);
        listView.setHasFixedSize(true);
        progressBar=findViewById(R.id.progressBar);
        adapter = new GridAppAdapter( this);
        if(AndroidHelper.isTablet(this)){
            listView.setLayoutManager(new GridLayoutManager(this, 4));
            listView.addItemDecoration(new GridSpacingItemDecoration(5,50,false));
        }else{
            listView.setLayoutManager(new GridLayoutManager(this, 3));
            listView.addItemDecoration(new GridSpacingItemDecoration(3,50,false));
        }
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(this,  R.anim.layout_animation_from_bottom);
        listView.setLayoutAnimation(controller);
        listView.scheduleLayoutAnimation();
        listView.setAdapter(adapter);


        BaseApiController.getInstance().getAppsForSubCollId(subCollId,this);
    }



    @Override
    public void didReceiveData(int type, Object... object) {
        if(object==null){
            return;
        }

        if(type==BaseApiController.didReceiveAppsForSubCollations){
             apps=(List<CollApp>)object[0];
             adapter.notifyDataSetChanged();
             if(listView.getVisibility()!=View.VISIBLE){
                 hideProgress();
             }
        }
    }

    @Override
    public void onError(String error_message) {

    }


    public class GridAppAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        private class MainViewHolder extends RecyclerView.ViewHolder {
            private ImageView app_icon;
            private TextView app_name;
            private TextView app_size;

            public MainViewHolder(View itemView) {
                super(itemView);
                app_icon=itemView.findViewById(R.id.app_logo);
                app_name=itemView.findViewById(R.id.app_title);
                app_size=itemView.findViewById(R.id.app_size);

            }
        }

        private class ProgressViewHolder extends RecyclerView.ViewHolder {
            ProgressBar progressBar;
            ProgressViewHolder(@NonNull View itemView) {
                super(itemView);
                progressBar=itemView.findViewById(R.id.progressBar);
            }
        }


        private Context mContext;

        private final int main_view_type=1;
        private final int progress_view_type=2;


        @Override
        public int getItemViewType(int position) {
            if(apps.get(position)==null){
                return progress_view_type;
            }
            return  main_view_type;

        }

        public GridAppAdapter(Context context){
            this.mContext = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if(viewType==main_view_type){
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_item_cell, parent,false);
                return new MainViewHolder(v);
            }else {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_progress, parent,false);
                return new ProgressViewHolder(v);
            }
        }



        @Override
        public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int position) {
            if(getItemViewType(position)==main_view_type) {

                final MainViewHolder holder = (MainViewHolder) viewHolder;
                CollApp itemModel = apps.get(position);
                holder.app_name.setText(AndroidHelper.foramtString(itemModel.getName()));
                Glide.with(ColListActivity.this).load(BaseApiController.base_url + "media/"+ itemModel.getIcon()).into(holder.app_icon);

                holder.itemView.setOnClickListener(view -> {
                    Intent intent=new Intent(ColListActivity.this, AppDetailActivity.class);
                    BazaarApps.CafeBazaarApp bazarApp=new BazaarApps.CafeBazaarApp();
                    bazarApp.setName(itemModel.getName());
                    bazarApp.setIcon(itemModel.getIcon());
                    bazarApp.setPackage_name(itemModel.getPackage_name());
                    bazarApp.setRating_total(itemModel.getRating_total());
                    bazarApp.setRating_total_count(itemModel.getRating_total_count());
                    bazarApp.setUrl(itemModel.getUrl());
                    intent.putExtra("cafe_app",bazarApp);
                    intent.putExtra("from_home",true);
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(ColListActivity.this, (View)holder.app_icon, "profile");

                    startActivity(intent, options.toBundle());


                    showAdd();
                });
            }

        }


        @Override
        public int getItemCount() {
            return (null != apps ? apps.size() : 0);
        }

    }


    private void hideProgress(){
        if(progressBar!=null && listView!=null){
            int mShortAnimationDuration=getResources().getInteger(
                    android.R.integer.config_shortAnimTime);
            listView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            listView.setAlpha(0f);
            listView.setVisibility(View.VISIBLE);
            listView.animate()
                    .alpha(1f)
                    .setDuration(mShortAnimationDuration)
                    .setListener(null);
            progressBar.animate()
                    .alpha(0f)
                    .setDuration(mShortAnimationDuration)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            progressBar.setVisibility(View.GONE);
                        }
                    });
        }
    }








}
