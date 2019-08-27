package ir.cafebazar.et.ui.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import apps.cafebazaar.all.apps.R;
import ir.cafebazar.et.AndroidHelper;
import ir.cafebazar.et.ApplicationLoader;
import ir.cafebazar.et.Models.BazaarApps;
import ir.cafebazar.et.Models.Recommended;
import ir.cafebazar.et.Models.SubCategories;
import ir.cafebazar.et.Models.collections.CollDataModel;
import ir.cafebazar.et.Models.collections.Subcollection;
import ir.cafebazar.et.Models.section.RecoSectionModel;
import ir.cafebazar.et.Models.section.SectionDataModel;
import ir.cafebazar.et.network.BaseApiController;
import ir.cafebazar.et.ui.activity.AppDetailActivity;
import ir.cafebazar.et.ui.activity.ColListActivity;
import ir.cafebazar.et.ui.activity.MainTwoActivity;
import ir.cafebazar.et.ui.activity.MoreAppsActivity;
import ir.cafebazar.et.util.NoAnimationItemAnimator;


public class GamesFragment extends Fragment implements BaseApiController.ApiCallBack{

    private ProgressBar progressBar;
    private RecyclerView listView;
    private LinearLayout errorView;
    private Button retryButton;
    private ImageView statusImageView;
    private TextView statusTextView;

    private int posation;
    private String cat_id;


    private List<Object> allApps;
    private RecyclerViewDataAdapter adapter;

    private int endSize;


    private void initView(View view){
        progressBar = view.findViewById(R.id.progressBar);
        listView    = view.findViewById(R.id.appListView);
        errorView   = view.findViewById(R.id.errorView);
        retryButton   = view.findViewById(R.id.retryButton);
        statusImageView   = view.findViewById(R.id.statusImageView);
        statusTextView   = view.findViewById(R.id.statusTextView);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            posation = getArguments().getInt("pos");
            cat_id  =  getArguments().getString("cat_id");
        }
    }

    public static GamesFragment getInstance(int pos,String cat_id){
        GamesFragment appsFragment = new GamesFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("pos",pos);
        bundle.putString("cat_id",cat_id);
        appsFragment.setArguments(bundle);
        return appsFragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(getContext()).inflate(R.layout.catagory_fragment,container,false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initView(view);

        allApps = new ArrayList<>();
        listView.setHasFixedSize(true);
        adapter = new RecyclerViewDataAdapter( getContext());
        listView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        listView.setAdapter(adapter);

        retryButton.setOnClickListener(viewe -> {
            animateProgressWithError();
            new Handler().postDelayed(() -> {
                BaseApiController.getInstance().getSubCategoriesForMainCategory(cat_id,this);
            },3000);
        });

        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getContext(),  R.anim.layout_animation_from_bottom);
        listView.setLayoutAnimation(controller);
        listView.scheduleLayoutAnimation();
        listView.setItemAnimator(new NoAnimationItemAnimator());

        BaseApiController.getInstance().getSubCategoriesForMainCategory(cat_id,this);
    }

    @Override
    public void didReceiveData(int type, Object... object) {

        if(object==null){
            displayErrorMessage(getString(R.string.unknow_error));
            return;
        }

        if(type== BaseApiController.didReceivedSubCategories){
            List<SubCategories> categories=(List<SubCategories>)object[0];
            endSize=categories.size();
            for(SubCategories category:categories){
                BaseApiController.getInstance().getAppsForSubCategories(category.getId(),category.getName(),this);
            }
        }else if(type ==  BaseApiController.didReceivedAppsForSubCategories){

            List<BazaarApps.CafeBazaarApp> bazaarApps=(List<BazaarApps.CafeBazaarApp>)object[0];

            if(!bazaarApps.isEmpty()){
                String title = (String) object[1];
                SectionDataModel dataModel=new SectionDataModel();
                dataModel.setHeaderTitle(title);
                dataModel.setCafeBazarApps(bazaarApps);

                new Handler().postDelayed(() -> {
                    adapter.addToDataList(dataModel);
                    if(listView.getVisibility()!=View.VISIBLE){
                        hideProgress();
                    }
                    if(object[2]!=null){
                        String nextLink = (String) object[2];
                        dataModel.setNextLink(nextLink);
                    }

                },500);



            }else{

                endSize--;
            }

        }


    }

    @Override
    public void onError(String error_message) {

        if(getContext()==null){
            return;
        }
        if(AndroidHelper.isNetworkOnline()){
            displayErrorMessage(getString(R.string.unknow_error));
        }else{
            displayErrorMessage(null);
        }
    }


