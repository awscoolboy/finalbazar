package ir.cafebazar.et.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


import apps.cafebazaar.all.apps.R;
import ir.cafebazar.et.Models.BazaarApps;
import ir.cafebazar.et.network.BaseApiController;


public class SearchActivity extends Activity implements SearchView.OnQueryTextListener {


    private RecyclerView mRecyclerView;
    private SearchResultAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private SearchView searchView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView status;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.search_layout);
        status=findViewById(R.id.status);
        mRecyclerView = findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager=new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new SearchResultAdapter();
        mRecyclerView.setAdapter(mAdapter);
        swipeRefreshLayout=findViewById(R.id.swipeRefresh);

        findViewById(R.id.backButton).setOnClickListener(view -> SearchActivity.super.onBackPressed());

        searchView=findViewById(R.id.search_view);
        searchView.setQueryHint("search for apps");
        searchView.onActionViewExpanded();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                swipeRefreshLayout.setRefreshing(true);
                doSearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

    }



    private void doSearch(String query) {
        BaseApiController.getInstance().searchApps(query, new BaseApiController.ApiCallBack() {
            @Override
            public void didReceiveData( int type,Object... object) {

                if(type== BaseApiController.didReceivedSearchResult){

                    List<BazaarApps.CafeBazaarApp>  bazaarApps=(List<BazaarApps.CafeBazaarApp>)object[0];
                    if(bazaarApps.size()!=0){
                        status.setVisibility(View.GONE);
                        mAdapter.addREsults(bazaarApps);
                        mAdapter.notifyDataSetChanged();

                    }else{
                        status.setVisibility(View.VISIBLE);
                        status.setText("No item found!");

                    }
                    swipeRefreshLayout.setRefreshing(false);

                }
            }

            @Override
            public void onError(String error_message) {

            }
        });

    }



    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }


    public  class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

        private List<BazaarApps.CafeBazaarApp> mDataset = new ArrayList<>();


        public class ViewHolder extends RecyclerView.ViewHolder {

            private ImageView app_icon;
            private TextView app_name;


            public ViewHolder(View itemView) {
                super(itemView);
                app_icon=itemView.findViewById(R.id.app_logo);
                app_name=itemView.findViewById(R.id.app_title);

                itemView.setOnClickListener(view -> {

                    Intent intent=new Intent(SearchActivity.this, AppDetailActivity.class);
                    intent.putExtra("cafe_app",mDataset.get(getAdapterPosition()));
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(SearchActivity.this,app_icon, "profile");

                    startActivity(intent, options.toBundle());


                });
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public SearchResultAdapter() {
        }


        // Create new views (invoked by the layout manager)
        @Override
        public SearchResultAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                 int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item, parent,false);
            return new ViewHolder(v);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final BazaarApps.CafeBazaarApp recipe = mDataset.get(position);

            holder.app_name.setText(foramtString(recipe.getName()));
            Glide.with(SearchActivity.this).load(BaseApiController.base_url + "media/"+ recipe.getIcon()).apply(RequestOptions.circleCropTransform()).into(holder.app_icon);


        }

        @Override
        public int getItemCount() {
            return mDataset.size();
        }

        public void addResult(BazaarApps.CafeBazaarApp recipe) {
            mDataset.add(recipe);
        }

        public void addREsults(List<BazaarApps.CafeBazaarApp> apps){
            clearResults();
            mDataset.addAll(apps);
        }

        public void clearResults() {
            mDataset.clear();
        }
    }



    private String foramtString(String data){

        if(data==null)return "";

        String str = "";
        try {
            str = new String(data.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();
        }
        String decodedStr = Html.fromHtml(str).toString();
        return decodedStr;
    }


}