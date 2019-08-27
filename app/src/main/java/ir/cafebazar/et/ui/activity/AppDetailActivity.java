package ir.cafebazar.et.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
import java.util.List;

import apps.cafebazaar.all.apps.R;
import ir.cafebazar.et.AndroidHelper;
import ir.cafebazar.et.ApplicationLoader;
import ir.cafebazar.et.Models.BazaarApps;
import ir.cafebazar.et.Models.Favorite;
import ir.cafebazar.et.database.DatabaseHandler;
import ir.cafebazar.et.Models.Developer;
import ir.cafebazar.et.network.BaseApiController;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class AppDetailActivity extends AppCompatActivity {



    //adds
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
    private ListAdapter listAdapter;
    private Toolbar toolbar;

    private BazaarApps.CafeBazaarApp cafeBazarApp;
    private Developer developer;
    private  boolean fromHome;

    private List<BazaarApps.CafeBazaarApp> fromDevloper=new ArrayList<>();
    private List<BazaarApps.CafeBazaarApp> categorys=new ArrayList<>();

    private  int rowCount;
    private  int profile;
    private  int install;
    private  int detials;
    private  int screenshot;
    private  int app_desc;


    private Favorite convrtToFav(BazaarApps.CafeBazaarApp cafeBazaarApp){
        Favorite favorite= new Favorite();
        favorite.setCateogry(cafeBazaarApp.getCateogry());
        favorite.setDescription(cafeBazaarApp.getDescription());
        favorite.setDeveloper(cafeBazaarApp.getDeveloper());
        favorite.setName(cafeBazaarApp.getName());
        favorite.setIcon(cafeBazaarApp.getIcon());
        favorite.setDeveloper_url(cafeBazaarApp.getDeveloper_url());
        favorite.setPackage_name(cafeBazaarApp.getPackage_name());
        favorite.setRating_total(cafeBazaarApp.getRating_total());
        favorite.setRating_total_count(cafeBazaarApp.getRating_total_count());
        favorite.setInstalls(cafeBazaarApp.getInstalls());
        return favorite;
    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_detial);


        loadAdd();
        ((AdView)findViewById(R.id.adView)).loadAd(new AdRequest.Builder().build());


        rowCount=0;
        profile=rowCount++;
        install=rowCount++;
        detials=rowCount++;
        screenshot=rowCount++;
        app_desc=rowCount++;


        toolbar=findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.app_viewer_menu);
        listView=findViewById(R.id.listView);
        cafeBazarApp=(BazaarApps.CafeBazaarApp) getIntent().getSerializableExtra("cafe_app");
        fromHome=getIntent().getBooleanExtra("from_home",false);
        if(cafeBazarApp==null || cafeBazarApp.getName()==null){
            finish();
            return;
        }
        toolbar.setOnMenuItemClickListener(menuItem -> {
            int id = menuItem.getItemId();
            if (id == R.id.action_search) {
                Intent intent=new Intent(AppDetailActivity.this, SearchActivity.class);
                startActivity(intent);
                return true;
            }else if(id==R.id.add_to_fav){
                if(cafeBazarApp!=null){
                    if(developer!=null){
                        cafeBazarApp.setDeveloper(developer.getName());
                    }
                    DatabaseHandler.getInstance().appDao().insertFavApp(convrtToFav(cafeBazarApp));
                    MenuItem manuVal=toolbar.getMenu().getItem(1);
                    if(manuVal.getItemId()==R.id.add_to_fav){
                        manuVal.setIcon(R.drawable.ic_favorite);
                    }

                }
            }else if(id==R.id.share_app){
                AndroidHelper.share("https://play.google.com/store/apps/details?id=" +cafeBazarApp.getPackage_name(),AppDetailActivity.this);
            }
            return true;
        });

        toolbar.setNavigationOnClickListener(view -> AppDetailActivity.super.onBackPressed());
        if(DatabaseHandler.getInstance().appDao().findFavoriteAppByPackageName(cafeBazarApp.getPackage_name())!=null){
            MenuItem manuVal=toolbar.getMenu().getItem(1);
            if(manuVal.getItemId()==R.id.add_to_fav){
                manuVal.setIcon(R.drawable.ic_favorite);
            }else{
                manuVal.setIcon(R.drawable.ic_fav_outline);

            }
        }
        listAdapter=new ListAdapter();
        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.setAdapter(listAdapter);
        if(fromHome){
            BaseApiController.getInstance().getAppbyPackageName(cafeBazarApp.getPackage_name(), new BaseApiController.ApiCallBack() {
                @Override
                public void didReceiveData( int type,Object[] object) {
                    if(object==null){
                        return;
                    }
                    if(type== BaseApiController.didReceiveAppByPackageName){
                        cafeBazarApp=(BazaarApps.CafeBazaarApp) object[0];
                        listAdapter.notifyItemChanged(detials);
                        listAdapter.notifyItemChanged(screenshot);
                        listAdapter.notifyItemChanged(app_desc);
                        getDeveloper();
                    }
                }
                @Override
                public void onError(String error_message) {

                }
            });
        }else{

            getDeveloper();
        }


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }



//    private void getAppsByDeveloper(){
//        if (developer != null && developer.getId() == null) {
//            return;
//        }
//
//        Log.i("berhan","dapp recived by devloepr");
//
//        BaseApiController.getInstance().getAppsByDeveloper(developer.getId(), new BaseApiController.ApiCallBack() {
//            @Override
//            public void didReceiveData(int type,Object... object) {
//                if(object==null){
//                    return;
//                }
//
//                if(type== BaseApiController.didReceiveDeveloperApps){
//
//                    Log.i("berhan","revied apps by devloper");
//
//
//                    fromDevloper=(List<BazaarApps.CafeBazaarApp>)object[0];
//                    listAdapter.notifyItemChanged(devloper_apps);
//                    getAppsByCategory();
//                    Log.i("berhan","revied apps by devloper works fine");
//
//
//                }
//            }
//            @Override
//            public void onError(String error_message) {
//
//            }
//        });
//
//    }

//    private void getAppsByCategory(){
//
//        BaseApiController.getInstance().getAppsForSubCategories(Integer.parseInt(cafeBazarApp.getSub_category()),cafeBazarApp.getName(), new BaseApiController.ApiCallBack() {
//            @Override
//            public void didReceiveData(int type,Object[] object) {
//                if(object==null){
//                    return;
//                }
//
//                if(type== BaseApiController.didReceivedAppsForSubCategories){
//                    categorys= ( List<BazaarApps.CafeBazaarApp>)object[0];
//                    listAdapter.notifyItemChanged(cat_apps);
//                }
//            }
//
//            @Override
//            public void onError(String error_message) {
//            }
//        });
//
//
//    }


    private void getDeveloper(){

        if(cafeBazarApp.getDeveloper()!=null){
            BaseApiController.getInstance().getDeveloperById(cafeBazarApp.getDeveloper(), new BaseApiController.ApiCallBack() {
                @Override
                public void didReceiveData(int type,Object[] object) {
                    if(object==null){
                        return;
                    }

                    developer=(Developer)object[0];
                    listAdapter.notifyItemChanged(profile);
                }
                @Override
                public void onError(String error_message) {

                }
            });

        }
    }

    private class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


        private class MoreFromDevHolder extends RecyclerView.ViewHolder{

            private TextView headerTititle;
            private RecyclerView moreDevRv;

            public MoreFromDevHolder(@NonNull View itemView) {
                super(itemView);
                headerTititle=itemView.findViewById(R.id.headerTitile);
                moreDevRv=itemView.findViewById(R.id.moreDevRv);
            }
        }


        private class AppDescHolder extends RecyclerView.ViewHolder{
            private TextView appDesc;
            public AppDescHolder(@NonNull View itemView) {
                super(itemView);
                appDesc=itemView.findViewById(R.id.app_desc);
            }
        }

        private class AppScreenShotHolder extends RecyclerView.ViewHolder{
            private RecyclerView screenShotRv;
            public AppScreenShotHolder(@NonNull View itemView) {
                super(itemView);
                screenShotRv=itemView.findViewById(R.id.app_screen_shot);
            }
        }


        private class AppStatics extends RecyclerView.ViewHolder{

            private TextView averateRate;
            private TextView rateCount;
            private TextView appSize;
            private TextView downlodCount;

            AppStatics(@NonNull View itemView) {
                super(itemView);
                averateRate=itemView.findViewById(R.id.app_average_rate);
                rateCount=itemView.findViewById(R.id.app_review_count);
                appSize=itemView.findViewById(R.id.app_size);
                downlodCount=itemView.findViewById(R.id.download_count);
            }
        }


        private class AppProfileViewHolder extends RecyclerView.ViewHolder{
            TextView name;
            TextView devloper;
            ImageView appLogo;
            AppProfileViewHolder(@NonNull View itemView) {
                super(itemView);
                name=itemView.findViewById(R.id.app_title);
                devloper=itemView.findViewById(R.id.app_dev);
                appLogo=itemView.findViewById(R.id.app_logo);
            }
        }

        private class AppInstallView extends RecyclerView.ViewHolder{

            Button google;
            Button cafe;
            Button chrome;
            public AppInstallView(@NonNull View itemView) {
                super(itemView);
                google=itemView.findViewById(R.id.open_in_play);
                cafe=itemView.findViewById(R.id.open_in_cffe_app);
                chrome=itemView.findViewById(R.id.open_in_cafe_web);

            }
        }


        @Override
        public int getItemViewType(int position) {
          return position;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

            View view;
            if(getItemViewType(i)==profile){
                view=LayoutInflater.from(AppDetailActivity.this).inflate(R.layout.row_profile_view,viewGroup,false);
                return new AppProfileViewHolder(view);
            }else if(getItemViewType(i)==install){
                view=LayoutInflater.from(AppDetailActivity.this).inflate(R.layout.row_install,viewGroup,false);
                return new AppInstallView(view);
            }else if(getItemViewType(i)==detials){
                view=LayoutInflater.from(AppDetailActivity.this).inflate(R.layout.row_app_statics,viewGroup,false);
                return new AppStatics(view);
            }else if(getItemViewType(i)==screenshot){
                view=LayoutInflater.from(AppDetailActivity.this).inflate(R.layout.row_screen_shot,viewGroup,false);
                return new AppScreenShotHolder(view);
            }else if(getItemViewType(i)==app_desc) {
                view = LayoutInflater.from(AppDetailActivity.this).inflate(R.layout.row_app_desc, viewGroup, false);
                return new AppDescHolder(view);
            }
//            }else if(getItemViewType(i)==devloper_apps || getItemViewType(i)==cat_apps){
//                view=LayoutInflater.from(AppDetailActivity.this).inflate(R.layout.row_more,viewGroup,false);
//                return new MoreFromDevHolder(view);
//            }

            return new AppProfileViewHolder(new View(viewGroup.getContext()));
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


            if(getItemViewType(i)==profile){
                AppProfileViewHolder appProfileViewHolder=(AppProfileViewHolder)viewHolder;
                appProfileViewHolder.name.setText(AndroidHelper.foramtString(cafeBazarApp.getName()));
                if(developer!=null)
                    appProfileViewHolder.devloper.setText(AndroidHelper.foramtString(developer.getName()));
                DrawableCrossFadeFactory factory =
                        new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();
                Glide.with(AppDetailActivity.this)
                        .load(BaseApiController.base_url + "media/"+ cafeBazarApp.getIcon())
                        .transition(withCrossFade(factory))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(appProfileViewHolder.appLogo);

                if(developer!=null){
                    appProfileViewHolder.devloper.setOnClickListener(view -> {
                        Intent intent=new Intent(AppDetailActivity.this, DeveloperAppsActivity.class);
                        intent.putExtra("developer",developer);
                        startActivity(intent);
                    });
                }
            }else if(getItemViewType(i)==install){


                AppInstallView appInstallView=(AppInstallView)viewHolder;
                appInstallView.chrome.setOnClickListener(view -> {
                    AndroidHelper.openAppInChrome(cafeBazarApp.getUrl(),AppDetailActivity.this);
                    showAdd();
                });

                appInstallView.google.setOnClickListener(view -> {
                    if(!AndroidHelper.openAppInGooglePlay(cafeBazarApp.getPackage_name(),AppDetailActivity.this)){
                        AndroidHelper.openAppInChrome(cafeBazarApp.getUrl(),AppDetailActivity.this);

                    }
                    showAdd();

                });

                appInstallView.cafe.setOnClickListener(view -> {


                   if(!AndroidHelper.openAppCafePlay(cafeBazarApp.getUrl(),AppDetailActivity.this)){
                       Toast.makeText(AppDetailActivity.this,"INSTALL OFFICAL CAFE BAZAR APP",Toast.LENGTH_SHORT).show();
                       AndroidHelper.openAppInChrome(cafeBazarApp.getUrl(),AppDetailActivity.this);
                   }
                    showAdd();
                });

            }else if(getItemViewType(i)==detials){
                AppStatics appStatics=(AppStatics) viewHolder;
                appStatics.appSize.setText(AndroidHelper.foramtString(cafeBazarApp.getSize()));
                appStatics.downlodCount.setText(AndroidHelper.foramtString(cafeBazarApp.getInstalls()));
                appStatics.rateCount.setText(AndroidHelper.foramtString(cafeBazarApp.getRating_total_count()));
                appStatics.averateRate.setText(AndroidHelper.foramtString(cafeBazarApp.getRating_total()));

            }else if(getItemViewType(i)==screenshot){
                AppScreenShotHolder holder=(AppScreenShotHolder)viewHolder;
                ScreenShotAdapter imageAdapter=new ScreenShotAdapter(AppDetailActivity.this);
                holder.screenShotRv.setHasFixedSize(true);
                holder.screenShotRv.setLayoutManager(new LinearLayoutManager(AppDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
                holder.screenShotRv.setAdapter(imageAdapter);
            }else if(getItemViewType(i)==app_desc) {
                AppDescHolder holder = (AppDescHolder) viewHolder;
                holder.appDesc.setText(AndroidHelper.foramtString(cafeBazarApp.getDescription()));


//            }else if(getItemViewType(i)==devloper_apps){
//                MoreFromDevHolder holder=(MoreFromDevHolder)viewHolder;
//                if(fromDevloper.size()<=1){
//                    holder.itemView.setVisibility(View.GONE);
//                }else{
//                    holder.itemView.setVisibility(View.VISIBLE);
//                    if(developer!=null){
//                        holder.headerTititle.setText(AndroidHelper.foramtString(developer.getName()));
//                    }else{
//                        holder.headerTititle.setText("برنامه های کاربردی از توسعه دهنده");
//
//                    }
//                    SectionListDataAdapter adapter = new SectionListDataAdapter(fromDevloper, AppDetailActivity.this);
//                    holder.moreDevRv.setHasFixedSize(true);
//                    holder.moreDevRv.setLayoutManager(new LinearLayoutManager(AppDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
//                    holder.moreDevRv.setAdapter(adapter);
//                    holder.moreDevRv.setRecycledViewPool(new RecyclerView.RecycledViewPool());
//
//                }

//            }else if(getItemViewType(i)==cat_apps){
//                Log.i("hello","cat apps is this");
//                MoreFromDevHolder holder=(MoreFromDevHolder)viewHolder;
//                if(categorys.isEmpty()){
//                    Log.i("hello","it is empety");
//                    holder.itemView.setVisibility(View.GONE);
//                }else{
//                    holder.itemView.setVisibility(View.VISIBLE);
//                    holder.headerTititle.setText("نرم افزار مشابه");
//                    SectionListDataAdapter adapter = new SectionListDataAdapter(categorys, AppDetailActivity.this);
//                    holder.moreDevRv.setHasFixedSize(true);
//                    holder.moreDevRv.setLayoutManager(new LinearLayoutManager(AppDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
//                    holder.moreDevRv.setAdapter(adapter);
//                    holder.moreDevRv.setRecycledViewPool(new RecyclerView.RecycledViewPool());
//                }
//            }
            }
        }

        @Override
        public int getItemCount() {
            return rowCount;
        }
    }


    private class ScreenShotAdapter extends RecyclerView.Adapter<AppDetailActivity.ScreenShotAdapter.ImageViewHOlder>{

        private Context con;
        DrawableCrossFadeFactory factory;
        private ScreenShotAdapter(Context context){
            this.con=context;
            factory=new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();
        }


        @NonNull
        @Override
        public AppDetailActivity.ScreenShotAdapter.ImageViewHOlder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

            View view= LayoutInflater.from(con).inflate(R.layout.screen_shot_item,viewGroup,false);
            return new AppDetailActivity.ScreenShotAdapter.ImageViewHOlder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AppDetailActivity.ScreenShotAdapter.ImageViewHOlder imageViewHOlder, int i) {
            Glide.with(con.getApplicationContext())
                    .load(BaseApiController.base_url + "media/" + cafeBazarApp.getScreenshots().get(i))
                    .transition(withCrossFade(factory))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageViewHOlder.imageView);
        }

        @Override
        public int getItemCount() {
            return cafeBazarApp.getScreenshots()!=null?cafeBazarApp.getScreenshots().size():0;
        }


        public class ImageViewHOlder extends RecyclerView.ViewHolder {
            private ImageView imageView;
            public ImageViewHOlder(@NonNull View itemView) {
                super(itemView);
                imageView=(ImageView)itemView;
            }
        }
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
            Glide.with(ApplicationLoader.applicationContext)
                    .load(BaseApiController.base_url + "media/"+ itemModel.getIcon())
                    .into(holder.app_icon);
            holder.itemView.setOnClickListener(view -> {

                Intent intent=new Intent(AppDetailActivity.this,AppDetailActivity.class);
                intent.putExtra("cafe_app",itemModels.get(position));
                intent.putExtra("from_home",true);


                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(AppDetailActivity.this, holder.app_icon
                                , "profile");
                startActivity(intent, options.toBundle());


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


}