//    public class SectionListDataAdapter extends RecyclerView.Adapter<SectionListDataAdapter.SingleItemRowHolder>{
//
//        private List<BazaarApps.CafeBazaarApp> itemModels;
//        private Context mContext;
//
//
//        SectionListDataAdapter(List<BazaarApps.CafeBazaarApp> itemModel) {
//            this.itemModels = itemModel;
//        }
//
//        @Override
//        public SingleItemRowHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
//            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_item_cell, parent,false);
//            return new SingleItemRowHolder(v);
//        }
//
//
//        @Override
//        public void onBindViewHolder(@NotNull final SingleItemRowHolder holder, final int position) {
//            BazaarApps.CafeBazaarApp itemModel = itemModels.get(position);
//            holder.app_name.setText(AndroidHelper.foramtString(itemModel.getName()));
//            holder.app_size.setText(AndroidHelper.foramtString(itemModel.getSize()));
//            Glide.with(ApplicationLoader.applicationContext)
//                    .load(BaseApiController.base_url + "media/"+ itemModel.getIcon())
//                    .into(holder.app_icon);
//
//            holder.itemView.setOnClickListener(view -> {
//
//                Intent intent=new Intent(mContext, AppDetailActivity.class);
//                intent.putExtra("cafe_app",itemModels.get(position));
//
//                ActivityOptionsCompat options = ActivityOptionsCompat.
//                        makeSceneTransitionAnimation(getActivity(), (View)holder.app_icon
//                                , "profile");
//                startActivity(intent, options.toBundle());
//            });
//
//        }
//
//        @Override
//        public int getItemCount() {
//            return (null != itemModels ? itemModels.size() : 0);
//        }
//
//        class SingleItemRowHolder extends RecyclerView.ViewHolder {
//
//            private ImageView app_icon;
//            private TextView app_name;
//            private TextView app_size;
//
//            SingleItemRowHolder(View itemView) {
//                super(itemView);
//                app_icon=itemView.findViewById(R.id.app_logo);
//                app_name=itemView.findViewById(R.id.app_title);
//                app_size=itemView.findViewById(R.id.app_size);
//            }
//        }
//    }

