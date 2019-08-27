package ir.cafebazar.et.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
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
import ir.cafebazar.et.network.BaseApiController;
import ir.cafebazar.et.ui.cells.GridSpacingItemDecoration;


public class MoreAppsActivity extends AppCompatActivity implements  BaseApiController.ApiCallBack{

    private RecyclerView listView;
    private ProgressBar progressBar;
    private GridAppAdapter adapter;
    private GridLayoutManager gridLayoutManager;
    private List<BazaarApps.CafeBazaarApp> apps;


    private boolean isFromHome;
    private int endSize;
    private boolean isLoading;


    private String nextLink;



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




    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }



    private int getSpanCount(){
        if(isTablet(this)){
            return 5;
        }else{
           return 3;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more_app_layou);
        loadAdd();

        ((AdView)findViewById(R.id.adView)).loadAd(new AdRequest.Builder().build());


        Toolbar toolbar = findViewById(R.id.toolbar);




        toolbar.inflateMenu(R.menu.menu_home);
        toolbar.setOnMenuItemClickListener(menuItem -> {
            Intent intent = new Intent(MoreAppsActivity.this,SearchActivity.class);
            startActivity(intent);
            return true;
        });

        toolbar.setNavigationOnClickListener(v -> {
            MoreAppsActivity.this.onBackPressed();
            finish();
        });


        Bundle bundle= getIntent().getBundleExtra("bundle");
        isFromHome=bundle.getBoolean("from_home",false);
        String title=bundle.getString("header");
        nextLink = bundle.getString("nextLink");




        toolbar.setTitle(title);

        listView=findViewById(R.id.listView);
        listView.setHasFixedSize(true);
        progressBar=findViewById(R.id.progressBar);


        adapter = new GridAppAdapter( this);
        listView.setLayoutManager(gridLayoutManager=new GridLayoutManager(this, getSpanCount()));
        listView.addItemDecoration(new GridSpacingItemDecoration(getSpanCount(),50,false));


        listView.setAdapter(adapter);
        if(isFromHome){
            apps=(List<BazaarApps.CafeBazaarApp>)bundle.getSerializable("apps");
            adapter.notifyDataSetChanged();
            new Handler().postDelayed(() -> hideProgress(),1000);
        }else{
            //get already loaded apps
            apps=(List<BazaarApps.CafeBazaarApp>)bundle.getSerializable("apps");
            adapter.notifyDataSetChanged();
            new Handler().postDelayed(() -> hideProgress(),300);

        }

        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(this,  R.anim.layout_animation_from_bottom);
        listView.setLayoutAnimation(controller);
        listView.scheduleLayoutAnimation();


        listView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView,int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(nextLink==null){
                   return;
                }

               int visibleItemCount = gridLayoutManager.getChildCount();
               int totalItemCount = gridLayoutManager.getItemCount();
               int  pastVisiblesItems = gridLayoutManager.findFirstVisibleItemPosition();
                if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                    apps.add(null);
                    adapter.notifyItemInserted(apps.size()-1);
                    BaseApiController.getInstance().getMoreAppsForCategory(nextLink,MoreAppsActivity.this);
                    nextLink = null;

                }
            }
        });

        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch(adapter.getItemViewType(position)){
                    case GridAppAdapter.main_view_type:
                        return 1;
                    case GridAppAdapter.progress_view_type:
                        return getSpanCount();
                    default:
                        return -1;
                }
            }
        });


    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {
            Intent intent=new Intent(this, SearchActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void didReceiveData( int type ,Object... object) {

        if(object==null){
            return;
        }

        if(type== BaseApiController.didReceivedMoreApps){

            new Thread(() -> {
                List<BazaarApps.CafeBazaarApp> bazaarApps=(List<BazaarApps.CafeBazaarApp>)object[0];
                if(bazaarApps.size()!=0){
                    if(apps.get(apps.size()-1)==null){
                        apps.remove(apps.size()-1);
                        AndroidHelper.runOnUIThread(() -> adapter.notifyItemRemoved(apps.size()-1));
                    }
                    int start= apps.size()-1;
                    apps.addAll(bazaarApps);

                    AndroidHelper.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyItemRangeChanged(start,apps.size()-1);
                           // listView.scrollToPosition(apps.size());

                        }
                    });

                    if(object[1] != null){
                        nextLink = (String)object[1];

                    }else{
                        nextLink = null;
                    }
                }

            }).start();
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

        public static final int main_view_type=1;
        public static final int progress_view_type=2;


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



        public void addToDataList(BazaarApps.CafeBazaarApp dataModel){
            if(apps.isEmpty()){
                apps.add(dataModel);
                notifyDataSetChanged();
            }else{
                apps.remove(apps.size()-1);
                apps.add(dataModel);
                if(!(getItemCount()==endSize-1)){
                    apps.add(null);
                    notifyItemInserted(apps.size()-1);
                }

            }

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
                BazaarApps.CafeBazaarApp itemModel = apps.get(position);
                holder.app_name.setText(AndroidHelper.foramtString(itemModel.getName()));
                holder.app_size.setText(AndroidHelper.foramtString(itemModel.getSize()));
                Glide.with(MoreAppsActivity.this).load(BaseApiController.base_url + "media/"+ itemModel.getIcon()).into(holder.app_icon);

                holder.itemView.setOnClickListener(view -> {

                    Intent intent=new Intent(MoreAppsActivity.this, AppDetailActivity.class);
                    intent.putExtra("cafe_app",apps.get(viewHolder.getAdapterPosition()));
                    intent.putExtra("from_home",true);
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(MoreAppsActivity.this, (View)holder.app_icon, "profile");

                    startActivity(intent, options.toBundle());

                    showAdd();

                    showAdd();


                });
            }

        }



        @Override
        public int getItemCount() {
            return (null != apps ? apps.size() : 0);
        }

    }

    private void showProgress(){
        if(progressBar!=null && listView!=null){
            int mShortAnimationDuration=getResources().getInteger(
                    android.R.integer.config_shortAnimTime);
            progressBar.setVisibility(View.VISIBLE);
            listView.setVisibility(View.INVISIBLE);
            progressBar.setAlpha(0f);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.animate()
                    .alpha(1f)
                    .setDuration(mShortAnimationDuration)
                    .setListener(null);
            listView.animate()
                    .alpha(0f)
                    .setDuration(mShortAnimationDuration)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            listView.setVisibility(View.GONE);
                        }
                    });
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
