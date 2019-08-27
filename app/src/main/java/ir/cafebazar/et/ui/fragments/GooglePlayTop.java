package ir.cafebazar.et.ui.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.os.Handler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import apps.cafebazaar.all.apps.R;

import ir.cafebazar.et.AndroidHelper;
import ir.cafebazar.et.ApplicationLoader;
import ir.cafebazar.et.Models.Play.PlayApp;
import ir.cafebazar.et.Models.Play.PlayCategory;
import ir.cafebazar.et.Models.Play.PlayFilter;
import ir.cafebazar.et.network.BaseApiController;
import ir.cafebazar.et.ui.activity.MainTwoActivity;


import com.bumptech.glide.Glide;



public class GooglePlayTop extends Fragment implements BaseApiController.ApiCallBack{

    private RecyclerView listView;
    private ListAdapter listAdapter;
    private Spinner catSpiner;
    private Spinner filterSpinner;
    private ProgressBar progressBar;
    private LinearLayoutManager layoutManager;
    private LinearLayout error_view;

    private List<PlayCategory.PlayCat> playCats=new ArrayList<>();
    private List<PlayFilter> playFilters=new ArrayList<>();
    private List<PlayApp> playApps=new ArrayList<>();



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=LayoutInflater.from(getContext()).inflate(R.layout.google_play_top_apps_fragment,container,false);
        catSpiner=view.findViewById(R.id.catSpinner);
        filterSpinner=view.findViewById(R.id.filterSpinner);
        progressBar=view.findViewById(R.id.progressBar);
        listView=view.findViewById(R.id.listView);
        listAdapter=new ListAdapter();
        listView.setLayoutManager(layoutManager=new LinearLayoutManager(getContext()));
        listView.setAdapter(listAdapter);

        error_view=view.findViewById(R.id.errorView);
        Button retry = view.findViewById(R.id.retryButton);
        retry.setOnClickListener(view1 -> {
            animateProgressWithError();
            new Handler().postDelayed(() -> {
                showProgress();
                BaseApiController.getInstance().getPlayCategories(GooglePlayTop.this);

            },300);

        });



        BaseApiController.getInstance().getPlayCategories(this);
        BaseApiController.getInstance().getPlayFilters(this);



        catSpiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(catSpiner.getCount()==0 || filterSpinner.getCount()==0){
                    return;
                }
                showProgress();
                callApi(catSpiner.getSelectedItemPosition(),filterSpinner.getSelectedItemPosition());

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(catSpiner.getCount()==0 || filterSpinner.getCount()==0){
                    return;
                }
                showProgress();
                callApi(catSpiner.getSelectedItemPosition(),filterSpinner.getSelectedItemPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return view;
    }


    private void setListeners(){
        if(!playCats.isEmpty() && !playFilters.isEmpty()){
            catSpiner.setSelection(0);
            filterSpinner.setSelection(0);


        }
    }


    private void callApi(int catSpinPos,int playFilterPost){


        if(getContext() instanceof MainTwoActivity){
            ((MainTwoActivity)getContext()).showAdd();
        }


        BaseApiController.getInstance().getPlayApps(playCats.get(catSpinPos).getId(),
                playFilters.get(playFilterPost).getId(),GooglePlayTop.this);

    }





    @Override
    public void didReceiveData( int type ,Object... object) {
        if(object==null) {
            displayErrorMessage(null);
            return;
        }


        if(type== BaseApiController.didReceivePlayCategory){

            PlayCategory playCategory=(PlayCategory)object[0];
            playCats=playCategory.getPlayCategories();
            List<String> cats=new ArrayList<>();
            for(PlayCategory.PlayCat playCat:playCats){
                cats.add(playCat.getName());
            }
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), android.R.layout.simple_spinner_item,cats);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            catSpiner.setAdapter(dataAdapter);
            setListeners();

        }else if(type== BaseApiController.didReceivePlayFilters){
              playFilters=(List<PlayFilter>)object[0];
              final List<String> filters=new ArrayList<>();
              for(PlayFilter filter:playFilters){
                 filters.add(filter.getName());
              }
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), android.R.layout.simple_spinner_item,filters);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            filterSpinner.setAdapter(dataAdapter);
            setListeners();

        }else if(type== BaseApiController.didReceivedPlayApps){

            playApps=(List<PlayApp>)object[0];
            listAdapter.notifyDataSetChanged();
            layoutManager.scrollToPositionWithOffset(0, 0);
            hideProgress();
        }


    }


    @Override
    public void onError(String error_message) {

        displayErrorMessage(ApplicationLoader.getStr(R.string.unknow_error));

    }


    private class ListAdapter extends RecyclerView.Adapter<ListAdapter.TextViewHolder>{

        @NonNull
        @Override
        public TextViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view=LayoutInflater.from(getContext()).inflate(R.layout.category_item,viewGroup,false);
            return new TextViewHolder(view);
        }


        @Override
        public void onBindViewHolder(@NonNull TextViewHolder textViewHolder, final int i) {
           final PlayApp playApp=playApps.get(i);
           textViewHolder.rank.setText(playApp.getRank());
           textViewHolder.devloperName.setText("by " + AndroidHelper.foramtString(playApp.getDeveloper()));
           textViewHolder.title.setText(AndroidHelper.foramtString(playApp.getName()));
           Glide.with(ApplicationLoader.applicationContext).load(BaseApiController.base_url + "media/"+ playApp.getIcon()).into(textViewHolder.logo);

           textViewHolder.itemView.setOnClickListener(view -> {

               AndroidHelper.openAppRating(getContext(),playApp.getPackagename());

               if(getContext() instanceof MainTwoActivity){
                   ((MainTwoActivity)getContext()).showAdd();
               }


           });

           textViewHolder.more.setOnClickListener(view -> {

               PopupMenu popupMenu=new PopupMenu(getContext(),textViewHolder.more);

               popupMenu.getMenu().add(getString(R.string.share_app));

               popupMenu.setOnMenuItemClickListener(menuItem -> {
                   if(menuItem.getTitle().equals(getString(R.string.share_app))){


                       AndroidHelper.share("https://play.google.com/store/apps/details?id=" +playApp.getPackagename(),getContext());
                   }else if(menuItem.getTitle().equals(getString(R.string.downlod_on_telegram))){
                       AndroidHelper.share("https://telegram.me/apkdl_bot",getContext());
                   }

                   return true;
               });
               popupMenu.show();
           });
        }

        @Override
        public int getItemCount() {
            return playApps!=null?playApps.size():0;
        }

        class TextViewHolder extends RecyclerView.ViewHolder{

            TextView title;
            TextView devloperName;
            ImageView logo;
            TextView rank;
            ImageView more;

            TextViewHolder(@NonNull View itemView) {
                super(itemView);
                title=itemView.findViewById(R.id.titile);
                devloperName=itemView.findViewById(R.id.devloper);
                rank=itemView.findViewById(R.id.rank);
                logo=itemView.findViewById(R.id.logo);
                more=itemView.findViewById(R.id.more);

            }
        }

    }





    private void animateProgressWithError(){
        int mShortAnimationDuration=200;
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



    private void showProgress(){
        if(progressBar!=null && listView!=null){
            int mShortAnimationDuration=200;
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



    private void displayErrorMessage(String errorMessage){

        if(progressBar!=null && error_view!=null){

            if(errorMessage!=null){
                ((TextView)error_view.findViewById(R.id.errorText)).setText(errorMessage);
            }

            int mShortAnimationDuration=200;
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
