package ir.cafebazar.et.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.Iterator;
import java.util.List;


import apps.cafebazaar.all.apps.R;
import ir.cafebazar.et.ApplicationLoader;
import ir.cafebazar.et.Models.BazaarApps;
import ir.cafebazar.et.Models.Categories;
import ir.cafebazar.et.Models.KeyValue;
import ir.cafebazar.et.database.AppDao;
import ir.cafebazar.et.database.DatabaseHandler;
import ir.cafebazar.et.Models.Developer;
import ir.cafebazar.et.Models.Play.PlayApp;
import ir.cafebazar.et.Models.Play.PlayCategory;
import ir.cafebazar.et.Models.Play.PlayFilter;
import ir.cafebazar.et.Models.Recommended;
import ir.cafebazar.et.Models.SubCategories;
import ir.cafebazar.et.Models.collections.CollApp;
import ir.cafebazar.et.Models.collections.Collections;
import ir.cafebazar.et.Models.collections.Subcollection;
import ir.cafebazar.et.util.AppPreference;



public class BaseApiController{

    private static int totalEvents = 1;

    public static String base_url="http://178.128.89.64:800/";

    private static final long play_app_update_threshold=3 * 60 * 60 * 1000 ;//3 hour


    private static final long home__update_threshold= 24 * 60 * 60 * 1000;//1 day


    private static final long update_threshold=24 * 60 * 60 * 1000;//1 day

    private static final long apps_update_threshold = 24 * 60 * 60 * 1000;//1 day

    public static final int didReceivedHomeCategories = totalEvents++;
    public static final int didReceivedSubCategories =totalEvents++;
    public static final int didReceivedAppsForSubCategories =totalEvents++;
    public static final int didReceivedGameCategory =totalEvents++;
    public static final int didReceivedAppCategory =totalEvents++;
    public static final int didReceivedHomeSubCatApps =totalEvents++;
    public static final int didReceivedSearchResult =totalEvents++;
    public static final int didReceivePlayCategory =totalEvents++;
    public static final int didReceivePlayFilters=totalEvents++;
    public static final int didReceivedPlayApps=totalEvents++;
    public static final int didReceivedMoreApps=totalEvents++;
    public static final int didReceiveAppByPackageName =totalEvents++;
    public static final int didReceivedDeveloper=totalEvents++;
    public static final int didReceiveDeveloperApps =totalEvents++;
    public static final int didReceiveCollations=totalEvents++;
    public static final int didReceiveSubCollations=totalEvents++;
    public static final int didReceiveRecommendedApps=totalEvents++;
    public static final int didReceiveAdvertisementApps =totalEvents++;
    public static final int didReceiveAppsForSubCollations=totalEvents++;
    public static final int didReceiveProxyList = totalEvents++;

    private static BaseApiController instance;
    private RequestQueue requestQueue;
    private Gson gson;

    public interface ApiCallBack{
        void didReceiveData(int type,Object... object);
        void onError(String error_message);
    }


    private BaseApiController() {
        requestQueue = getRequestQueue();
        gson = new GsonBuilder().create();
    }

    public static synchronized BaseApiController getInstance() {
        if (instance == null) {
            instance = new BaseApiController();
        }
        return instance;
    }

