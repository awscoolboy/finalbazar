package ir.cafebazar.et.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import apps.cafebazaar.all.apps.R;

import ir.cafebazar.et.AndroidHelper;
import ir.cafebazar.et.ApplicationLoader;
import ir.cafebazar.et.Models.BazaarApps;
import ir.cafebazar.et.Models.Categories;
import ir.cafebazar.et.Models.section.SectionDataModel;
import ir.cafebazar.et.Models.SubCategories;
import ir.cafebazar.et.network.BaseApiController;


public class CategoryActivity extends AppCompatActivity {


    private Categories category;

    private RecyclerView listView;
    private List<SectionDataModel> allApps;
    private RecyclerViewDataAdapter adapter;

    private ProgressBar progressBar;
    private LinearLayout error_view;
    private int endSize;


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
        setContentView(R.layout.catagroy_actiity_layotu);


        ((AdView)findViewById(R.id.adView)).loadAd(new AdRequest.Builder().build());

        loadAdd();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        category=(Categories) getIntent().getSerializableExtra("cat");
        if(category==null){
            finish();
            return;
        }

        getSupportActionBar().setTitle(AndroidHelper.foramtString(category.getName()));
        allApps=new ArrayList<>();
        listView=findViewById(R.id.listView);
        listView.setHasFixedSize(true);
        adapter = new RecyclerViewDataAdapter( this);
        listView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        listView.setAdapter(adapter);

