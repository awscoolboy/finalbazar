package ir.cafebazar.et.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import apps.cafebazaar.all.apps.R;
import ir.cafebazar.et.AndroidHelper;
import ir.cafebazar.et.Models.Categories;
import ir.cafebazar.et.auth.AccountBottomSheetDialog;
import ir.cafebazar.et.bazar.models.Category;
import ir.cafebazar.et.network.BaseApiController;
import ir.cafebazar.et.ui.activity.SearchActivity;


public class GamePagerFragment extends Fragment {

    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ImageView profileImageView;
    private TextView toolbarTextView;


    private ArrayList<Category> categoryList = new ArrayList<>();


    private void initView(View view){
        toolbar = view.findViewById(R.id.toolbar);
        viewPager = view.findViewById(R.id.viewpager);
        tabLayout = view.findViewById(R.id.tabs);
        profileImageView = view.findViewById(R.id.profileImageView);
        toolbarTextView = view.findViewById(R.id.toolBarTitle);

    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=LayoutInflater.from(getContext()).inflate(R.layout.category_view_pager,container,false);
        initView(view);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.inflateMenu(R.menu.menu_home);
        toolbar.setOnMenuItemClickListener(menuItem -> {

            if(menuItem.getItemId()==R.id.action_search){
                Intent intent = new Intent(getContext(), SearchActivity.class);
                startActivity(intent);
            }

            return false;
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            Uri imageUrl = user.getPhotoUrl();
            Glide.with(this).load(imageUrl).apply(RequestOptions.circleCropTransform()).into(profileImageView);
        }


        profileImageView.setOnClickListener(v -> {

            AccountBottomSheetDialog accountBottomSheetDialog = new AccountBottomSheetDialog();
            accountBottomSheetDialog.show(getChildFragmentManager(),"account_sheet");
        });



        toolbarTextView.setText(getString(R.string.menu_games));
        getCategory();
    }




    private void getCategory(){
        BaseApiController.getInstance().getBazaarCategories(true,new BaseApiController.ApiCallBack() {
            @Override
            public void didReceiveData(int type, Object... object) {

                if(object != null && object.length >0){
                    categoryList = (ArrayList<Category>)object[0];
                    setUpViewPagers();


                }
            }

            @Override
            public void onError(String error_message) {

            }
        });

    }


    private void setUpViewPagers(){
        AppPagerAdapter appPagerAdapter = new AppPagerAdapter(getChildFragmentManager());
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(appPagerAdapter);
    }



    private class AppPagerAdapter extends FragmentStatePagerAdapter {


        AppPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return GamesFragment.getInstance(i,categoryList.get(i).getId());
        }

        @Override
        public int getCount() {
            return categoryList!=null?categoryList.size():0;
        }


        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return AndroidHelper.foramtString(categoryList.get(position).getName());
        }
    }
}
