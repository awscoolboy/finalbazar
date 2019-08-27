package ir.cafebazar.et.ui.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
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
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import apps.cafebazaar.all.apps.R;
import ir.cafebazar.et.AndroidHelper;
import ir.cafebazar.et.ApplicationLoader;
import ir.cafebazar.et.Models.BazaarApps;
import ir.cafebazar.et.Models.Categories;
import ir.cafebazar.et.Models.section.RecoSectionModel;
import ir.cafebazar.et.Models.Recommended;
import ir.cafebazar.et.Models.section.SectionDataModel;
import ir.cafebazar.et.Models.collections.CollDataModel;
import ir.cafebazar.et.Models.collections.Collections;
import ir.cafebazar.et.Models.collections.Subcollection;
import ir.cafebazar.et.auth.AccountBottomSheetDialog;
import ir.cafebazar.et.network.BaseApiController;
import ir.cafebazar.et.ui.activity.AppDetailActivity;
import ir.cafebazar.et.ui.activity.ColListActivity;
import ir.cafebazar.et.ui.activity.MainTwoActivity;
import ir.cafebazar.et.ui.activity.MoreAppsActivity;
import ir.cafebazar.et.ui.activity.SearchActivity;
import ir.cafebazar.et.util.NoAnimationItemAnimator;


public class HomeFragment extends Fragment implements BaseApiController.ApiCallBack{

    private RecyclerView listView;
    private ProgressBar progressBar;
    private LinearLayout error_view;
    private Toolbar toolbar;
    private Button retry;
    private ImageView profileImageView;

    private List<Object> allApps;

    private RecyclerViewDataAdapter adapter;

    private int endSize;


