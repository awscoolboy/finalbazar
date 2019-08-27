package ir.cafebazar.et.ui.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import apps.cafebazaar.all.apps.R;
import ir.cafebazar.et.AndroidHelper;
import ir.cafebazar.et.ApplicationLoader;
import ir.cafebazar.et.Models.BazaarApps;
import ir.cafebazar.et.Models.Favorite;
import ir.cafebazar.et.auth.AccountBottomSheetDialog;
import ir.cafebazar.et.database.DatabaseHandler;
import ir.cafebazar.et.network.BaseApiController;
import ir.cafebazar.et.ui.activity.AppDetailActivity;
import ir.cafebazar.et.ui.activity.MainTwoActivity;
import ir.cafebazar.et.ui.activity.SearchActivity;


public class FavoriteFragment extends Fragment {

    private RecyclerView listView;
    private ListAdapter listAdapter;
    private List<Favorite> appList=new ArrayList<>();
    private ProgressBar progressBar;
    private LinearLayout error_view;
    public Toolbar toolbar;


    private TextView toolbarTextView;
    private ImageView profileImageView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=LayoutInflater.from(getContext()).inflate(R.layout.favorite_ui,container,false);

        toolbar = view.findViewById(R.id.toolbar);

        toolbarTextView = view.findViewById(R.id.toolBarTitle);
        profileImageView = view.findViewById(R.id.profileImageView);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            Uri imageUrl = user.getPhotoUrl();
            Glide.with(this).load(imageUrl).apply(RequestOptions.circleCropTransform()).into(profileImageView);
        }


        profileImageView.setOnClickListener(v -> {

            AccountBottomSheetDialog accountBottomSheetDialog = new AccountBottomSheetDialog();
            accountBottomSheetDialog.show(getChildFragmentManager(),"account_sheet");
        });

        toolbarTextView.setText(getString(R.string.menu_fav));


        listView=view.findViewById(R.id.listView);
        progressBar=view.findViewById(R.id.progressBar);
        listAdapter=new ListAdapter();
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        listView.setAdapter(listAdapter);
        error_view=view.findViewById(R.id.errorView);
        appList= DatabaseHandler.getInstance().appDao().getAllFavApps();
        if(appList.isEmpty()){
            hideProgress();
            new Handler().postDelayed(this::animateProgressWithError,300);

        }else{
            new Handler().postDelayed(() -> {
                hideProgress();
                listAdapter.notifyDataSetChanged();
            },300);

        }

        toolbar.inflateMenu(R.menu.menu_home);
        toolbarTextView.setText(getString(R.string.menu_apps));
        toolbar.setOnMenuItemClickListener(menuItem -> {

            if(menuItem.getItemId()==R.id.action_search){
                Intent intent = new Intent(getContext(), SearchActivity.class);
                startActivity(intent);

            }
            return false;
        });

        return view;
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
            final Favorite playApp=appList.get(i);
            textViewHolder.rank.setText(String.valueOf(i));
            textViewHolder.devloperName.setText("by " + AndroidHelper.foramtString(playApp.getDeveloper()));
            textViewHolder.title.setText(AndroidHelper.foramtString(playApp.getName()));
            Glide.with(ApplicationLoader.applicationContext).load(BaseApiController.base_url + "media/"+ playApp.getIcon()).into(textViewHolder.logo);
            textViewHolder.itemView.setOnClickListener(view -> {

                Intent intent=new Intent(getContext(), AppDetailActivity.class);
                intent.putExtra("cafe_app",convrtFav(playApp));
                intent.putExtra("from_home",true);

                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(getActivity(), textViewHolder.logo
                                , "profile");
                startActivity(intent, options.toBundle());


                if(getContext() instanceof MainTwoActivity){
                    ((MainTwoActivity)getContext()).showAdd();
                }


            });

            textViewHolder.more.setOnClickListener(view -> {

                PopupMenu popupMenu=new PopupMenu(getContext(),textViewHolder.more);

                popupMenu.getMenu().add(getString(R.string.delete_app));
                popupMenu.getMenu().add(getString(R.string.share_app));

                popupMenu.setOnMenuItemClickListener(menuItem -> {
                    if(menuItem.getTitle().equals(getString(R.string.share_app))){

                        AndroidHelper.share("https://play.google.com/store/apps/details?id=" +playApp.getPackage_name(),getContext());

                    }else if(menuItem.getTitle().equals(getString(R.string.delete_app))){
                        DatabaseHandler.getInstance().appDao().deleteFavApp(playApp);
                        appList.remove(i);
                        if(getItemCount()>=1){
                            notifyItemRemoved(i);
                        }else{
                            notifyDataSetChanged();
                        }
                    }

                    return true;
                });
                popupMenu.show();
            });

        }

        @Override
        public int getItemCount() {
            return appList!=null?appList.size():0;
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


    private BazaarApps.CafeBazaarApp convrtFav(Favorite cafeBazaarApp){
        BazaarApps.CafeBazaarApp favorite= new BazaarApps.CafeBazaarApp();
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

        int mShortAnimationDuration=200;
        error_view.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);

        progressBar.setAlpha(0f);
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
