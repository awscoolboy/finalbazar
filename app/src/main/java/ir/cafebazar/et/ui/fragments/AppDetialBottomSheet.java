//package ir.cafebazar.et.ui.fragments;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//
//import androidx.core.app.ActivityOptionsCompat;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.engine.DiskCacheStrategy;
//import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
//import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
//
//import java.util.List;
//
//import apps.cafebazaar.all.apps.R;
//import ir.cafebazar.et.AndroidHelper;
//import ir.cafebazar.et.ApplicationLoader;
//import ir.cafebazar.et.Models.BazaarApps;
//import ir.cafebazar.et.Models.Developer;
//import ir.cafebazar.et.network.BaseApiController;
//import ir.cafebazar.et.ui.activity.AppDetailActivity;
//import ir.cafebazar.et.ui.activity.DeveloperAppsActivity;
//
//import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
//
//public class AppDetialBottomSheet extends BottomSheetDialogFragment {
//
//
//    private RecyclerView listView;
//    private ListAdapter listAdapter;
//
//    private BazaarApps.CafeBazaarApp cafeBazarApp;
//    private Developer developer;
//    private  boolean fromHome;
//
//    private  int rowCount;
//    private  int profile;
//    private  int install;
//    private  int detials;
//    private  int screenshot;
//
//
//
//public AppDetialBottomSheet(){
//
//}
//
//
//
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        if(getArguments()!=null){
//            cafeBazarApp=(BazaarApps.CafeBazaarApp) getArguments().getSerializable("cafe_app");
//
//        }
//        fromHome=getArguments().getBoolean("from_home",false);
//        if(cafeBazarApp==null || cafeBazarApp.getName()==null){
//            return;
//        }
//
//
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view=inflater.inflate(R.layout.activity_app_detial,container,false);
//
//
//        view.findViewById(R.id.toolbar).setVisibility(View.GONE);
//        rowCount=0;
//        profile=rowCount++;
//        install=rowCount++;
//        detials=rowCount++;
//        screenshot=rowCount++;
//
//
//        listView=view.findViewById(R.id.listView);
//
//        listAdapter=new ListAdapter();
//        listView.setLayoutManager(new LinearLayoutManager(getContext()));
//        listView.setAdapter(listAdapter);
//
//        if(fromHome){
//            BaseApiController.getInstance().getAppbyPackageName(cafeBazarApp.getPackage_name(), new BaseApiController.ApiCallBack() {
//                @Override
//                public void didReceiveData( int type,Object[] object) {
//                    if(object==null){
//                        return;
//                    }
//                    if(type== BaseApiController.didReceiveAppByPackageName){
//                        cafeBazarApp=(BazaarApps.CafeBazaarApp) object[0];
//                        listAdapter.notifyItemChanged(detials);
//                        listAdapter.notifyItemChanged(screenshot);
//                        getDevloper();
//                    }
//                }
//                @Override
//                public void onError(String error_message) {
//
//                }
//            });
//        }else{
//            getDevloper();
//        }
//
//        return view;
//    }
//
//
//
//
//    private void getDevloper(){
//    Log.i("berhan","devloper called");
//
//        if(cafeBazarApp.getDeveloper()!=null){
//
//
//            BaseApiController.getInstance().getDeveloperById(cafeBazarApp.getDeveloper(), new BaseApiController.ApiCallBack() {
//                @Override
//                public void didReceiveData(int type,Object[] object) {
//                    if(object==null){
//                        return;
//                    }
//                    Log.i("berhan","devloepr called and backed");
//
//                    developer=(Developer)object[0];
//                    listAdapter.notifyItemChanged(profile);
//                }
//                @Override
//                public void onError(String error_message) {
//
//                }
//            });
//
//        }
//    }
//
//    private class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
//
//
//        private class MoreFromDevHolder extends RecyclerView.ViewHolder{
//
//            private TextView headerTititle;
//            private RecyclerView moreDevRv;
//
//            public MoreFromDevHolder(@NonNull View itemView) {
//                super(itemView);
//                headerTititle=itemView.findViewById(R.id.headerTitile);
//                moreDevRv=itemView.findViewById(R.id.moreDevRv);
//            }
//        }
//
//
//        private class AppDescHolder extends RecyclerView.ViewHolder{
//            private TextView appDesc;
//            public AppDescHolder(@NonNull View itemView) {
//                super(itemView);
//                appDesc=itemView.findViewById(R.id.app_desc);
//            }
//        }
//
//        private class AppScreenShotHolder extends RecyclerView.ViewHolder{
//            private RecyclerView screenShotRv;
//            public AppScreenShotHolder(@NonNull View itemView) {
//                super(itemView);
//                screenShotRv=itemView.findViewById(R.id.app_screen_shot);
//            }
//        }
//
//
//        private class AppStatics extends RecyclerView.ViewHolder{
//
//            private TextView averateRate;
//            private TextView rateCount;
//            private TextView appSize;
//            private TextView downlodCount;
//
//            AppStatics(@NonNull View itemView) {
//                super(itemView);
//                averateRate=itemView.findViewById(R.id.app_average_rate);
//                rateCount=itemView.findViewById(R.id.app_review_count);
//                appSize=itemView.findViewById(R.id.app_size);
//                downlodCount=itemView.findViewById(R.id.download_count);
//            }
//        }
//
//
//        private class AppProfileViewHolder extends RecyclerView.ViewHolder{
//            TextView name;
//            TextView devloper;
//            ImageView appLogo;
//            AppProfileViewHolder(@NonNull View itemView) {
//                super(itemView);
//                name=itemView.findViewById(R.id.app_title);
//                devloper=itemView.findViewById(R.id.app_dev);
//                appLogo=itemView.findViewById(R.id.app_logo);
//            }
//        }
//
//        private class AppInstallView extends RecyclerView.ViewHolder{
//
//            Button google;
//            Button cafe;
//            Button chrome;
//            public AppInstallView(@NonNull View itemView) {
//                super(itemView);
//                google=itemView.findViewById(R.id.open_in_play);
//                cafe=itemView.findViewById(R.id.open_in_cffe_app);
//                chrome=itemView.findViewById(R.id.open_in_cafe_web);
//
//            }
//        }
//
//
//        @Override
//        public int getItemViewType(int position) {
//            return position;
//        }
//
//        @NonNull
//        @Override
//        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//
//            View view;
//            if(getItemViewType(i)==profile){
//                view=LayoutInflater.from(getActivity()).inflate(R.layout.row_profile_view,viewGroup,false);
//                return new AppProfileViewHolder(view);
//            }else if(getItemViewType(i)==install){
//                view=LayoutInflater.from(getActivity()).inflate(R.layout.row_install,viewGroup,false);
//                return new AppInstallView(view);
//            }else if(getItemViewType(i)==detials){
//                view=LayoutInflater.from(getActivity()).inflate(R.layout.row_app_statics,viewGroup,false);
//                return new AppStatics(view);
//            }else if(getItemViewType(i)==screenshot){
//                view=LayoutInflater.from(getActivity()).inflate(R.layout.row_screen_shot,viewGroup,false);
//                return new AppScreenShotHolder(view);
//            }
//            return new AppProfileViewHolder(new View(viewGroup.getContext()));
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
//
//
//            if(getItemViewType(i)==profile){
//                AppProfileViewHolder appProfileViewHolder=(AppProfileViewHolder)viewHolder;
//                appProfileViewHolder.name.setText(AndroidHelper.foramtString(cafeBazarApp.getName()));
//                if(developer!=null)
//                    appProfileViewHolder.devloper.setText(AndroidHelper.foramtString(developer.getName()));
//                DrawableCrossFadeFactory factory =
//                        new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();
//                Glide.with(getActivity())
//                        .load(BaseApiController.base_url + "media/"+ cafeBazarApp.getIcon())
//                        .transition(withCrossFade(factory))
//                        .diskCacheStrategy(DiskCacheStrategy.ALL)
//                        .into(appProfileViewHolder.appLogo);
//
//                if(developer!=null){
//                    appProfileViewHolder.devloper.setOnClickListener(view -> {
//                        Intent intent=new Intent(getActivity(), DeveloperAppsActivity.class);
//                        intent.putExtra("developer",developer);
//                        startActivity(intent);
//                    });
//                }
//            }else if(getItemViewType(i)==install){
//
//                AppInstallView appInstallView=(AppInstallView)viewHolder;
//                appInstallView.chrome.setOnClickListener(view -> {
//                    AndroidHelper.openAppInChrome(cafeBazarApp.getUrl(),getActivity());
//                });
//
//                appInstallView.google.setOnClickListener(view -> {
//                    if(!AndroidHelper.openAppInGooglePlay(cafeBazarApp.getPackage_name(),getActivity())){
//                        AndroidHelper.openAppInChrome(cafeBazarApp.getUrl(),getActivity());
//
//                    }
//                });
//
//                appInstallView.cafe.setOnClickListener(view -> {
//
//                    AndroidHelper.openAppInCafe(cafeBazarApp.getUrl(),getActivity());
//                });
//
//            }else if(getItemViewType(i)==detials){
//                AppStatics appStatics=(AppStatics) viewHolder;
//                appStatics.appSize.setText(AndroidHelper.foramtString(cafeBazarApp.getSize()));
//                appStatics.downlodCount.setText(AndroidHelper.foramtString(cafeBazarApp.getInstalls()));
//                appStatics.rateCount.setText(AndroidHelper.foramtString(cafeBazarApp.getRating_total_count()));
//                appStatics.averateRate.setText(AndroidHelper.foramtString(cafeBazarApp.getRating_total()));
//
//            }else if(getItemViewType(i)==screenshot){
//                AppScreenShotHolder holder=(AppScreenShotHolder)viewHolder;
//                ScreenShotAdapter imageAdapter=new ScreenShotAdapter(getActivity());
//                holder.screenShotRv.setHasFixedSize(true);
//                holder.screenShotRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
//                holder.screenShotRv.setAdapter(imageAdapter);
//            }
//
//        }
//
//        @Override
//        public int getItemCount() {
//            return rowCount;
//        }
//    }
//
//
//    private class ScreenShotAdapter extends RecyclerView.Adapter<ScreenShotAdapter.ImageViewHOlder>{
//
//        private Context con;
//        DrawableCrossFadeFactory factory;
//        private ScreenShotAdapter(Context context){
//            this.con=context;
//            factory=new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();
//        }
//
//
//        @NonNull
//        @Override
//        public ScreenShotAdapter.ImageViewHOlder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//
//            View view= LayoutInflater.from(con).inflate(R.layout.screen_shot_item,viewGroup,false);
//            return new ScreenShotAdapter.ImageViewHOlder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull ScreenShotAdapter.ImageViewHOlder imageViewHOlder, int i) {
//            Glide.with(con.getApplicationContext())
//                    .load(BaseApiController.base_url + "media/" + cafeBazarApp.getScreenshots().get(i))
//                    .transition(withCrossFade(factory))
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .into(imageViewHOlder.imageView);
//        }
//
//        @Override
//        public int getItemCount() {
//            return cafeBazarApp.getScreenshots()!=null?cafeBazarApp.getScreenshots().size():0;
//        }
//
//
//        public class ImageViewHOlder extends RecyclerView.ViewHolder {
//            private ImageView imageView;
//            public ImageViewHOlder(@NonNull View itemView) {
//                super(itemView);
//                imageView=(ImageView)itemView;
//            }
//        }
//    }
//
//
//
//    public class SectionListDataAdapter extends RecyclerView.Adapter<SectionListDataAdapter.SingleItemRowHolder>{
//
//        private List<BazaarApps.CafeBazaarApp> itemModels;
//        private Context mContext;
//
//        public SectionListDataAdapter(List<BazaarApps.CafeBazaarApp> itemModels, Context mContext) {
//            this.itemModels = itemModels;
//            this.mContext = mContext;
//        }
//
//        @Override
//        public SingleItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_item_cell, null);
//            return new SingleItemRowHolder(v);
//        }
//
//        @Override
//        public void onBindViewHolder(final SingleItemRowHolder holder, final int position) {
//            BazaarApps.CafeBazaarApp itemModel = itemModels.get(position);
//            holder.app_name.setText(AndroidHelper.foramtString(itemModel.getName()));
//            holder.app_size.setText(AndroidHelper.foramtString(itemModel.getSize()));
//            Glide.with(ApplicationLoader.applicationContext)
//                    .load(BaseApiController.base_url + "media/"+ itemModel.getIcon())
//                    .into(holder.app_icon);
//            holder.itemView.setOnClickListener(view -> {
//
//                Intent intent=new Intent(getActivity(), AppDetailActivity.class);
//                intent.putExtra("cafe_app",itemModels.get(position));
//                intent.putExtra("from_home",true);
//
//
//                ActivityOptionsCompat options = ActivityOptionsCompat.
//                        makeSceneTransitionAnimation(getActivity(), holder.app_icon
//                                , "profile");
//                startActivity(intent, options.toBundle());
//
//
//            });
//
//        }
//
//        @Override
//        public int getItemCount() {
//            return (null != itemModels ? itemModels.size() : 0);
//        }
//
//        public class SingleItemRowHolder extends RecyclerView.ViewHolder {
//
//            private ImageView app_icon;
//            private TextView app_name;
//            private TextView app_size;
//
//            public SingleItemRowHolder(View itemView) {
//                super(itemView);
//                app_icon=itemView.findViewById(R.id.app_logo);
//                app_name=itemView.findViewById(R.id.app_title);
//                app_size=itemView.findViewById(R.id.app_size);
//
//            }
//        }
//    }
//
//
//
//
//
//}