    private void initView(View view){
        listView=view.findViewById(R.id.listView);
        progressBar=view.findViewById(R.id.progressBar);
        error_view=view.findViewById(R.id.errorView);
        retry = view.findViewById(R.id.retryButton);
        toolbar = view.findViewById(R.id.toolbar);
        profileImageView = view.findViewById(R.id.profileImageView);
    }


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(getContext()).inflate(R.layout.ic_home_fragment,container,false);
    }



    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initView(view);
        toolbar.inflateMenu(R.menu.menu_home);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
          Uri imageUrl = user.getPhotoUrl();
          Glide.with(this).load(imageUrl).apply(RequestOptions.circleCropTransform()).into(profileImageView);
        }

      profileImageView.setOnClickListener(v -> {

          AccountBottomSheetDialog accountBottomSheetDialog = new AccountBottomSheetDialog();
          accountBottomSheetDialog.show(getChildFragmentManager(),"account_sheet");
      });

        toolbar.setOnMenuItemClickListener(menuItem -> {

            if(menuItem.getItemId()==R.id.action_search){
                Intent intent = new Intent(getContext(), SearchActivity.class);
                startActivity(intent);

            }
            return false;
        });

        retry.setOnClickListener(view1 -> {

            animateProgressWithError();
            BaseApiController.getInstance().getHomeCategories(HomeFragment.this);

        });


        allApps=new ArrayList<>();
        listView.setHasFixedSize(true);
        adapter = new RecyclerViewDataAdapter(getContext());
        listView.setAdapter(adapter);
        listView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getContext(),  R.anim.layout_animation_from_bottom);
        listView.setLayoutAnimation(controller);
        listView.scheduleLayoutAnimation();
        listView.setItemAnimator(new NoAnimationItemAnimator());


        BaseApiController.getInstance().getHomeCategories(this);

    }

    @Override
    public void didReceiveData(int type,Object... object) {
        if(object==null){
            displayErrorMessage(getString(R.string.unknow_error));
            return;
        }

        if(type==BaseApiController.didReceivedHomeCategories){
            List<Categories> categories=(List<Categories>)object[0];
            endSize=categories.size();
            int i = 0;
            for(Categories category:categories){
                i++;
                BaseApiController.getInstance().getHomeSubCategoryApps(category.getId(),category.getName(),this);
                if(i==2){
                    BaseApiController.getInstance().getCollection(HomeFragment.this);
                }

            }


            BaseApiController.getInstance().getRecommendedApps(HomeFragment.this);
            BaseApiController.getInstance().getAdverizmentApps(HomeFragment.this);
        }else  if(type==BaseApiController.didReceivedHomeSubCatApps){
            List<BazaarApps.CafeBazaarApp> bazaarApps=(List<BazaarApps.CafeBazaarApp>) object[0];
            String secTitle = (String) object[1];
            if(bazaarApps.size()>1){
                SectionDataModel dataModel=new SectionDataModel();
                dataModel.setHeaderTitle(secTitle);
                dataModel.setCafeBazarApps(bazaarApps);
                adapter.addToDataList(dataModel);

                if(listView.getVisibility()!=View.VISIBLE){
                    hideProgress();
                }

            }else{
                endSize--;
            }
        }else if(type==BaseApiController.didReceiveCollations){
            Collections collection=(Collections)object[0];
            if(collection.getResults()==null || collection.getResults().isEmpty()){
                return;
            }
            for(Collections.Collection col:collection.getResults()){
                BaseApiController.getInstance().getSubCollForCollId(col.getId(),col.getName(),this);
            }

        }else if(type==BaseApiController.didReceiveSubCollations){

            List<Subcollection> subCollections=(List<Subcollection>)object[1];


            if(subCollections.size()>1){
                endSize++;
                CollDataModel collDataModel=new CollDataModel();
                collDataModel.setHeaderTitle(object[0].toString());
                collDataModel.setAppCollecation(subCollections);
                Random random=new Random();
                int val=random.nextInt(allApps.size());
                adapter.addToDataList(collDataModel,val);

            }else{
                endSize--;
            }

//
//            allApps.add(val,collDataModel);
//
//            adapter.notifyItemInserted(val);



        }else if(type==BaseApiController.didReceiveAdvertisementApps){
            endSize++;
            List<Recommended> bazaarApps=(List<Recommended>) object[0];
            if(bazaarApps.size()!=0){
                RecoSectionModel dataModel=new RecoSectionModel();
                dataModel.setHeaderTitle(getString(R.string.sujjested_tet));
                dataModel.setCafeBazarApps(bazaarApps);
                if(allApps.size()>=2){
                    allApps.add(2,dataModel);
                    adapter.notifyItemInserted(2);

                }
            }

        }else if(type==BaseApiController.didReceiveRecommendedApps){
            endSize++;
            listView.smoothScrollToPosition(0);
            List<Recommended> recommendeds=(List<Recommended>) object[0];
            if(recommendeds.size()!=0){
                RecoSectionModel dataModel=new RecoSectionModel();
                dataModel.setHeaderTitle(getString(R.string.recomended));
                dataModel.setCafeBazarApps(recommendeds);
                allApps.add(0,dataModel);
                adapter.notifyItemInserted(0);
            }
        }


    }





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

                    if(!AndroidHelper.openAppInGooglePlay(itemModel.getPackage_name(),getContext())){
                         AndroidHelper.openAppInChrome(itemModel.getUrl(),getContext());
                    }

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


    //the main adapter
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
            }else if(allApps.get(position) instanceof  RecoSectionModel){
                return app_reco_type;
            }
            return -1;
        }


        RecyclerViewDataAdapter(Context context){
            this.mContext = context;
            recycledViewPool = new RecyclerView.RecycledViewPool();
        }


        void addToDataList(Object dataModel,int pos){

            if(allApps.isEmpty()){
                allApps.add(dataModel);
                notifyItemInserted(0);
            }else{

                Object o = allApps.get(allApps.size()-1);
                if(o == null){
                    allApps.remove(allApps.size()-1);
                    notifyItemRemoved(allApps.size()-1);
                }
                allApps.add(pos,dataModel);
                notifyItemInserted(pos);
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
                holder.recyclerView.setRecycledViewPool(recycledViewPool);


                LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getContext(),  R.anim.layout_animation_from_bottom);
                holder.recyclerView.setLayoutAnimation(controller);
                holder.recyclerView.scheduleLayoutAnimation();
                holder.recyclerView.setItemAnimator(new NoAnimationItemAnimator());

                if(singleSectionItems.size()<=3){
                    holder.btnMore.setVisibility(View.GONE);
                }else{
                    holder.btnMore.setVisibility(View.VISIBLE);
                }

               holder.moreFrame.setOnClickListener(view -> {



                   Intent intent=new Intent(getContext(), MoreAppsActivity.class);
                   Bundle bundle = new Bundle();
                   bundle.putSerializable("apps",(Serializable)singleSectionItems);
                   bundle.putBoolean("from_home",true);
                   bundle.putString("header",AndroidHelper.foramtString(sectionName));
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


                LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getContext(),  R.anim.layout_animation_from_bottom);
                holder.recyclerView.setLayoutAnimation(controller);
                holder.recyclerView.scheduleLayoutAnimation();
                holder.recyclerView.setItemAnimator(new NoAnimationItemAnimator());

            }else if(getItemViewType(position)==app_reco_type){

                RecoSectionModel sectionDataModel=(RecoSectionModel)allApps.get(position);
                MainViewHolder holder=(MainViewHolder)viewHolder;
                final String sectionName = sectionDataModel.getHeaderTitle();
                final List singleSectionItems = sectionDataModel.getCafeBazarApps();
                holder.itemTitle.setText(sectionName);
                SectionListDataAdapter adapter = new SectionListDataAdapter(singleSectionItems);
                holder.recyclerView.setHasFixedSize(true);
                holder.recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                holder.recyclerView.setAdapter(adapter);
                holder.recyclerView.setRecycledViewPool(recycledViewPool);
                holder.btnMore.setVisibility(View.INVISIBLE);


                LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getContext(),  R.anim.layout_animation_from_bottom);
                holder.recyclerView.setLayoutAnimation(controller);
                holder.recyclerView.scheduleLayoutAnimation();
                holder.recyclerView.setItemAnimator(new NoAnimationItemAnimator());

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
            int mShortAnimationDuration= 200;
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
                    .setListener(  new AnimatorListenerAdapter() {
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


    @Override
    public void onError(String error_message) {

        if(AndroidHelper.isNetworkOnline()){
            displayErrorMessage(ApplicationLoader.getStr(R.string.unknow_error));
        }else{
            displayErrorMessage(null);
        }
    }





}