    private RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(ApplicationLoader.applicationContext);
        }
        return requestQueue;
    }

    private <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }


    public void getHomeCategories(final  ApiCallBack callBack){
        AppDao appDao = DatabaseHandler.getInstance().appDao();
        String url=base_url + "home/categories/";
        String key = String.valueOf(url.hashCode());
        long duration  = System.currentTimeMillis() - AppPreference.getLastUpdateTime(didReceivedHomeCategories);
        if(appDao!=null && appDao.getValue(key)!=null && duration<home__update_threshold){
            String  value = appDao.getValue(key).getValue();
            if(value!=null &&  !value.isEmpty()){
                Categories.CatField catField = (new Gson()).fromJson(value, Categories.CatField.class);
                if(callBack!=null){
                    callBack.didReceiveData(didReceivedHomeCategories,catField.getResults());
                    return;
                }
            }
        }
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    Categories.CatField catField = (new Gson()).fromJson(response, Categories.CatField.class);
                    if(callBack!=null){
                        callBack.didReceiveData(didReceivedHomeCategories,catField.getResults());
                        KeyValue keyValue = new KeyValue();
                        keyValue.setKey(key);
                        keyValue.setValue(response);
                        if(appDao!=null){
                            appDao.insertValue(keyValue);
                            AppPreference.setLastUpdateTime(didReceivedHomeCategories);
                        }

                    }
                },
                error -> {
                    if(callBack!=null && error!=null){
                        callBack.onError(error.getMessage());
                    }
                });
        addToRequestQueue(stringRequest);
    }


    public void getHomeSubCategoryApps(String id, String name, final ApiCallBack callBack){

        AppDao appDao = DatabaseHandler.getInstance().appDao();
        String url=base_url + "home/category/" + id;
        String key = String.valueOf(url.hashCode());

        long duration  = System.currentTimeMillis() - AppPreference.getLastUpdateTime(didReceivedHomeSubCatApps);


        if(appDao!=null && appDao.getValue(key)!=null && duration < home__update_threshold){
            String  value = appDao.getValue(key).getValue();
            if(value!=null &&  !value.isEmpty()){
                List<BazaarApps.CafeBazaarApp> subsCatList = (new Gson()).fromJson(value, new TypeToken<List<BazaarApps.CafeBazaarApp>>() {}.getType());
                if(callBack!=null){
                    callBack.didReceiveData(didReceivedHomeSubCatApps, subsCatList,name);
                    return;
                }
            }
        }


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    List<BazaarApps.CafeBazaarApp> subsCatList = (new Gson()).fromJson(response, new TypeToken<List<BazaarApps.CafeBazaarApp>>() {}.getType());
                    if(callBack!=null){
                        callBack.didReceiveData(didReceivedHomeSubCatApps, subsCatList,name);
                        KeyValue keyValue = new KeyValue();
                        keyValue.setKey(key);
                        keyValue.setValue(response);
                        if(appDao!=null){
                            appDao.insertValue(keyValue);
                            AppPreference.setLastUpdateTime(didReceivedHomeSubCatApps);

                        }


                    }
                },
                error -> {
                    if(callBack!=null && error!=null){
                        callBack.onError(error.getMessage());
                    }
                });
        addToRequestQueue(stringRequest);

    }


    public void getCategories(boolean game, final ApiCallBack callBack){
        AppDao appDao = DatabaseHandler.getInstance().appDao();
        String  url= base_url + "categories/?limit=100&offset=0";
        String key = String.valueOf(url.hashCode());
        long duration  = System.currentTimeMillis() - AppPreference.getLastUpdateTime(didReceivedHomeCategories);
        if(appDao!=null && appDao.getValue(key)!=null && duration<home__update_threshold){
            String  value = appDao.getValue(key).getValue();
            if(value!=null &&  !value.isEmpty()){
                Categories.CatField catField = (new Gson()).fromJson(value, Categories.CatField.class);
                List<ir.cafebazar.et.Models.Categories> categoriesList=catField.getResults();
                if(callBack!=null && categoriesList!=null && categoriesList.size()>0){
                    Iterator<ir.cafebazar.et.Models.Categories> iter = categoriesList.iterator();
                    while (iter.hasNext())
                    {
                        Categories cat = iter.next();
                        appDao.insertCategories(cat);
                        if(game){
                            if(cat.getCategory_type().equals("1"))
                            {
                                iter.remove();
                            }
                        }else{
                            if(cat.getCategory_type().equals("2"))
                            {
                                iter.remove();
                            }
                        }
                    }
                    if(game)
                    {
                        callBack.didReceiveData(didReceivedGameCategory, categoriesList);
                    }else {
                        callBack.didReceiveData(didReceivedAppCategory, categoriesList);
                    }


                    return;
                }

            }
        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    response -> {
                    if(response!=null && !response.isEmpty()){

                        Categories.CatField catField = (new Gson()).fromJson(response, Categories.CatField.class);

                        List<ir.cafebazar.et.Models.Categories> categoriesList=catField.getResults();
                        if(callBack!=null && categoriesList!=null && categoriesList.size()>0 && appDao!=null){
                            Iterator<ir.cafebazar.et.Models.Categories> iter = categoriesList.iterator();
                            while (iter.hasNext())
                            {
                                Categories cat = iter.next();
                                appDao.insertCategories(cat);
                                if(game){
                                    if(cat.getCategory_type().equals("1"))
                                    {
                                        iter.remove();
                                    }
                                }else{
                                    if(cat.getCategory_type().equals("2"))
                                    {
                                        iter.remove();
                                    }
                                }
                            }
                            if(game)
                            {
                                callBack.didReceiveData(didReceivedGameCategory, categoriesList);
                            }else {
                                callBack.didReceiveData(didReceivedAppCategory, categoriesList);
                            }
                            KeyValue keyValue = new KeyValue();
                            keyValue.setKey(key);
                            keyValue.setValue(response);
                            appDao.insertValue(keyValue);
                            AppPreference.setLastUpdateTime(didReceivedHomeCategories);
                            return;
                       }
                        return;
                    }
                     if(callBack!=null){
                            callBack.onError(ApplicationLoader.getStr(R.string.unknow_error));
                      }
                    },
                    error -> {
                        if(callBack!=null){

                            if(error!=null && error.getMessage()!=null){
                                callBack.onError(error.getMessage());
                            }
                        }
                    });
            getRequestQueue().add(stringRequest);


    }


    public void getSubCategoriesForMainCategory(String id, final ApiCallBack callBack){

        AppDao appDao = DatabaseHandler.getInstance().appDao();
        String url=base_url + "subcategories/" + id;
        String key = String.valueOf(url.hashCode());

        long duration  = System.currentTimeMillis() - AppPreference.getLastUpdateTime(didReceivedHomeSubCatApps);


        if(appDao!=null && appDao.getValue(key)!=null && duration < home__update_threshold){
            String  value = appDao.getValue(key).getValue();
            if(value!=null &&  !value.isEmpty()){

                List<SubCategories> subsCatList = (new Gson()).fromJson(value, new TypeToken<List<SubCategories>>(){}.getType());
                if(callBack!=null){
                    callBack.didReceiveData(didReceivedSubCategories, subsCatList);
                    return;
                }

            }
        }


        addToRequestQueue(new StringRequest(Request.Method.GET, url,
                response -> {
                    List<SubCategories> subsCatList = (new Gson()).fromJson(response, new TypeToken<List<SubCategories>>(){}.getType());

                    if(callBack!=null){
                        callBack.didReceiveData(didReceivedSubCategories, subsCatList);

                        new Thread(() -> {
                            KeyValue keyValue = new KeyValue();
                            keyValue.setKey(key);
                            keyValue.setValue(response);
                            if(appDao!=null){
                                appDao.insertValue(keyValue);
                                AppPreference.setLastUpdateTime(didReceivedSubCategories);
                            }

                        }).start();
                    }
                },
                error -> {
                    if(callBack!=null && error!=null){
                        callBack.onError(error.getMessage());
                 }
       }));
    }


    public void getAppsForSubCategories(int sub_cat_id,String title,final ApiCallBack callBack){

        String url=base_url + "subcategory/" + sub_cat_id + "/";
        AppDao appDao = DatabaseHandler.getInstance().appDao();
        String key = String.valueOf(url.hashCode());

        long duration  = System.currentTimeMillis() - AppPreference.getLastUpdateTime(didReceivedAppsForSubCategories);


        if(appDao!=null && appDao.getValue(key)!=null && duration < home__update_threshold){
            String  value = appDao.getValue(key).getValue();
            if(value!=null &&  !value.isEmpty()){

                BazaarApps bazarApps=gson.fromJson(value, BazaarApps.class);
                if(callBack!=null){
                    callBack.didReceiveData(didReceivedAppsForSubCategories, bazarApps.getCafeBazaarAppList(),title,bazarApps.getNext());
                    return;
                }

            }
        }


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
               response -> {
                   BazaarApps bazarApps=gson.fromJson(response, BazaarApps.class);
                   if(callBack!=null){
                       callBack.didReceiveData(didReceivedAppsForSubCategories, bazarApps.getCafeBazaarAppList(),title,bazarApps.getNext());

                       new Thread(() -> {
                           KeyValue keyValue = new KeyValue();
                           keyValue.setKey(key);
                           keyValue.setValue(response);
                           if(appDao!=null){
                               appDao.insertValue(keyValue);
                               AppPreference.setLastUpdateTime(didReceivedAppsForSubCategories);
                           }
                       }).start();
                   }
               },
               error -> {
                   if(callBack!=null && error!=null){
                       callBack.onError(error.getMessage());
                   }
               });
       addToRequestQueue(stringRequest);

    }


    public void searchApps(String query,final ApiCallBack callBack){
        String url=base_url + "search/" + query;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {


                    List<BazaarApps.CafeBazaarApp> subsCatList = (new Gson()).fromJson(response, new TypeToken<List<BazaarApps.CafeBazaarApp>>() {}.getType());
                    if(callBack!=null){
                        callBack.didReceiveData(didReceivedSearchResult, subsCatList);
                    }
                },
                error -> {
                    if(callBack!=null && error!=null){
                        callBack.onError(error.getMessage());
                    }
                });
        addToRequestQueue(stringRequest);
    }



    public void getPlayCategories(final ApiCallBack callBack){
        SharedPreferences preferences=ApplicationLoader.applicationContext.getSharedPreferences("main_config",Context.MODE_PRIVATE);
        String playCat=preferences.getString("play_cat","");
        if (playCat != null && !playCat.isEmpty()) {
            PlayCategory playCategory = (new Gson()).fromJson(playCat, PlayCategory.class);
            if (callBack != null) {
                callBack.didReceiveData(didReceivePlayCategory, playCategory);
            }
            return;
        }
        String url=base_url + "rank/categories/";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                     PlayCategory playCategory = (new Gson()).fromJson(response, PlayCategory.class);
                    if(callBack!=null){
                        callBack.didReceiveData(didReceivePlayCategory, playCategory);
                    }
                    updateData(didReceivePlayCategory,response);
                },
                error -> {
                    if(callBack!=null && error!=null){
                        callBack.onError(error.getMessage());
                    }
                });
        addToRequestQueue(stringRequest);
    }


    public void getPlayFilters(final ApiCallBack callBack){
        SharedPreferences preferences=ApplicationLoader.applicationContext.getSharedPreferences("main_config",Context.MODE_PRIVATE);
        String play_filter=preferences.getString("play_filter","");
        if (play_filter != null && !play_filter.isEmpty()) {
            List<PlayFilter> filters = (new Gson()).fromJson(play_filter, new TypeToken<List<PlayFilter>>() {
            }.getType());
            if (callBack != null) {
                callBack.didReceiveData(didReceivePlayFilters, filters);
            }
            return;
        }
        String url=base_url + "rank/filters/";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    List<PlayFilter> filters = (new Gson()).fromJson(response, new TypeToken<List<PlayFilter>>() {}.getType());
                    if(callBack!=null){
                        callBack.didReceiveData(didReceivePlayFilters, filters);
                        updateData(didReceivePlayFilters,response);
                    }
                },
                error -> {
                    if(callBack!=null && error!=null){
                        callBack.onError(error.getMessage());
                    }
                });
        addToRequestQueue(stringRequest);
    }




    public void getPlayApps(String categoryID,String filterID,final  ApiCallBack callBack){

        String url= base_url +"rank/apps/" + filterID + "/" + categoryID;
        AppDao appDao = DatabaseHandler.getInstance().appDao();
        String key = String.valueOf(url.hashCode());
        long duration  = System.currentTimeMillis() - AppPreference.getLastUpdateTime(didReceivedPlayApps);
        if(appDao!=null && appDao.getValue(key)!=null && duration < play_app_update_threshold){
            String  value = appDao.getValue(key).getValue();
            if(value!=null &&  !value.isEmpty()){
                List<PlayApp> playApps = (new Gson()).fromJson(value, new TypeToken<List<PlayApp>>() {}.getType());
                if(callBack!=null) {
                    callBack.didReceiveData(didReceivedPlayApps, playApps);
                }
            }
        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    List<PlayApp> playApps = (new Gson()).fromJson(response, new TypeToken<List<PlayApp>>() {}.getType());
                    if(callBack!=null){
                        callBack.didReceiveData(didReceivedPlayApps, playApps);

                        new Thread(() -> {
                            KeyValue keyValue = new KeyValue();
                            keyValue.setKey(key);
                            keyValue.setValue(response);
                            if(appDao!=null){
                                appDao.insertValue(keyValue);
                                AppPreference.setLastUpdateTime(didReceivedPlayApps);
                            }

                        }).start();
                    }
                },
                error -> {
                    if(callBack!=null && error!=null){
                        callBack.onError(error.getMessage());
                    }
                });
        addToRequestQueue(stringRequest);
    }


    public void getMoreAppsForCategory(String url,final  ApiCallBack callBack){


        AppDao appDao = DatabaseHandler.getInstance().appDao();
        String key = String.valueOf(url.hashCode());
        long duration  = System.currentTimeMillis() - AppPreference.getLastUpdateTime(didReceivedMoreApps);


        if(appDao!=null && appDao.getValue(key)!=null && duration < home__update_threshold){
            String  value = appDao.getValue(key).getValue();
            if(value!=null &&  !value.isEmpty()){

                BazaarApps bazarApps=gson.fromJson(value, BazaarApps.class);
                if(callBack!=null){
                    callBack.didReceiveData(didReceivedMoreApps, bazarApps.getCafeBazaarAppList(),bazarApps.getNext());
                    return;

                }


            }
        }


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    BazaarApps bazarApps=gson.fromJson(response, BazaarApps.class);
                    if(callBack!=null){

                        callBack.didReceiveData(didReceivedMoreApps, bazarApps.getCafeBazaarAppList(),bazarApps.getNext());
                        new Thread(() -> {
                            KeyValue keyValue = new KeyValue();
                            keyValue.setKey(key);
                            keyValue.setValue(response);
                            if(appDao!=null){
                                appDao.insertValue(keyValue);
                                AppPreference.setLastUpdateTime(didReceivedMoreApps);
                            }

                        }).start();

                    }
                },
                error -> {
                    if(callBack!=null && error!=null){
                        callBack.onError(error.getMessage());
                    }
                });
        addToRequestQueue(stringRequest);
    }


    public void getAppbyPackageName(String packageName,final  ApiCallBack callBack){


        String url=base_url + "app/" + packageName;
        AppDao appDao = DatabaseHandler.getInstance().appDao();
        String key = String.valueOf(url.hashCode());
        long duration  = System.currentTimeMillis() - AppPreference.getLastUpdateTime(didReceiveAppByPackageName);


        if(appDao!=null && appDao.getValue(key)!=null && duration < home__update_threshold){
            String  value = appDao.getValue(key).getValue();
            if(value!=null &&  !value.isEmpty()){

                BazaarApps.CafeBazaarApp cafeBazarApp = (new Gson()).fromJson(value, BazaarApps.CafeBazaarApp.class);
                if(callBack!=null){
                    callBack.didReceiveData(didReceiveAppByPackageName, cafeBazarApp);
                    return;
                }



            }
        }


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url
                ,
                response -> {

                 BazaarApps.CafeBazaarApp cafeBazarApp = (new Gson()).fromJson(response, BazaarApps.CafeBazaarApp.class);
                    if(callBack!=null){
                        callBack.didReceiveData(didReceiveAppByPackageName, cafeBazarApp);
                        new Thread(() -> {
                            KeyValue keyValue = new KeyValue();
                            keyValue.setKey(key);
                            keyValue.setValue(response);
                            if(appDao!=null){
                                appDao.insertValue(keyValue);
                                AppPreference.setLastUpdateTime(didReceiveAppByPackageName);
                            }

                        }).start();
                    }

                },
                error -> {
                    if(callBack!=null && error!=null){
                        callBack.onError(error.getMessage());
                    }
                });
        addToRequestQueue(stringRequest);

    }


    public void getDeveloperById(String id, final  ApiCallBack callBack){

        String url=base_url + "developer/" + id;
        AppDao appDao = DatabaseHandler.getInstance().appDao();
        String key = String.valueOf(url.hashCode());
        long duration  = System.currentTimeMillis() - AppPreference.getLastUpdateTime(didReceivedDeveloper);


        if(appDao!=null && appDao.getValue(key)!=null && duration < home__update_threshold){
            String  value = appDao.getValue(key).getValue();
            if(value!=null &&  !value.isEmpty()){

                Developer developer = (new Gson()).fromJson(value, Developer.class);
                if(callBack!=null){
                    callBack.didReceiveData(didReceivedDeveloper, developer);
                    return;
                }


            }
        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url
                ,
                response -> {

                    Developer developer = (new Gson()).fromJson(response, Developer.class);
                    if(callBack!=null){
                        callBack.didReceiveData(didReceivedDeveloper, developer);
                        new Thread(() -> {
                            KeyValue keyValue = new KeyValue();
                            keyValue.setKey(key);
                            keyValue.setValue(response);
                            if(appDao!=null){
                                appDao.insertValue(keyValue);
                                AppPreference.setLastUpdateTime(didReceivedDeveloper);
                            }

                        }).start();

                    }

                },
                error -> {
                    if(callBack!=null && error!=null){
                        callBack.onError(error.getMessage());
                    }
                });
        addToRequestQueue(stringRequest);

    }


    public void getAppsByDeveloper(String id, final  ApiCallBack callBack){
        String url=base_url + "developer/"+ id +"/apps";
        AppDao appDao = DatabaseHandler.getInstance().appDao();
        String key = String.valueOf(url.hashCode());
        long duration  = System.currentTimeMillis() - AppPreference.getLastUpdateTime(didReceiveDeveloperApps);


        if(appDao!=null && appDao.getValue(key)!=null && duration < home__update_threshold){
            String  value = appDao.getValue(key).getValue();
            if(value!=null &&  !value.isEmpty()){

                List<BazaarApps.CafeBazaarApp> cafeBazarApps = (new Gson()).fromJson(value, new TypeToken<List<BazaarApps.CafeBazaarApp>>() {}.getType());
                if(callBack!=null){
                    callBack.didReceiveData(didReceiveDeveloperApps, cafeBazarApps);
                    return;
                }


            }
        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    List<BazaarApps.CafeBazaarApp> cafeBazarApps = (new Gson()).fromJson(response, new TypeToken<List<BazaarApps.CafeBazaarApp>>() {}.getType());
                    if(callBack!=null){
                        callBack.didReceiveData(didReceiveDeveloperApps, cafeBazarApps);
                        new Thread(() -> {
                            KeyValue keyValue = new KeyValue();
                            keyValue.setKey(key);
                            keyValue.setValue(response);
                            if(appDao!=null){
                                appDao.insertValue(keyValue);
                                AppPreference.setLastUpdateTime(didReceiveDeveloperApps);
                            }

                        }).start();

                    }
                },
                error -> {
                    if(callBack!=null && error!=null){
                        callBack.onError(error.getMessage());
                    }
                });
        addToRequestQueue(stringRequest);
    }


    public void getSubCollection(final ApiCallBack callBack){
        String url=base_url + "home/subcollections/";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    List<BazaarApps.CafeBazaarApp> subsCatList = (new Gson()).fromJson(response, new TypeToken<List<BazaarApps.CafeBazaarApp>>() {}.getType());
                    if(callBack!=null){
                        callBack.didReceiveData(didReceiveSubCollations, subsCatList);
                    }
                },
                error -> {
                    if(callBack!=null && error!=null){
                        callBack.onError(error.getMessage());
                    }
                });
        addToRequestQueue(stringRequest);


    }

    public void getCollection(final ApiCallBack callBack){

        String url=base_url + "home/collections/";
        AppDao appDao = DatabaseHandler.getInstance().appDao();
        String key = String.valueOf(url.hashCode());

        long duration  = System.currentTimeMillis() - AppPreference.getLastUpdateTime(didReceiveCollations);

        if(appDao!=null && appDao.getValue(key)!=null && duration < home__update_threshold){
            String  value = appDao.getValue(key).getValue();
            if(value!=null &&  !value.isEmpty()){
                Collections collection = (new Gson()).fromJson(value, Collections.class);
                if(callBack!=null){
                    callBack.didReceiveData(didReceiveCollations, collection);
                    return;
                }
            }
        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {

            Collections collection = (new Gson()).fromJson(response, Collections.class);
                    if(callBack!=null){
                        callBack.didReceiveData(didReceiveCollations, collection);
                        KeyValue keyValue = new KeyValue();
                        keyValue.setKey(key);
                        keyValue.setValue(response);
                        if(appDao!=null)
                            appDao.insertValue(keyValue);

                    }
                },
                error -> {
                    if(callBack!=null && error!=null){
                        callBack.onError(error.getMessage());
                    }
                });
        addToRequestQueue(stringRequest);

    }


    public void getSubCollForCollId(String id,String name,final ApiCallBack callBack){

        String url=base_url + "home/subcollections/" + id;
        AppDao appDao = DatabaseHandler.getInstance().appDao();
        String key = String.valueOf(url.hashCode());
        long duration  = System.currentTimeMillis() - AppPreference.getLastUpdateTime(didReceiveSubCollations);
        if(appDao!=null && appDao.getValue(key)!=null && duration < home__update_threshold){
            String  value = appDao.getValue(key).getValue();
            if(value!=null &&  !value.isEmpty()){
                List<Subcollection> collection = (new Gson()).fromJson(value, new TypeToken<List<Subcollection>>() {}.getType());
                if(callBack!=null){
                    callBack.didReceiveData(didReceiveSubCollations,name, collection);
                    return;
                }
            }
        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    List<Subcollection> collection = (new Gson()).fromJson(response, new TypeToken<List<Subcollection>>() {}.getType());

                    if(callBack!=null){
                        callBack.didReceiveData(didReceiveSubCollations,name, collection);

                        new Thread(() -> {
                            KeyValue keyValue = new KeyValue();
                            keyValue.setKey(key);
                            keyValue.setValue(response);
                            if(appDao!=null){
                                appDao.insertValue(keyValue);
                                AppPreference.setLastUpdateTime(didReceiveSubCollations);
                            }

                        }).start();
                    }
                },
                error -> {
                    if(callBack!=null && error!=null){
                        callBack.onError(error.getMessage());
                    }
                });
        addToRequestQueue(stringRequest);
    }


    public void getAppsForSubCollId(String id,final ApiCallBack callBack){

        String url=base_url + "home/subcollection/" + id;
        AppDao appDao = DatabaseHandler.getInstance().appDao();
        String key = String.valueOf(url.hashCode());
        long duration  = System.currentTimeMillis() - AppPreference.getLastUpdateTime(didReceiveAppsForSubCollations);
        if(appDao!=null && appDao.getValue(key)!=null && duration < home__update_threshold){
            String  value = appDao.getValue(key).getValue();
            if(value!=null &&  !value.isEmpty()){
                List<CollApp> collApps = (new Gson()).fromJson(value, new TypeToken<List<CollApp>>() {}.getType());
                if(callBack!=null){
                    callBack.didReceiveData(didReceiveAppsForSubCollations, collApps );
                    return;
                }
            }
        }


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    List<CollApp> collApps = (new Gson()).fromJson(response, new TypeToken<List<CollApp>>() {}.getType());
                    if(callBack!=null){
                        callBack.didReceiveData(didReceiveAppsForSubCollations, collApps );
                        new Thread(() -> {
                            KeyValue keyValue = new KeyValue();
                            keyValue.setKey(key);
                            keyValue.setValue(response);
                            if(appDao!=null){
                                appDao.insertValue(keyValue);
                                AppPreference.setLastUpdateTime(didReceiveAppsForSubCollations);
                            }

                        }).start();

                    }
                },
                error -> {
                    if(callBack!=null && error!=null){
                        callBack.onError(error.getMessage());
                    }
                });
        addToRequestQueue(stringRequest);
    }



    public void getRecommendedApps(final ApiCallBack callBack){
        String url=base_url + "myapps/recom/";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    List<Recommended> recommendeds = (new Gson()).fromJson(response, new TypeToken<List<Recommended>>() {}.getType());
                    if(callBack!=null){
                        callBack.didReceiveData(didReceiveRecommendedApps, recommendeds );
                    }
                },
                error -> {
                    if(callBack!=null && error!=null){
                        callBack.onError(error.getMessage());
                    }
                });
        addToRequestQueue(stringRequest);

    }


    public void getAdverizmentApps(final ApiCallBack callBack){
        String url=base_url + "myapps/ad/";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    List<Recommended> subsCatList = (new Gson()).fromJson(response, new TypeToken<List<Recommended>>() {}.getType());
                    if(callBack!=null){
                        callBack.didReceiveData(didReceiveAdvertisementApps, subsCatList);
                    }
                },
                error -> {
                    if(callBack!=null && error!=null){
                        callBack.onError(error.getMessage());
                    }
                });
        addToRequestQueue(stringRequest);

    }

    private void updateData(int type,Object data){
        if(data==null)return;
        SharedPreferences appPref= ApplicationLoader.applicationContext.getSharedPreferences("main_config",Context.MODE_PRIVATE);
        if(type== didReceivedGameCategory || type== didReceivedAppCategory){
            appPref.edit().putString("cat",data.toString()).apply();
        }else if(type== didReceivePlayCategory){
            appPref.edit().putString("play_cat",data.toString()).apply();
        }else if(type==didReceivePlayFilters){
            appPref.edit().putString("play_filter",data.toString()).apply();
        }
    }
}