        progressBar=findViewById(R.id.progressBar);
        error_view=findViewById(R.id.errorView);
        Button retry = findViewById(R.id.retryButton);
        retry.setOnClickListener(view -> {
            animateProgressWithError();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getData();
                }
            },3000);
        });

        getData();

    }


    private void getData(){
        BaseApiController.getInstance().getSubCategoriesForMainCategory(category.getId(),new BaseApiController.ApiCallBack() {
            @Override
            public void didReceiveData( int type ,Object... object) {
                if(object==null){
                    displayErrorMessage(getString(R.string.unknow_error));
                    return;
                }


                if(type== BaseApiController.didReceivedSubCategories){


                    List<SubCategories> categories=(List<SubCategories>)object[0];
                    endSize=categories.size();
                    for(SubCategories category:categories){
                        getAppsForSubCatagories(category);
                    }
                }
            }

            @Override
            public void onError(String error_message) {

                if(AndroidHelper.isNetworkOnline()){
                    displayErrorMessage(getString(R.string.unknow_error));
                }else{
                    displayErrorMessage(null);
                }
            }
        });

    }


    private void getAppsForSubCatagories(final SubCategories subCategories){


        BaseApiController.getInstance().getAppsForSubCategories(subCategories.getId(),subCategories.getName(), new BaseApiController.ApiCallBack() {
            @Override
            public void didReceiveData( int type ,Object... object) {

                if(object==null){
                    displayErrorMessage(getString(R.string.unknow_error));
                    return;
                }

                if(type== BaseApiController.didReceivedAppsForSubCategories){

                    List<BazaarApps.CafeBazaarApp> bazaarApps=(List<BazaarApps.CafeBazaarApp>)
                            object[0];


                        SectionDataModel dataModel=new SectionDataModel();
                        dataModel.setHeaderTitle(subCategories.getName());
                        dataModel.setCafeBazarApps(bazaarApps);
                        adapter.addToDataList(dataModel);
                    Log.i("category","size o fthe apps"  +bazaarApps.size() + " is the size");

                    if(listView.getVisibility()!=View.VISIBLE){
                            hideProgress();
                        }
                    }
            }

            @Override
            public void onError(String error_message) {
                if(AndroidHelper.isNetworkOnline()){
                    displayErrorMessage(getString(R.string.unknow_error));
                }else{
                    displayErrorMessage(null);
                }
            }
        });
    }



    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {

            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public class SectionListDataAdapter extends RecyclerView.Adapter<SectionListDataAdapter.SingleItemRowHolder>{

        private List<BazaarApps.CafeBazaarApp> itemModels;
        private Context mContext;

        public SectionListDataAdapter(List<BazaarApps.CafeBazaarApp> itemModels, Context mContext) {
            this.itemModels = itemModels;
            this.mContext = mContext;
        }

        @Override
        public SingleItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_item_cell, null);
            return new SingleItemRowHolder(v);
        }

        @Override
        public void onBindViewHolder(final SingleItemRowHolder holder, final int position) {
            BazaarApps.CafeBazaarApp itemModel = itemModels.get(position);
            holder.app_name.setText(AndroidHelper.foramtString(itemModel.getName()));
            holder.app_size.setText(AndroidHelper.foramtString(itemModel.getSize()));
            Glide.with(CategoryActivity.this)
                    .load(BaseApiController.base_url + "media/"+ itemModel.getIcon())
                    .into(holder.app_icon);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    Intent intent=new Intent(CategoryActivity.this, AppDetailActivity.class);
                    intent.putExtra("cafe_app",itemModels.get(position));

                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(CategoryActivity.this, (View)holder.app_icon
                                    , "profile");
                    startActivity(intent, options.toBundle());

                    showAdd();
                }
            });

        }

        @Override
        public int getItemCount() {
            return (null != itemModels ? itemModels.size() : 0);
        }

        public class SingleItemRowHolder extends RecyclerView.ViewHolder {

            private ImageView app_icon;
            private TextView app_name;
            private TextView app_size;

            public SingleItemRowHolder(View itemView) {
                super(itemView);
                app_icon=itemView.findViewById(R.id.app_logo);
                app_name=itemView.findViewById(R.id.app_title);
                app_size=itemView.findViewById(R.id.app_size);
            }
        }
    }

    public class RecyclerViewDataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        private class MainViewHolder extends RecyclerView.ViewHolder {
            TextView itemTitle;
            RecyclerView recyclerView;
            TextView btnMore;
            MainViewHolder(View itemView) {
                super(itemView);
                this.itemTitle = itemView.findViewById(R.id.headerTitile);
                this.recyclerView = itemView.findViewById(R.id.listView);
                this.btnMore = itemView.findViewById(R.id.moreTextView);
            }
        }

        private class ProgressViewHolder extends RecyclerView.ViewHolder {
            ProgressBar progressBar;
            ProgressViewHolder(View itemView) {
                super(itemView);
                progressBar=itemView.findViewById(R.id.progressBar);
            }
        }

        private Context mContext;
        private RecyclerView.RecycledViewPool recycledViewPool;

        private final int main_view_type=1;
        private final int progress_view_type=2;


        @Override
        public int getItemViewType(int position) {
            if(allApps.get(position)==null){
                return progress_view_type;
            }
            return  main_view_type;

        }

        public RecyclerViewDataAdapter(Context context){
            this.mContext = mContext;
            recycledViewPool = new RecyclerView.RecycledViewPool();
        }


        public void addToDataList(SectionDataModel dataModel){
            if(allApps.isEmpty()){
                allApps.add(dataModel);
                notifyDataSetChanged();
            }else{
                allApps.remove(allApps.size()-1);
                allApps.add(dataModel);
                if(!(getItemCount()==endSize-1)){
                    allApps.add(null);
                    notifyItemInserted(allApps.size()-1);
                }

            }
        }



        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if(viewType==main_view_type){
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_horizontal_layout, parent,false);
                return new MainViewHolder(v);
            }else {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_progress, parent,false);
                return new ProgressViewHolder(v);
            }
        }



        @Override
        public void onBindViewHolder( RecyclerView.ViewHolder viewHolder, int position) {
            if(getItemViewType(position)==main_view_type){
                MainViewHolder holder=(MainViewHolder)viewHolder;
                final String sectionName = allApps.get(position).getHeaderTitle();
                final List singleSectionItems = allApps.get(position).getCafeBazarApps();
                holder.itemTitle.setText(AndroidHelper.decodebase64(AndroidHelper.foramtString(sectionName)));
                SectionListDataAdapter adapter = new SectionListDataAdapter(singleSectionItems, mContext);
                holder.recyclerView.setHasFixedSize(true);
                holder.recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                holder.recyclerView.setAdapter(adapter);
                holder.recyclerView.setRecycledViewPool(recycledViewPool);

                if(singleSectionItems.size()<=3){
                    holder.btnMore.setVisibility(View.INVISIBLE);
                }else{
                    holder.btnMore.setVisibility(View.VISIBLE);
                }

                holder.btnMore.setOnClickListener(view -> {
                    Intent intent=new Intent(CategoryActivity.this, MoreAppsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("apps",(Serializable)singleSectionItems);
                    bundle.putBoolean("from_home",true);
                    bundle.putString("header",AndroidHelper.decodebase64(AndroidHelper.foramtString(sectionName)));
                    intent.putExtra("bundle",bundle);
                    startActivity(intent);

                    showAdd();
                });
            }

        }



        @Override
        public int getItemCount() {
            return (null != allApps ? allApps.size() : 0);
        }

    }

    private void hideProgress(){
        if(progressBar!=null && listView!=null){
            error_view.setVisibility(View.INVISIBLE);
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

    private void animateProgressWithError(){
        int mShortAnimationDuration=getResources().getInteger(
                android.R.integer.config_shortAnimTime);
        progressBar.setVisibility(View.VISIBLE);
        error_view.setVisibility(View.INVISIBLE);

        error_view.setAlpha(0f);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.animate()
                .alpha(1f)
                .setDuration(mShortAnimationDuration)
                .setListener(null);
        error_view.animate()
                .alpha(0f)
                .setDuration(mShortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        error_view.setVisibility(View.GONE);
                    }
                });
    }

    private void displayErrorMessage(String errorMessage){

        if(progressBar!=null && error_view!=null){

            if(errorMessage!=null){
                ((TextView)error_view.findViewById(R.id.errorText)).setText(errorMessage);
            }


            int mShortAnimationDuration=getResources().getInteger(
                    android.R.integer.config_shortAnimTime);
            error_view.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            error_view.setAlpha(0f);
            error_view.setVisibility(View.VISIBLE);
            error_view.animate()
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
