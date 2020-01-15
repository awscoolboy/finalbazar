package ir.cafebazar.et.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import apps.cafebazaar.all.apps.R;
import ir.cafebazar.et.AndroidHelper;
import ir.cafebazar.et.ApplicationLoader;
import ir.cafebazar.et.util.AppPreference;

public class AccountBottomSheetDialog extends BottomSheetDialogFragment {


    private RecyclerView listView;
    private ListAdapter listAdapter;

    private FirebaseUser firebaseUser;

    private  int rowCount;

    private  int account_row;
    private  int share_app_row;
    private  int telegram_channel_row;
    private  int logoutRow;


    public AccountBottomSheetDialog(){

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser==null){
            Intent intent = new Intent(getContext(),SignUpActivity.class);
            startActivity(intent);
            dismiss();
            return;
        }
        rowCount = 0;

        account_row = rowCount++;
        share_app_row = rowCount++;
        telegram_channel_row = rowCount++;
        logoutRow   = rowCount++;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.account_layout,container,false);


        listView = view.findViewById(R.id.listView);
        listAdapter = new ListAdapter(getContext());
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        listView.setAdapter(listAdapter);


        return view;
    }



    private class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{



        private Context context;

        public ListAdapter(Context con){
            context = con;
        }


        class AccountItemHolder extends RecyclerView.ViewHolder{
            ImageView profileImageView;
            TextView  emailTextView;
            TextView  userNameTextView;
            AccountItemHolder(@NonNull View itemView) {
                super(itemView);
                  profileImageView = itemView.findViewById(R.id.profileImageView);
                  emailTextView = itemView.findViewById(R.id.emailTextView);
                  userNameTextView = itemView.findViewById(R.id.usernameTextView);

            }
        }


        @Override
        public int getItemViewType(int position) {

            if (position == 0) {
                return account_row;
            } else if (position == 1) {
                return share_app_row;
            } else if (position == 2) {
                return telegram_channel_row;
            } else if (position == 3) {
                return logoutRow;
            }
            return super.getItemViewType(position);

        }

        class SettingItemHolder extends RecyclerView.ViewHolder{

            ImageView icon;
            TextView title;
            SettingItemHolder(@NonNull View itemView) {
                super(itemView);
                icon = itemView.findViewById(R.id.icon);
                title = itemView.findViewById(R.id.name);

            }
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

            i = getItemViewType(i);

            View view;
            if(i == account_row){
                view = LayoutInflater.from(getContext()).inflate(R.layout.account_item_layout,viewGroup,false);
                return new AccountItemHolder(view);
            }else if( i==logoutRow || i==share_app_row || i==telegram_channel_row){
                view = LayoutInflater.from(getContext()).inflate(R.layout.setting_item,viewGroup,false);
                return new SettingItemHolder(view);
            }

            return new AccountItemHolder(new View(viewGroup.getContext()));

        }


        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

            if(getItemViewType(i)==account_row) {

                AccountItemHolder accountItemHolder = (AccountItemHolder) viewHolder;


                if (firebaseUser.getPhotoUrl() != null) {
                    Glide.with(ApplicationLoader.applicationContext).load(firebaseUser.getPhotoUrl()).into(accountItemHolder.profileImageView);
                }
                if (firebaseUser.getEmail() != null) {
                    accountItemHolder.emailTextView.setText(firebaseUser.getEmail());
                }


                if (firebaseUser.getDisplayName() != null) {
                    accountItemHolder.userNameTextView.setText(firebaseUser.getDisplayName());
                }


            }else if(getItemViewType(i)==share_app_row){
                SettingItemHolder settingItemHolder = (SettingItemHolder) viewHolder;
                settingItemHolder.title.setText(ApplicationLoader.getStr(R.string.menu_share));
                settingItemHolder.icon.setImageResource(R.drawable.ic_share);
                settingItemHolder.itemView.setOnClickListener(v -> {
                    AndroidHelper.share("https://play.google.com/store/apps/details?id=" + getActivity().getPackageName() ,context);
                });

            }else if(getItemViewType(i)==telegram_channel_row){

                SettingItemHolder settingItemHolder = (SettingItemHolder) viewHolder;
                settingItemHolder.title.setText(ApplicationLoader.getStr(R.string.menu_join_telegram_channel));
                settingItemHolder.icon.setImageResource(R.drawable.ic_telegeram_apps);
                settingItemHolder.itemView.setOnClickListener(v -> AndroidHelper.share("https://t.me/bzappsgroup",context));



            }else if(getItemViewType(i)==logoutRow){

                SettingItemHolder settingItemHolder = (SettingItemHolder) viewHolder;
                settingItemHolder.title.setText(ApplicationLoader.getStr(R.string.logout));
                settingItemHolder.icon.setImageResource(R.drawable.ic_logout);


                settingItemHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppPreference.logout(getActivity());
                    }
                });

            }


        }


        @Override
        public int getItemCount() {
            return rowCount;
        }
    }



}