//    public class RecyclerViewDataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
//
//        private class MainViewHolder extends RecyclerView.ViewHolder {
//            TextView itemTitle;
//            RecyclerView recyclerView;
//            TextView btnMore;
//            MainViewHolder(View itemView) {
//                super(itemView);
//                this.itemTitle = itemView.findViewById(R.id.headerTitile);
//                this.recyclerView = itemView.findViewById(R.id.listView);
//                this.btnMore = itemView.findViewById(R.id.moreTextView);
//            }
//        }
//
//
//        private class ProgressViewHolder extends RecyclerView.ViewHolder {
//            ProgressBar progressBar;
//            ProgressViewHolder(View itemView) {
//                super(itemView);
//                progressBar=itemView.findViewById(R.id.progressBar);
//            }
//        }
//
//        private Context mContext;
//        private RecyclerView.RecycledViewPool recycledViewPool;
//
//        private final int main_view_type=1;
//        private final int progress_view_type=2;
//
//
//        @Override
//        public int getItemViewType(int position) {
//            if(allApps.get(position)==null){
//                return progress_view_type;
//            }else if(allApps.get(position) instanceof SectionDataModel){
//                return  main_view_type;
//
//            }
//            return -1;
//        }
//
//        RecyclerViewDataAdapter(Context context){
//            this.mContext = context;
//            recycledViewPool = new RecyclerView.RecycledViewPool();
//        }
//
//
//        void addToDataList(Object dataModel) {
//
//            if (allApps.isEmpty()) {
//                allApps.add(dataModel);
//                notifyItemInserted(0);
//            } else {
//                Object o = allApps.get(allApps.size() - 1);
//                if (o == null) {
//                    allApps.remove(allApps.size() - 1);
//                    notifyItemRemoved(allApps.size() - 1);
//                }
//                allApps.add(dataModel);
//                notifyItemInserted(allApps.size() - 1);
//            }
//
//
//            //end is reached
//            if (allApps.size() == endSize) {
//                Object o = allApps.get(allApps.size() - 1);
//                if (o == null) {
//                    allApps.remove(allApps.size() - 1);
//                    notifyItemRemoved(allApps.size() - 1);
//                }
//            } else {
//                allApps.add(null);
//            }
//        }
//
//
//
//        @NotNull
//        @Override
//        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            if(viewType==main_view_type){
//                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_horizontal_layout, parent,false);
//                return new MainViewHolder(v);
//            }else {
//                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_progress, parent,false);
//                return new ProgressViewHolder(v);
//            }
//        }
//
//
//
//        @Override
//        public void onBindViewHolder( RecyclerView.ViewHolder viewHolder, int position) {
//
//            if(getItemViewType(position)==main_view_type){
//
//                SectionDataModel sectionDataModel = (SectionDataModel)allApps.get(position);
//
//                MainViewHolder holder=(MainViewHolder)viewHolder;
//
//                final String sectionName = sectionDataModel.getHeaderTitle();
//                final List singleSectionItems = sectionDataModel.getCafeBazarApps();
//
//                SectionListDataAdapter adapter = new SectionListDataAdapter(singleSectionItems);
//
//
//
//                holder.itemTitle.setText(AndroidHelper.foramtString(sectionName) );
//
//                holder.recyclerView.setHasFixedSize(true);
//                holder.recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
//                holder.recyclerView.setAdapter(adapter);
//                holder.recyclerView.setRecycledViewPool(recycledViewPool);
//
//                if(singleSectionItems.size()<=3){
//                    holder.btnMore.setVisibility(View.INVISIBLE);
//                }else{
//                    holder.btnMore.setVisibility(View.VISIBLE);
//                }
//
//
//                holder.btnMore.setOnClickListener(view -> {
//                    Intent intent=new Intent(getContext(), MoreAppsActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("apps",(Serializable)singleSectionItems);
//                    bundle.putBoolean("from_home",true);
//                    bundle.putString("header",AndroidHelper.decodebase64(AndroidHelper.foramtString(sectionName)));
//                    intent.putExtra("bundle",bundle);
//                    startActivity(intent);
//                });
//            }
//
//        }
//
//
//
//        @Override
//        public int getItemCount() {
//            return (null != allApps ? allApps.size() : 0);
//        }
//
//    }




    public class AppCollListADafter extends RecyclerView.Adapter<AppCollListADafter.SingleItemRowHolder>{


        private List<Subcollection> itemModels;

        AppCollListADafter(List<Subcollection> itemModels) {
            this.itemModels = itemModels;
        }


        @Override
        public SingleItemRowHolder onCreateViewHolder( ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.coll_sub_layout, parent,false);
            return new SingleItemRowHolder(v);
        }

        @Override
        public void onBindViewHolder(final SingleItemRowHolder holder, final int position) {
            if(holder!=null){
                Subcollection itemModel = itemModels.get(position);
                if(itemModel==null){
                    return;
                }
                holder.coll_titile.setText(AndroidHelper.foramtString(itemModel.getName()));
                Glide.with(ApplicationLoader.applicationContext)
                        .load(itemModel.getImg())
                        .into(holder.coll_image);

                holder.itemView.setOnClickListener(view -> {
                    Intent intent=new Intent(getActivity(), ColListActivity.class);
                    intent.putExtra("header",itemModel.getName());
                    intent.putExtra("sub_coll_id",itemModel.getId());
                    startActivity(intent);


                    if(getContext() instanceof MainTwoActivity){
                        ((MainTwoActivity)getContext()).showAdd();
                    }


                });
            }


        }

        @Override
        public int getItemCount() {
            return (null != itemModels ? itemModels.size() : 0);
        }

        class SingleItemRowHolder extends RecyclerView.ViewHolder {

            private ImageView coll_image;
            private TextView coll_titile;

            SingleItemRowHolder(View itemView) {
                super(itemView);
                coll_image=itemView.findViewById(R.id.coll_image);
                coll_titile=itemView.findViewById(R.id.colTitile);

            }
        }
    }

    public class SectionListDataAdapter extends RecyclerView.Adapter<SectionListDataAdapter.SingleItemRowHolder>{

        private List<Object> itemModels;

        SectionListDataAdapter(List<Object> itemModels) {
            this.itemModels = itemModels;
        }



        @Override
        public SingleItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_item_cell, parent,false);
            return new SingleItemRowHolder(v);
        }




        @Override
        public void onBindViewHolder(final SingleItemRowHolder holder, final int position) {

            Object object=itemModels.get(position);
            if(object instanceof BazaarApps.CafeBazaarApp){

                BazaarApps.CafeBazaarApp itemModel = (BazaarApps.CafeBazaarApp) object;
                holder.app_name.setText(AndroidHelper.foramtString(itemModel.getName()));
                holder.app_size.setText(AndroidHelper.foramtString(itemModel.getSize()));
                Glide.with(ApplicationLoader.applicationContext)
                        .load(BaseApiController.base_url + "media/"+ itemModel.getIcon())
                        .into(holder.app_icon);

                Log.i("imageinfo",BaseApiController.base_url + "media/"+ itemModel.getIcon());

                holder.itemView.setOnClickListener(view -> {

//                    AppDetialBottomSheet appDetialBottomSheet=new AppDetialBottomSheet();
//                    Bundle bundle=new Bundle();
//                    bundle.putSerializable("cafe_app",itemModel);
//                    bundle.putBoolean("from_home",true);
//                    appDetialBottomSheet.setArguments(bundle);
//
//                    appDetialBottomSheet.show(getFragmentManager(), appDetialBottomSheet.getTag());

                    // AndroidHelper.openAppInGooglePlay(itemModel.getPackage_name(),getContext())

//


                    Intent intent=new Intent(getContext(), AppDetailActivity.class);
                    intent.putExtra("cafe_app",itemModel);
                    intent.putExtra("from_home",true);

                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(getActivity(), holder.app_icon
                                    , "profile");
                    startActivity(intent, options.toBundle());


                    if(getContext() instanceof MainTwoActivity){
                        ((MainTwoActivity)getContext()).showAdd();
                    }




                });
            }else if(object instanceof Recommended){
                Recommended itemModel = (Recommended)object;
                holder.app_name.setText(AndroidHelper.foramtString(itemModel.getName()));
                holder.app_size.setText(AndroidHelper.foramtString(itemModel.getSize()));
                Glide.with(ApplicationLoader.applicationContext)
                        .load(itemModel.getIcon())
                        .into(holder.app_icon);

                holder.itemView.setOnClickListener(view -> {



                });
            }



        }

        @Override
        public int getItemCount() {
            return (null != itemModels ? itemModels.size() : 0);
        }

        class SingleItemRowHolder extends RecyclerView.ViewHolder {

            private ImageView app_icon;
            private TextView app_name;
            private TextView app_size;

            SingleItemRowHolder(View itemView) {
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
            FrameLayout moreFrame;
            MainViewHolder(View itemView) {
                super(itemView);
                this.itemTitle = itemView.findViewById(R.id.headerTitile);
                this.recyclerView = itemView.findViewById(R.id.listView);
                this.btnMore = itemView.findViewById(R.id.moreTextView);
                this.moreFrame=itemView.findViewById(R.id.moreItemFrame);
                //add

            }


        }

        private class ProgressViewHolder extends RecyclerView.ViewHolder {
            ProgressBar progressBar;
            ProgressViewHolder(@NonNull View itemView) {
                super(itemView);
                progressBar=itemView.findViewById(R.id.progressBar);
            }
        }


        private class CollAppViewHolder extends RecyclerView.ViewHolder{
            TextView itemTitle;
            RecyclerView recyclerView;
            public CollAppViewHolder(@NonNull View itemView) {
                super(itemView);
                this.itemTitle = itemView.findViewById(R.id.headerTitile);
                this.recyclerView = itemView.findViewById(R.id.listView);



            }
        }

        private Context mContext;
        private RecyclerView.RecycledViewPool recycledViewPool;

        private int count=0;
        private final int main_view_type=count++;
        private final int progress_view_type=count++;
        private final int app_coll_type=count++;
        private final int app_reco_type=count++;


        @Override
        public int getItemViewType(int position) {

            if(allApps.get(position) instanceof SectionDataModel){
                return main_view_type;
            }else if(allApps.get(position) instanceof CollDataModel){
                return app_coll_type;
            }else if(allApps.get(position)==null){
                return progress_view_type;
            }else if(allApps.get(position) instanceof RecoSectionModel){
                return app_reco_type;
            }
            return -1;
        }


        RecyclerViewDataAdapter(Context context){
            this.mContext = context;
            recycledViewPool = new RecyclerView.RecycledViewPool();
        }


        void addToDataList(Object dataModel){

            if(allApps.isEmpty()){
                allApps.add(dataModel);
                notifyItemInserted(0);
            }else{
                Object o = allApps.get(allApps.size()-1);
                if(o == null){
                    allApps.remove(allApps.size()-1);
                    notifyItemRemoved(allApps.size()-1);
                }
                allApps.add(dataModel);
                notifyItemInserted(allApps.size()-1);

            }


            //end is reached
            if(allApps.size()==endSize) {
                Object o = allApps.get(allApps.size()-1);
                if(o == null){
                    allApps.remove(allApps.size()-1);
                    notifyItemRemoved(allApps.size()-1);
                }
            }else{
                allApps.add(null);
            }

            //notifyItemRangeChanged(0,allApps.size()-1);



//            if(allApps.isEmpty()){
//                allApps.add(dataModel);
//                notifyItemInserted(0);
//            }else{
//                allApps.remove(allApps.size()-1);
//                notifyItemRemoved(allApps.size()-1);
//                allApps.add(dataModel);
//                notifyItemInserted(allApps.size()-1);
//                if(!(getItemCount()==endSize-1)){
//                    allApps.add(null);
//                    notifyItemInserted(allApps.size()-1);
//                }else{
//                   if(allApps.get(allApps.size()-1)==null){
//                       allApps.remove(allApps.size()-1);
//                   }
//                }
//            }
        }



        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if(viewType==main_view_type){
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_horizontal_layout, parent,false);
                return new MainViewHolder(v);
            }else if(viewType==app_coll_type){
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.coll_layout, parent,false);
                return new CollAppViewHolder(v);
            }else if(viewType==app_reco_type) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_horizontal_layout, parent,false);
                return new MainViewHolder(v);
            }else {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_progress, parent,false);
                return new ProgressViewHolder(v);
            }
        }


        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
            if(getItemViewType(position)==main_view_type){
                SectionDataModel sectionDataModel=(SectionDataModel)allApps.get(position);

                MainViewHolder holder=(MainViewHolder)viewHolder;

                final String sectionName = sectionDataModel.getHeaderTitle();
                final List singleSectionItems = sectionDataModel.getCafeBazarApps();

                SectionListDataAdapter adapter = new SectionListDataAdapter(singleSectionItems);

                holder.itemTitle.setText(AndroidHelper.foramtString(sectionName));
                holder.recyclerView.setHasFixedSize(true);
                holder.recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                holder.recyclerView.setAdapter(adapter);
                LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getContext(),  R.anim.layout_animation_from_bottom);
                holder.recyclerView.setLayoutAnimation(controller);
                holder.recyclerView.scheduleLayoutAnimation();
                holder.recyclerView.setRecycledViewPool(recycledViewPool);

                if(singleSectionItems.size()<=3){
                    holder.btnMore.setVisibility(View.INVISIBLE);
                }else{
                    holder.btnMore.setVisibility(View.VISIBLE);
                }

                holder.moreFrame.setOnClickListener(view -> {

                    Intent intent=new Intent(getContext(), MoreAppsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("apps",(Serializable)singleSectionItems);
                    bundle.putBoolean("from_home",true);
                    bundle.putString("header",AndroidHelper.foramtString(sectionName));
                    bundle.putString("nextLink",sectionDataModel.getNextLink());
                    intent.putExtra("bundle",bundle);
                    startActivity(intent);


                    if(getContext() instanceof MainTwoActivity){
                        ((MainTwoActivity)getContext()).showAdd();
                    }


                });
            }else if(getItemViewType(position)==app_coll_type){



                CollDataModel collection=(CollDataModel)allApps.get(position);
                CollAppViewHolder holder=(CollAppViewHolder)viewHolder;
                final String sectionName = collection.getHeaderTitle();
                holder.itemTitle.setText(AndroidHelper.foramtString(sectionName));
                AppCollListADafter adapter = new AppCollListADafter(collection.getAppCollecation());
                holder.recyclerView.setHasFixedSize(true);
                holder.recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                holder.recyclerView.setAdapter(adapter);
                holder.recyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());


            }else if(getItemViewType(position)==app_reco_type){

                RecoSectionModel sectionDataModel=(RecoSectionModel)allApps.get(position);
                MainViewHolder holder=(MainViewHolder)viewHolder;
                final String sectionName = sectionDataModel.getHeaderTitle();
                final List singleSectionItems = sectionDataModel.getCafeBazarApps();
                holder.itemTitle.setText(AndroidHelper.foramtString(sectionName));
                SectionListDataAdapter adapter = new SectionListDataAdapter(singleSectionItems);
                holder.recyclerView.setHasFixedSize(true);
                holder.recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                holder.recyclerView.setAdapter(adapter);
                holder.recyclerView.setRecycledViewPool(recycledViewPool);
                holder.btnMore.setVisibility(View.INVISIBLE);

            }

        }

        @Override
        public int getItemCount() {
            return (null != allApps ? allApps.size() : 0);
        }

    }


    private void hideProgress(){
        if(progressBar!=null && listView!=null){
            errorView.setVisibility(View.INVISIBLE);
            int mShortAnimationDuration=200;
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
        int mShortAnimationDuration=200;
        progressBar.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.INVISIBLE);

        errorView.setAlpha(0f);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.animate()
                .alpha(1f)
                .setDuration(mShortAnimationDuration)
                .setListener(null);
        errorView.animate()
                .alpha(0f)
                .setDuration(mShortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        errorView.setVisibility(View.GONE);
                    }
                });
    }

    private void displayErrorMessage(String errorMessage){

        if(progressBar!=null && errorView!=null){

            if(errorMessage!=null){
                statusTextView.setText(errorMessage);
            }


            int mShortAnimationDuration=200;
            errorView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            errorView.setAlpha(0f);
            errorView.setVisibility(View.VISIBLE);
            errorView.animate()
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
