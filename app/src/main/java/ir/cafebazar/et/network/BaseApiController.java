package ir.cafebazar.et.network;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import ir.cafebazar.et.ApplicationLoader;
import ir.cafebazar.et.bazar.models.App;
import ir.cafebazar.et.bazar.models.AppDetials;
import ir.cafebazar.et.bazar.models.AppRow;
import ir.cafebazar.et.bazar.models.Category;
import ir.cafebazar.et.bazar.models.Gener;



public class BaseApiController{


    private static String THEMES_API = "https://telegramnews-a456a.firebaseio.com/themes.json";
    private static String STICKERS_API = "https://telegramnews-a456a.firebaseio.com/stickers.json";
    private static String GEBETA_API = "https://hulugram-beta.firebaseio.com/mobilecredit.json";

    private static String GAMEE_API = "https://api.service.gameeapp.com/";
    private static String useragent ="Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36";
    private static String Referer = "https://cafebazaar.ir/?l=en";
    private static String Origin = "https://cafebazaar.ir";
    private static String feachMode="cors";
    private static String contentType = "application/json;charset=UTF-8";
    public  static String BazarImageLink = "https://s.cafebazaar.ir/1/icons/{**}_128x128.webp";
    private static String bazarEndPoint = "https://api.cafebazaar.ir/rest-v1/process";
    private static String webDomain = "https://cafebazaar.ir/cat/{**}?l=en";
    public  static String base_url="http://159.65.94.189:80/";
    private static String proxyLink = "https://telegramnews-a456a.firebaseio.com/proxy.json";
    public  static String socks_5_proxy_api ="https://www.proxy-list.download/api/v1/get?type=socks5&anon=elite";
    private static String locationApi = "http://ip-api.com/json/";
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";
    private static String TRANSLATE_URL = "https://translate.googleapis.com/translate_a/single?client=gtx&sl=auto&tl={**}&dt=t&q=";




    private static int totalEvents = 1;
    public static final int didReceiveProxy = totalEvents++;
    public static final int didReceiveSocks5proxy = totalEvents++;
    public static final int diReceiveLocationData = totalEvents++;
    public static final int didReceiveMore = totalEvents++;
    public static final int didReceiveEndReached = totalEvents++;
    public static final int didReceiveSearchResult = totalEvents++;
    public static final int didReceiveGame = totalEvents++;
    public static final int didiReceiveBazaarCategories = totalEvents++;
    public static final int didReceiveApp = totalEvents++;
    public static final int didReceiveThemes =  totalEvents++;
    public static final int didReceiveSticker = totalEvents++;
    public static final int didReceiveMobilePackage = totalEvents++;
    public static final int didReceiveTranslatedText = totalEvents++;


    private ImageLoader mImageLoader;
    private static String uniqueID = null;
    public synchronized static String id() {
        if (uniqueID == null) {
            SharedPreferences sharedPrefs = ApplicationLoader.applicationContext.getSharedPreferences(
                    PREF_UNIQUE_ID, Context.MODE_PRIVATE);
            uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);
            if (uniqueID == null) {
                uniqueID = UUID.randomUUID().toString();
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString(PREF_UNIQUE_ID, uniqueID);
                editor.commit();
            }
        }
        return uniqueID;
    }

    public interface ApiCallBack{
        void didReceiveData(int type, Object... object);
        void onError(String error_message);
    }

    private static BaseApiController instance;
    private RequestQueue requestQueue;
    private Gson gson;

    private BaseApiController() {
        requestQueue = getRequestQueue();
        gson = new GsonBuilder().create();

        mImageLoader = new ImageLoader(requestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<>(20);
                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }
                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    public static synchronized BaseApiController getInstance() {
        if (instance == null) {
            instance = new BaseApiController();
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(ApplicationLoader.applicationContext);
        }
        return requestQueue;
    }

    private <T> void addToRequestQueue(Request<T> req,String tag) {
        req.setTag(tag);
        req.setShouldCache(false);
        getRequestQueue().add(req);
    }

    public void getProxysFromFirebase(final  ApiCallBack callBack){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, proxyLink,
                response -> {
                    if(callBack!=null){

                        List<String> proxyLinkList = (new Gson()).fromJson(response, new TypeToken<List<String>>(){}.getType());
                        callBack.didReceiveData(didReceiveProxy, proxyLinkList);

                    }
                },
                error -> {
                    if(callBack!=null && error!=null){
                        callBack.onError(error.getMessage());
                    }
                });
        addToRequestQueue(stringRequest,proxyLink);
    }

    public void getThemesFromFirebase(final  ApiCallBack callBack){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, THEMES_API,
                response -> {
                    if(callBack!=null){
                        HashMap<String ,String> themes = (new Gson()).fromJson(response, new TypeToken<HashMap<String,String>>(){}.getType());
                        callBack.didReceiveData(didReceiveThemes, themes);
                    }
                },
                error -> {
                    if(callBack!=null && error!=null){
                        callBack.onError(error.getMessage());
                    }
                });
        addToRequestQueue(stringRequest,THEMES_API);
    }



    public void getSocks5ProxyList(final  ApiCallBack callBack){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, socks_5_proxy_api, response -> {
            if(callBack!=null){
                String [] data= response.split("\n");
                if(data.length >=1){
                    List<String> proxyLinkList = Arrays.asList(data);

                    callBack.didReceiveData(didReceiveSocks5proxy,proxyLinkList);
                }


            }
        },
                error -> {
                    if(callBack!=null && error!=null){
                        callBack.onError(error.getMessage());
                    }
                });
        addToRequestQueue(stringRequest,socks_5_proxy_api);
    }

    public void getLocationDataByIp(String ip,final ApiCallBack callBack){
        String url = locationApi + ip;
        Log.i("response",url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            Log.i("response",response);
            if(callBack!=null){
                callBack.didReceiveData(diReceiveLocationData,response);
            }
        },
                error -> {
                    if(callBack!=null && error!=null){
                        callBack.onError(error.getMessage());
                    }
                });
        addToRequestQueue(stringRequest,url);

    }

    public void getBazaarCategories(boolean isGame, final ApiCallBack callBack){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, bazarEndPoint, response -> {
            if(callBack!=null){
                Gson gson = new Gson();
                try {
                    ArrayList<Category> categoryArrayList = new ArrayList<>();
                    JSONObject jsonObject =new JSONObject(response);
                    JSONArray categories = jsonObject.getJSONObject("singleReply").getJSONObject("getCategoryListSecureReply").getJSONArray("categories");
                    for(int i = 0;i<categories.length();i++){
                        JSONObject catObject = categories.getJSONObject(i);
                        Category category = gson.fromJson(catObject.toString(),Category.class);
                         if(isGame){
                             if(category.isGame()){
                                 categoryArrayList.add(category);
                             }
                         }else{
                             if(!category.isGame()){
                                 categoryArrayList.add(category);

                             }

                         }
                        categoryArrayList.add(category);
                    }
                    callBack.didReceiveData(didiReceiveBazaarCategories,categoryArrayList);

                }catch (Exception ignore){

                }
            }
        },
                error -> {
                    if(callBack!=null && error!=null){
                        callBack.onError(error.getMessage());
                    }
                }){

            @Override
            public Map<String, String> getHeaders(){
                Map<String, String> header = new HashMap<>();
                header.put("Content-Type",contentType);
                header.put("User-Agent",useragent);
                header.put("Referer",Referer);
                header.put("Sec-Fetch-Mode",feachMode);
                header.put("Origin",Origin);
                return header;
            }

            @Override
            public byte[] getBody() {
                String langNumber= "";
                String langCode = "";

                langNumber = "2";
                langCode = "fa";
                String  payload ="{\"" +
                        "properties\":{\"" +
                        "language\":"+langNumber +",\"" +
                        "clientID\":\""+ id()+"\",\"" +
                        "deviceID\":\""+id()+"\",\"" +
                        "clientVersion\":\"web\"},\"" +
                        "singleRequest\":{\"" +
                        "GetCategoryListSecureRequest\":{\"" +
                        "path\":\"videos-home\",\"" +
                        "offset\":0,\"" +
                        "language\":\""+langCode+"\"}}}";
                return payload.getBytes();
            }
        };

        addToRequestQueue(stringRequest,"categories");

    }

    public void getAppsForCategories(String path,final ApiCallBack callBack){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, bazarEndPoint, response -> {
            if(callBack!=null){
                Gson gson = new Gson();
                try {

                    ArrayList<AppRow> categoryArrayList = new ArrayList<>();
                    JSONObject jsonObject =new JSONObject(response);
                    JSONArray categories = jsonObject.getJSONObject("singleReply").getJSONObject("getPageReply").getJSONArray("rows");
                    for(int i = 0;i<categories.length();i++){
                        JSONObject catObject = categories.getJSONObject(i);
                        if(catObject.has("appRow")){
                            JSONObject appRows = catObject.getJSONObject("appRow");
                            AppRow row = gson.fromJson(appRows.toString(),AppRow.class);
                            row.setPos(i);
                            categoryArrayList.add(row);
                        }

                    }
                    callBack.didReceiveData(10,categoryArrayList);

                }catch (Exception ignore){

                }
            }
        },
                error -> {
                    if(callBack!=null && error!=null){
                        callBack.onError(error.getMessage());
                    }
                }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();

                header.put("Content-Type",contentType);
                header.put("User-Agent",useragent);
                header.put("Referer",webDomain);
                header.put("Sec-Fetch-Mode",feachMode);
                header.put("Origin",Origin);
                return header;
            }




            @Override
            public byte[] getBody() throws AuthFailureError {

                String langNumber= "";
                String langCode = "";


                langNumber = "1";
                langCode = "en";
                String  payload ="{\"" +
                        "properties\":{\"" +
                        "language\":" +langNumber +  ",\"" +
                        "clientID\":\""+ id()+"\",\"" +
                        "deviceID\":\""+id()+"\",\"" +
                        "clientVersion\":\"web\"},\"" +
                        "singleRequest\":{\"" +
                        "getPageRequest\":{\"" +
                        "path\":\""+path+"\",\"" +
                        "offset\":0,\"" +
                        "language\":\""+langCode+"\"}}}";

                Log.i("CATEGORY_p",payload);


                return payload.getBytes();
            }
        };

        addToRequestQueue(stringRequest,path);

    }


    public void getMoreApps(String slug,int offset,final ApiCallBack callBack){
        if(slug.contains("page?slug=")){
            slug = slug.replace("page?slug=","");
        }
        String finalSlug = slug;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, bazarEndPoint, response -> {
            if(callBack!=null){
                Gson gson = new Gson();
                try {
                    Log.i("hasmoremore","get mroe resulst come back");

                    ArrayList<App> categoryArrayList = new ArrayList<>();
                    JSONObject jsonObject =new JSONObject(response);
                    JSONArray categories = jsonObject.getJSONObject("singleReply").getJSONObject("getPageReply").getJSONArray("contentList");

                    if(categories.length()==0){
                        Log.i("hasmoremore","catagoreis lenths is  0");

                        callBack.didReceiveData(didReceiveEndReached,categoryArrayList);
                        return;
                    }
                    Log.i("hasmoremore","catagoreis lenths is " + categories.length());


                    for(int i = 0;i<categories.length();i++){
                        JSONObject catObject = categories.getJSONObject(i);
                        JSONObject appRows = catObject.getJSONObject("layoutApp");
                        App row = gson.fromJson(appRows.toString(), App.class);
                        categoryArrayList.add(row);
                    }
                    callBack.didReceiveData(didReceiveMore,categoryArrayList);

                }catch (Exception ignore){
                    Log.i("hasmoremore","ignor eon soer array");

                    callBack.didReceiveData(didReceiveEndReached,"");



                }
            }
        },
                error -> {
                    if(callBack!=null && error!=null){
                        callBack.onError(error.getMessage());
                        //  Log.i("googleme","resposne=> " + error.networkResponse.statusCode);

                    }
                }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> header = new HashMap<>();
                header.put("Content-Type",contentType);
                header.put("User-Agent",useragent);
                header.put("Referer",webDomain);
                header.put("Sec-Fetch-Mode",feachMode);
                header.put("Origin",Origin);

                return header;
            }


            @Override
            public byte[] getBody() throws AuthFailureError {


                String langNumber= "";
                String langCode = "";

                langNumber = "2";
                langCode = "fa";
                String  payload ="{\"" +
                        "properties\":{\"" +
                        "language\":"+langNumber+",\"" +
                        "clientID\":\""+ id()+"\",\"" +
                        "deviceID\":\""+id()+"\",\"" +
                        "clientVersion\":\"web\"},\"" +
                        "singleRequest\":{\"" +
                        "getPageRequest\":{\"" +
                        "path\":\""+ finalSlug +"\",\"" +
                        "offset\":"+offset+",\"" +
                        "language\":\""+langCode+"\"}}}";

                Log.i("googleme", payload);


                return payload.getBytes();
            }
        };

        addToRequestQueue(stringRequest,slug);

    }


    public void getApp(String packageName,final ApiCallBack callBack){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, bazarEndPoint, response -> {
            if(callBack!=null){
                Gson gson = new Gson();
                try {
                    AppDetials appDetials = null;
                    JSONObject jsonObject =new JSONObject(response);
                    JSONObject appObject = jsonObject.getJSONObject("singleReply").getJSONObject("appDetailsReply");
                    appDetials = gson.fromJson(appObject.toString(),AppDetials.class);
                    callBack.didReceiveData(didReceiveApp,appDetials);

                }catch (Exception ignore){
                    callBack.onError("");
                }
            }
        },
                error -> {
                    if(callBack!=null && error!=null){
                        callBack.onError(error.getMessage());
                    }
                }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Content-Type",contentType);
                header.put("User-Agent",useragent);
                header.put("Referer",webDomain);
                header.put("Sec-Fetch-Mode",feachMode);
                header.put("Origin",Origin);
                return header;
            }


            @Override
            public byte[] getBody() throws AuthFailureError {
                String langNumber= "";
                String langCode = "";

                langNumber = "2";
                langCode = "fa";
                String  payload ="{\"" +
                        "properties\":{\"" +
                        "language\":"+langNumber+",\"" +
                        "clientID\":\""+ id()+"\",\"" +
                        "deviceID\":\""+id()+"\",\"" +
                        "clientVersion\":\"web\"},\"" +
                        "singleRequest\":{\"" +
                        "appDetailsRequest\":{\"" +
                        "packageName\":"+ "\"" + packageName.trim()+ "\"" +",\"" +
                        "language\":\""+langCode+"\"}}}";
                return payload.getBytes();

            }
        };
        addToRequestQueue(stringRequest,packageName);
    }

    public void searchBazaar(String searchTerm, final ApiCallBack callBack){
        String offset = "0";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, bazarEndPoint, response -> {
            if(callBack!=null){
                ArrayList<App> searchReuslt= new ArrayList<>();

                Gson gson = new Gson();
                try {


                    JSONObject jsonObject =new JSONObject(response);
                    JSONArray appObject = jsonObject.getJSONObject("singleReply").getJSONObject("searchReply").getJSONArray("appList");
                    for(int i=0;i<appObject.length();i++){
                        JSONObject curObject = appObject.getJSONObject(i);

                        if(curObject.has("app")){
                            Log.i("appmeCur","yes it has app");
                            App app = gson.fromJson(curObject.getJSONObject("app").toString(),App.class);
                            Log.i("appmeCur","yes ifadfaft has app");
                            searchReuslt.add(app);

                        }
                    }
                    callBack.didReceiveData(didReceiveSearchResult,searchReuslt);

                }catch (Exception ignore){
                    Log.i("appme",ignore.getMessage() + " message");
                    if(searchReuslt.isEmpty()){
                        callBack.didReceiveData(didReceiveSearchResult,searchReuslt);

                    }

                }




            }
        },
                error -> {
                    if(callBack!=null && error!=null){

                        Log.i("hellomotherfucker",  error.networkResponse.statusCode + "stustc code");

                        callBack.onError(error.getMessage());
                    }
                }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Content-Type",contentType);
                header.put("User-Agent",useragent);
                header.put("Referer",webDomain);
                header.put("Sec-Fetch-Mode",feachMode);
                header.put("Origin",Origin);

                return header;
            }


            @Override
            public byte[] getBody() throws AuthFailureError {

                String langNumber= "";
                String langCode = "fa";


                langNumber = "2";
                langCode = "fa";

                String  payload ="{\"" +
                        "properties\":{\"" +
                        "language\":"+langNumber+",\"" +
                        "clientID\":\""+ id()+"\",\"" +
                        "deviceID\":\""+id()+"\",\"" +
                        "clientVersion\":\"web\"},\"" +
                        "singleRequest\":{\"" +
                        "searchRequest\":{\"" +
                        "query\":\""+ searchTerm.trim() +"\",\"" +
                        "offset\":"+offset+",\"" +
                        "language\":\""+langCode+"\"}}}";

                return payload.getBytes();
            }
        };

        addToRequestQueue(stringRequest,searchTerm);

    }


    public void getGameGenera(final  ApiCallBack callBack){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, GAMEE_API, response -> {
            if(callBack!=null){
                Gson gson = new Gson();
                try {
                    ArrayList<Gener> geners = new ArrayList<>();
                    JSONArray resarray = new JSONObject(response).getJSONObject("result").getJSONArray("genres");
                    for(int i = 0;i < resarray.length(); i++){
                        Gener gener = gson.fromJson(resarray.getJSONObject(i).toString(),Gener.class);
                        geners.add(gener);
                    }
                    callBack.didReceiveData(didReceiveGame,geners);

                }catch (Exception ignore){
                }
            }
        },
                error -> {
                    if(callBack!=null && error!=null){
                        callBack.onError(error.getMessage());

                    }
                }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put(":authority","api.service.gameeapp.com");
                header.put("client-language","en");
                header.put("content-type","text/plain;charset=UTF-8");
                header.put("referer","https://www.gamee.com/games");
                header.put("origin","https://www.gamee.com");
                header.put("sec-fetch-mode","cors");
                header.put("sec-fetch-site","cross-site");
                header.put("user-agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.70 Safari/537.36");
                header.put("x-install-uuid","2b3a5eb3-335d-4895-8370-86335d548ec6\n");
                return header;
            }


            @Override
            public byte[] getBody() throws AuthFailureError {

                String   payload = "{\"jsonrpc\":\"2.0\",\"id\":\"genre.getAllWithGames\",\"method\":\"genre.getAllWithGames\",\"params\":{\"gameCount\":100}}";
                return payload.getBytes();
            }
        };

        addToRequestQueue(stringRequest,"");
    }






}












































//package ir.cafebazar.et.network;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.util.Log;
//
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.toolbox.StringRequest;
//import com.android.volley.toolbox.Volley;
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.google.gson.reflect.TypeToken;
//
//import java.util.Iterator;
//import java.util.List;
//
//
//import apps.cafebazaar.all.apps.R;
//import ir.cafebazar.et.ApplicationLoader;
//import ir.cafebazar.et.Models.BazaarApps;
//import ir.cafebazar.et.Models.Categories;
//import ir.cafebazar.et.Models.KeyValue;
//import ir.cafebazar.et.database.AppDao;
//import ir.cafebazar.et.database.DatabaseHandler;
//import ir.cafebazar.et.Models.Developer;
//import ir.cafebazar.et.Models.Play.PlayApp;
//import ir.cafebazar.et.Models.Play.PlayCategory;
//import ir.cafebazar.et.Models.Play.PlayFilter;
//import ir.cafebazar.et.Models.Recommended;
//import ir.cafebazar.et.Models.SubCategories;
//import ir.cafebazar.et.Models.collections.CollApp;
//import ir.cafebazar.et.Models.collections.Collections;
//import ir.cafebazar.et.Models.collections.Subcollection;
//import ir.cafebazar.et.util.AppPreference;
//
//
//
//public class BaseApiController{
//
//    private static int totalEvents = 1;
//
//
//    public static String base_url="http://178.128.89.64:800/";
//
//    private static final long play_app_update_threshold=3 * 60 * 60 * 1000 ;//3 hour
//
//
//    private static final long home__update_threshold= 24 * 60 * 60 * 1000;//1 day
//
//
//    private static final long update_threshold=24 * 60 * 60 * 1000;//1 day
//
//    private static final long apps_update_threshold = 24 * 60 * 60 * 1000;//1 day
//
//    public static final int didReceivedHomeCategories = totalEvents++;
//    public static final int didReceivedSubCategories =totalEvents++;
//    public static final int didReceivedAppsForSubCategories =totalEvents++;
//    public static final int didReceivedGameCategory =totalEvents++;
//    public static final int didReceivedAppCategory =totalEvents++;
//    public static final int didReceivedHomeSubCatApps =totalEvents++;
//    public static final int didReceivedSearchResult =totalEvents++;
//    public static final int didReceivePlayCategory =totalEvents++;
//    public static final int didReceivePlayFilters=totalEvents++;
//    public static final int didReceivedPlayApps=totalEvents++;
//    public static final int didReceivedMoreApps=totalEvents++;
//    public static final int didReceiveAppByPackageName =totalEvents++;
//    public static final int didReceivedDeveloper=totalEvents++;
//    public static final int didReceiveDeveloperApps =totalEvents++;
//    public static final int didReceiveCollations=totalEvents++;
//    public static final int didReceiveSubCollations=totalEvents++;
//    public static final int didReceiveRecommendedApps=totalEvents++;
//    public static final int didReceiveAdvertisementApps =totalEvents++;
//    public static final int didReceiveAppsForSubCollations=totalEvents++;
//
//    public static final int didReceiveProxyList = totalEvents++;
//
////    public static final int didReciveChannels = totalEvents++;
////    public static final int didReciveStickers = totalEvents++;
////    public static final int didRecveAnimatedSticker = totalEvents++;
//
//
//
//    private static BaseApiController instance;
//    private RequestQueue requestQueue;
//    private Gson gson;
//
//    public interface ApiCallBack{
//        void didReceiveData(int type,Object... object);
//        void onError(String error_message);
//    }
//
//
//    private BaseApiController() {
//        requestQueue = getRequestQueue();
//        gson = new GsonBuilder().create();
//    }
//
//    public static synchronized BaseApiController getInstance() {
//        if (instance == null) {
//            instance = new BaseApiController();
//        }
//        return instance;
//    }
//
//    private RequestQueue getRequestQueue() {
//        if (requestQueue == null) {
//            requestQueue = Volley.newRequestQueue(ApplicationLoader.applicationContext);
//        }
//        return requestQueue;
//    }
//
//    private <T> void addToRequestQueue(Request<T> req) {
//        getRequestQueue().add(req);
//    }
//
//
//    public void getHomeCategories(final  ApiCallBack callBack){
//        AppDao appDao = DatabaseHandler.getInstance().appDao();
//        String url=base_url + "home/categories/";
//        String key = String.valueOf(url.hashCode());
//        long duration  = System.currentTimeMillis() - AppPreference.getLastUpdateTime(didReceivedHomeCategories);
//        if(appDao!=null && appDao.getValue(key)!=null && duration<home__update_threshold){
//            String  value = appDao.getValue(key).getValue();
//            if(value!=null &&  !value.isEmpty()){
//                Categories.CatField catField = (new Gson()).fromJson(value, Categories.CatField.class);
//                if(callBack!=null){
//                    callBack.didReceiveData(didReceivedHomeCategories,catField.getResults());
//                    return;
//                }
//            }
//        }
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                response -> {
//                    Categories.CatField catField = (new Gson()).fromJson(response, Categories.CatField.class);
//                    if(callBack!=null){
//                        callBack.didReceiveData(didReceivedHomeCategories,catField.getResults());
//                        KeyValue keyValue = new KeyValue();
//                        keyValue.setKey(key);
//                        keyValue.setValue(response);
//                        if(appDao!=null){
//                            appDao.insertValue(keyValue);
//                            AppPreference.setLastUpdateTime(didReceivedHomeCategories);
//                        }
//
//                    }
//                },
//                error -> {
//                    if(callBack!=null && error!=null){
//                        callBack.onError(error.getMessage());
//                    }
//                });
//        addToRequestQueue(stringRequest);
//    }
//
//
//    public void getHomeSubCategoryApps(String id, String name, final ApiCallBack callBack){
//
//        AppDao appDao = DatabaseHandler.getInstance().appDao();
//        String url=base_url + "home/category/" + id;
//        String key = String.valueOf(url.hashCode());
//
//        long duration  = System.currentTimeMillis() - AppPreference.getLastUpdateTime(didReceivedHomeSubCatApps);
//
//
//        if(appDao!=null && appDao.getValue(key)!=null && duration < home__update_threshold){
//            String  value = appDao.getValue(key).getValue();
//            if(value!=null &&  !value.isEmpty()){
//                List<BazaarApps.CafeBazaarApp> subsCatList = (new Gson()).fromJson(value, new TypeToken<List<BazaarApps.CafeBazaarApp>>() {}.getType());
//                if(callBack!=null){
//                    callBack.didReceiveData(didReceivedHomeSubCatApps, subsCatList,name);
//                    return;
//                }
//            }
//        }
//
//
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                response -> {
//                    List<BazaarApps.CafeBazaarApp> subsCatList = (new Gson()).fromJson(response, new TypeToken<List<BazaarApps.CafeBazaarApp>>() {}.getType());
//                    if(callBack!=null){
//                        callBack.didReceiveData(didReceivedHomeSubCatApps, subsCatList,name);
//                        KeyValue keyValue = new KeyValue();
//                        keyValue.setKey(key);
//                        keyValue.setValue(response);
//                        if(appDao!=null){
//                            appDao.insertValue(keyValue);
//                            AppPreference.setLastUpdateTime(didReceivedHomeSubCatApps);
//
//                        }
//
//
//                    }
//                },
//                error -> {
//                    if(callBack!=null && error!=null){
//                        callBack.onError(error.getMessage());
//                    }
//                });
//        addToRequestQueue(stringRequest);
//
//    }
//
//
//    public void getCategories(boolean game, final ApiCallBack callBack){
//        AppDao appDao = DatabaseHandler.getInstance().appDao();
//        String  url= base_url + "categories/?limit=100&offset=0";
//        String key = String.valueOf(url.hashCode());
//        long duration  = System.currentTimeMillis() - AppPreference.getLastUpdateTime(didReceivedHomeCategories);
//        if(appDao!=null && appDao.getValue(key)!=null && duration<home__update_threshold){
//            String  value = appDao.getValue(key).getValue();
//            if(value!=null &&  !value.isEmpty()){
//                Categories.CatField catField = (new Gson()).fromJson(value, Categories.CatField.class);
//                List<ir.cafebazar.et.Models.Categories> categoriesList=catField.getResults();
//                if(callBack!=null && categoriesList!=null && categoriesList.size()>0){
//                    Iterator<ir.cafebazar.et.Models.Categories> iter = categoriesList.iterator();
//                    while (iter.hasNext())
//                    {
//                        Categories cat = iter.next();
//                        appDao.insertCategories(cat);
//                        if(game){
//                            if(cat.getCategory_type().equals("1"))
//                            {
//                                iter.remove();
//                            }
//                        }else{
//                            if(cat.getCategory_type().equals("2"))
//                            {
//                                iter.remove();
//                            }
//                        }
//                    }
//                    if(game)
//                    {
//                        callBack.didReceiveData(didReceivedGameCategory, categoriesList);
//                    }else {
//                        callBack.didReceiveData(didReceivedAppCategory, categoriesList);
//                    }
//
//
//                    return;
//                }
//
//            }
//        }
//
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                    response -> {
//                    if(response!=null && !response.isEmpty()){
//
//                        Categories.CatField catField = (new Gson()).fromJson(response, Categories.CatField.class);
//
//                        List<ir.cafebazar.et.Models.Categories> categoriesList=catField.getResults();
//                        if(callBack!=null && categoriesList!=null && categoriesList.size()>0 && appDao!=null){
//                            Iterator<ir.cafebazar.et.Models.Categories> iter = categoriesList.iterator();
//                            while (iter.hasNext())
//                            {
//                                Categories cat = iter.next();
//                                appDao.insertCategories(cat);
//                                if(game){
//                                    if(cat.getCategory_type().equals("1"))
//                                    {
//                                        iter.remove();
//                                    }
//                                }else{
//                                    if(cat.getCategory_type().equals("2"))
//                                    {
//                                        iter.remove();
//                                    }
//                                }
//                            }
//                            if(game)
//                            {
//                                callBack.didReceiveData(didReceivedGameCategory, categoriesList);
//                            }else {
//                                callBack.didReceiveData(didReceivedAppCategory, categoriesList);
//                            }
//                            KeyValue keyValue = new KeyValue();
//                            keyValue.setKey(key);
//                            keyValue.setValue(response);
//                            appDao.insertValue(keyValue);
//                            AppPreference.setLastUpdateTime(didReceivedHomeCategories);
//                            return;
//                       }
//                        return;
//                    }
//                     if(callBack!=null){
//                            callBack.onError(ApplicationLoader.getStr(R.string.unknow_error));
//                      }
//                    },
//                    error -> {
//                        if(callBack!=null){
//
//                            if(error!=null && error.getMessage()!=null){
//                                callBack.onError(error.getMessage());
//                            }
//                        }
//                    });
//            getRequestQueue().add(stringRequest);
//
//
//    }
//
//
//    public void getSubCategoriesForMainCategory(String id, final ApiCallBack callBack){
//
//        AppDao appDao = DatabaseHandler.getInstance().appDao();
//        String url=base_url + "subcategories/" + id;
//        String key = String.valueOf(url.hashCode());
//
//        long duration  = System.currentTimeMillis() - AppPreference.getLastUpdateTime(didReceivedHomeSubCatApps);
//
//
//        if(appDao!=null && appDao.getValue(key)!=null && duration < home__update_threshold){
//            String  value = appDao.getValue(key).getValue();
//            if(value!=null &&  !value.isEmpty()){
//
//                List<SubCategories> subsCatList = (new Gson()).fromJson(value, new TypeToken<List<SubCategories>>(){}.getType());
//                if(callBack!=null){
//                    callBack.didReceiveData(didReceivedSubCategories, subsCatList);
//                    return;
//                }
//
//            }
//        }
//
//
//        addToRequestQueue(new StringRequest(Request.Method.GET, url,
//                response -> {
//                    List<SubCategories> subsCatList = (new Gson()).fromJson(response, new TypeToken<List<SubCategories>>(){}.getType());
//
//                    if(callBack!=null){
//                        callBack.didReceiveData(didReceivedSubCategories, subsCatList);
//
//                        new Thread(() -> {
//                            KeyValue keyValue = new KeyValue();
//                            keyValue.setKey(key);
//                            keyValue.setValue(response);
//                            if(appDao!=null){
//                                appDao.insertValue(keyValue);
//                                AppPreference.setLastUpdateTime(didReceivedSubCategories);
//                            }
//
//                        }).start();
//                    }
//                },
//                error -> {
//                    if(callBack!=null && error!=null){
//                        callBack.onError(error.getMessage());
//                 }
//       }));
//    }
//
//
//    public void getAppsForSubCategories(int sub_cat_id,String title,final ApiCallBack callBack){
//
//        String url=base_url + "subcategory/" + sub_cat_id + "/";
//        AppDao appDao = DatabaseHandler.getInstance().appDao();
//        String key = String.valueOf(url.hashCode());
//
//        long duration  = System.currentTimeMillis() - AppPreference.getLastUpdateTime(didReceivedAppsForSubCategories);
//
//
//        if(appDao!=null && appDao.getValue(key)!=null && duration < home__update_threshold){
//            String  value = appDao.getValue(key).getValue();
//            if(value!=null &&  !value.isEmpty()){
//
//                BazaarApps bazarApps=gson.fromJson(value, BazaarApps.class);
//                if(callBack!=null){
//                    callBack.didReceiveData(didReceivedAppsForSubCategories, bazarApps.getCafeBazaarAppList(),title,bazarApps.getNext());
//                    return;
//                }
//
//            }
//        }
//
//
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//               response -> {
//                   BazaarApps bazarApps=gson.fromJson(response, BazaarApps.class);
//                   if(callBack!=null){
//                       callBack.didReceiveData(didReceivedAppsForSubCategories, bazarApps.getCafeBazaarAppList(),title,bazarApps.getNext());
//
//                       new Thread(() -> {
//                           KeyValue keyValue = new KeyValue();
//                           keyValue.setKey(key);
//                           keyValue.setValue(response);
//                           if(appDao!=null){
//                               appDao.insertValue(keyValue);
//                               AppPreference.setLastUpdateTime(didReceivedAppsForSubCategories);
//                           }
//                       }).start();
//                   }
//               },
//               error -> {
//                   if(callBack!=null && error!=null){
//                       callBack.onError(error.getMessage());
//                   }
//               });
//       addToRequestQueue(stringRequest);
//
//    }
//
//
//    public void searchApps(String query,final ApiCallBack callBack){
//        String url=base_url + "search/" + query;
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                response -> {
//
//
//                    List<BazaarApps.CafeBazaarApp> subsCatList = (new Gson()).fromJson(response, new TypeToken<List<BazaarApps.CafeBazaarApp>>() {}.getType());
//                    if(callBack!=null){
//                        callBack.didReceiveData(didReceivedSearchResult, subsCatList);
//                    }
//                },
//                error -> {
//                    if(callBack!=null && error!=null){
//                        callBack.onError(error.getMessage());
//                    }
//                });
//        addToRequestQueue(stringRequest);
//    }
//
//
//
//    public void getPlayCategories(final ApiCallBack callBack){
//        SharedPreferences preferences=ApplicationLoader.applicationContext.getSharedPreferences("main_config",Context.MODE_PRIVATE);
//        String playCat=preferences.getString("play_cat","");
//        if (playCat != null && !playCat.isEmpty()) {
//            PlayCategory playCategory = (new Gson()).fromJson(playCat, PlayCategory.class);
//            if (callBack != null) {
//                callBack.didReceiveData(didReceivePlayCategory, playCategory);
//            }
//            return;
//        }
//        String url=base_url + "rank/categories/";
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                response -> {
//                     PlayCategory playCategory = (new Gson()).fromJson(response, PlayCategory.class);
//                    if(callBack!=null){
//                        callBack.didReceiveData(didReceivePlayCategory, playCategory);
//                    }
//                    updateData(didReceivePlayCategory,response);
//                },
//                error -> {
//                    if(callBack!=null && error!=null){
//                        callBack.onError(error.getMessage());
//                    }
//                });
//        addToRequestQueue(stringRequest);
//    }
//
//
//    public void getPlayFilters(final ApiCallBack callBack){
//        SharedPreferences preferences=ApplicationLoader.applicationContext.getSharedPreferences("main_config",Context.MODE_PRIVATE);
//        String play_filter=preferences.getString("play_filter","");
//        if (play_filter != null && !play_filter.isEmpty()) {
//            List<PlayFilter> filters = (new Gson()).fromJson(play_filter, new TypeToken<List<PlayFilter>>() {
//            }.getType());
//            if (callBack != null) {
//                callBack.didReceiveData(didReceivePlayFilters, filters);
//            }
//            return;
//        }
//        String url=base_url + "rank/filters/";
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                response -> {
//                    List<PlayFilter> filters = (new Gson()).fromJson(response, new TypeToken<List<PlayFilter>>() {}.getType());
//                    if(callBack!=null){
//                        callBack.didReceiveData(didReceivePlayFilters, filters);
//                        updateData(didReceivePlayFilters,response);
//                    }
//                },
//                error -> {
//                    if(callBack!=null && error!=null){
//                        callBack.onError(error.getMessage());
//                    }
//                });
//        addToRequestQueue(stringRequest);
//    }
//
//
//
//
//    public void getPlayApps(String categoryID,String filterID,final  ApiCallBack callBack){
//
//        String url= base_url +"rank/apps/" + filterID + "/" + categoryID;
//        AppDao appDao = DatabaseHandler.getInstance().appDao();
//        String key = String.valueOf(url.hashCode());
//        long duration  = System.currentTimeMillis() - AppPreference.getLastUpdateTime(didReceivedPlayApps);
//        if(appDao!=null && appDao.getValue(key)!=null && duration < play_app_update_threshold){
//            String  value = appDao.getValue(key).getValue();
//            if(value!=null &&  !value.isEmpty()){
//                List<PlayApp> playApps = (new Gson()).fromJson(value, new TypeToken<List<PlayApp>>() {}.getType());
//                if(callBack!=null) {
//                    callBack.didReceiveData(didReceivedPlayApps, playApps);
//                }
//            }
//        }
//
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                response -> {
//                    List<PlayApp> playApps = (new Gson()).fromJson(response, new TypeToken<List<PlayApp>>() {}.getType());
//                    if(callBack!=null){
//                        callBack.didReceiveData(didReceivedPlayApps, playApps);
//
//                        new Thread(() -> {
//                            KeyValue keyValue = new KeyValue();
//                            keyValue.setKey(key);
//                            keyValue.setValue(response);
//                            if(appDao!=null){
//                                appDao.insertValue(keyValue);
//                                AppPreference.setLastUpdateTime(didReceivedPlayApps);
//                            }
//
//                        }).start();
//                    }
//                },
//                error -> {
//                    if(callBack!=null && error!=null){
//                        callBack.onError(error.getMessage());
//                    }
//                });
//        addToRequestQueue(stringRequest);
//    }
//
//
//    public void getMoreAppsForCategory(String url,final  ApiCallBack callBack){
//
//
//        AppDao appDao = DatabaseHandler.getInstance().appDao();
//        String key = String.valueOf(url.hashCode());
//        long duration  = System.currentTimeMillis() - AppPreference.getLastUpdateTime(didReceivedMoreApps);
//
//
//        if(appDao!=null && appDao.getValue(key)!=null && duration < home__update_threshold){
//            String  value = appDao.getValue(key).getValue();
//            if(value!=null &&  !value.isEmpty()){
//
//                BazaarApps bazarApps=gson.fromJson(value, BazaarApps.class);
//                if(callBack!=null){
//                    callBack.didReceiveData(didReceivedMoreApps, bazarApps.getCafeBazaarAppList(),bazarApps.getNext());
//                    return;
//
//                }
//
//
//            }
//        }
//
//
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                response -> {
//                    BazaarApps bazarApps=gson.fromJson(response, BazaarApps.class);
//                    if(callBack!=null){
//
//                        callBack.didReceiveData(didReceivedMoreApps, bazarApps.getCafeBazaarAppList(),bazarApps.getNext());
//                        new Thread(() -> {
//                            KeyValue keyValue = new KeyValue();
//                            keyValue.setKey(key);
//                            keyValue.setValue(response);
//                            if(appDao!=null){
//                                appDao.insertValue(keyValue);
//                                AppPreference.setLastUpdateTime(didReceivedMoreApps);
//                            }
//
//                        }).start();
//
//                    }
//                },
//                error -> {
//                    if(callBack!=null && error!=null){
//                        callBack.onError(error.getMessage());
//                    }
//                });
//        addToRequestQueue(stringRequest);
//    }
//
//
//    public void getAppbyPackageName(String packageName,final  ApiCallBack callBack){
//
//
//        String url=base_url + "app/" + packageName;
//        AppDao appDao = DatabaseHandler.getInstance().appDao();
//        String key = String.valueOf(url.hashCode());
//        long duration  = System.currentTimeMillis() - AppPreference.getLastUpdateTime(didReceiveAppByPackageName);
//
//
//        if(appDao!=null && appDao.getValue(key)!=null && duration < home__update_threshold){
//            String  value = appDao.getValue(key).getValue();
//            if(value!=null &&  !value.isEmpty()){
//
//                BazaarApps.CafeBazaarApp cafeBazarApp = (new Gson()).fromJson(value, BazaarApps.CafeBazaarApp.class);
//                if(callBack!=null){
//                    callBack.didReceiveData(didReceiveAppByPackageName, cafeBazarApp);
//                    return;
//                }
//
//
//
//            }
//        }
//
//
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url
//                ,
//                response -> {
//
//                 BazaarApps.CafeBazaarApp cafeBazarApp = (new Gson()).fromJson(response, BazaarApps.CafeBazaarApp.class);
//                    if(callBack!=null){
//                        callBack.didReceiveData(didReceiveAppByPackageName, cafeBazarApp);
//                        new Thread(() -> {
//                            KeyValue keyValue = new KeyValue();
//                            keyValue.setKey(key);
//                            keyValue.setValue(response);
//                            if(appDao!=null){
//                                appDao.insertValue(keyValue);
//                                AppPreference.setLastUpdateTime(didReceiveAppByPackageName);
//                            }
//
//                        }).start();
//                    }
//
//                },
//                error -> {
//                    if(callBack!=null && error!=null){
//                        callBack.onError(error.getMessage());
//                    }
//                });
//        addToRequestQueue(stringRequest);
//
//    }
//
//
//    public void getDeveloperById(String id, final  ApiCallBack callBack){
//
//        String url=base_url + "developer/" + id;
//        AppDao appDao = DatabaseHandler.getInstance().appDao();
//        String key = String.valueOf(url.hashCode());
//        long duration  = System.currentTimeMillis() - AppPreference.getLastUpdateTime(didReceivedDeveloper);
//
//
//        if(appDao!=null && appDao.getValue(key)!=null && duration < home__update_threshold){
//            String  value = appDao.getValue(key).getValue();
//            if(value!=null &&  !value.isEmpty()){
//
//                Developer developer = (new Gson()).fromJson(value, Developer.class);
//                if(callBack!=null){
//                    callBack.didReceiveData(didReceivedDeveloper, developer);
//                    return;
//                }
//
//
//            }
//        }
//
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url
//                ,
//                response -> {
//
//                    Developer developer = (new Gson()).fromJson(response, Developer.class);
//                    if(callBack!=null){
//                        callBack.didReceiveData(didReceivedDeveloper, developer);
//                        new Thread(() -> {
//                            KeyValue keyValue = new KeyValue();
//                            keyValue.setKey(key);
//                            keyValue.setValue(response);
//                            if(appDao!=null){
//                                appDao.insertValue(keyValue);
//                                AppPreference.setLastUpdateTime(didReceivedDeveloper);
//                            }
//
//                        }).start();
//
//                    }
//
//                },
//                error -> {
//                    if(callBack!=null && error!=null){
//                        callBack.onError(error.getMessage());
//                    }
//                });
//        addToRequestQueue(stringRequest);
//
//    }
//
//
//    public void getAppsByDeveloper(String id, final  ApiCallBack callBack){
//        String url=base_url + "developer/"+ id +"/apps";
//        AppDao appDao = DatabaseHandler.getInstance().appDao();
//        String key = String.valueOf(url.hashCode());
//        long duration  = System.currentTimeMillis() - AppPreference.getLastUpdateTime(didReceiveDeveloperApps);
//
//
//        if(appDao!=null && appDao.getValue(key)!=null && duration < home__update_threshold){
//            String  value = appDao.getValue(key).getValue();
//            if(value!=null &&  !value.isEmpty()){
//
//                List<BazaarApps.CafeBazaarApp> cafeBazarApps = (new Gson()).fromJson(value, new TypeToken<List<BazaarApps.CafeBazaarApp>>() {}.getType());
//                if(callBack!=null){
//                    callBack.didReceiveData(didReceiveDeveloperApps, cafeBazarApps);
//                    return;
//                }
//
//
//            }
//        }
//
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                response -> {
//                    List<BazaarApps.CafeBazaarApp> cafeBazarApps = (new Gson()).fromJson(response, new TypeToken<List<BazaarApps.CafeBazaarApp>>() {}.getType());
//                    if(callBack!=null){
//                        callBack.didReceiveData(didReceiveDeveloperApps, cafeBazarApps);
//                        new Thread(() -> {
//                            KeyValue keyValue = new KeyValue();
//                            keyValue.setKey(key);
//                            keyValue.setValue(response);
//                            if(appDao!=null){
//                                appDao.insertValue(keyValue);
//                                AppPreference.setLastUpdateTime(didReceiveDeveloperApps);
//                            }
//
//                        }).start();
//
//                    }
//                },
//                error -> {
//                    if(callBack!=null && error!=null){
//                        callBack.onError(error.getMessage());
//                    }
//                });
//        addToRequestQueue(stringRequest);
//    }
//
//
//    public void getSubCollection(final ApiCallBack callBack){
//        String url=base_url + "home/subcollections/";
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                response -> {
//                    List<BazaarApps.CafeBazaarApp> subsCatList = (new Gson()).fromJson(response, new TypeToken<List<BazaarApps.CafeBazaarApp>>() {}.getType());
//                    if(callBack!=null){
//                        callBack.didReceiveData(didReceiveSubCollations, subsCatList);
//                    }
//                },
//                error -> {
//                    if(callBack!=null && error!=null){
//                        callBack.onError(error.getMessage());
//                    }
//                });
//        addToRequestQueue(stringRequest);
//
//
//    }
//
//    public void getCollection(final ApiCallBack callBack){
//
//        String url=base_url + "home/collections/";
//        AppDao appDao = DatabaseHandler.getInstance().appDao();
//        String key = String.valueOf(url.hashCode());
//
//        long duration  = System.currentTimeMillis() - AppPreference.getLastUpdateTime(didReceiveCollations);
//
//        if(appDao!=null && appDao.getValue(key)!=null && duration < home__update_threshold){
//            String  value = appDao.getValue(key).getValue();
//            if(value!=null &&  !value.isEmpty()){
//                Collections collection = (new Gson()).fromJson(value, Collections.class);
//                if(callBack!=null){
//                    callBack.didReceiveData(didReceiveCollations, collection);
//                    return;
//                }
//            }
//        }
//
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                response -> {
//
//            Collections collection = (new Gson()).fromJson(response, Collections.class);
//                    if(callBack!=null){
//                        callBack.didReceiveData(didReceiveCollations, collection);
//                        KeyValue keyValue = new KeyValue();
//                        keyValue.setKey(key);
//                        keyValue.setValue(response);
//                        if(appDao!=null)
//                            appDao.insertValue(keyValue);
//
//                    }
//                },
//                error -> {
//                    if(callBack!=null && error!=null){
//                        callBack.onError(error.getMessage());
//                    }
//                });
//        addToRequestQueue(stringRequest);
//
//    }
//
//
//    public void getSubCollForCollId(String id,String name,final ApiCallBack callBack){
//
//        String url=base_url + "home/subcollections/" + id;
//        AppDao appDao = DatabaseHandler.getInstance().appDao();
//        String key = String.valueOf(url.hashCode());
//        long duration  = System.currentTimeMillis() - AppPreference.getLastUpdateTime(didReceiveSubCollations);
//        if(appDao!=null && appDao.getValue(key)!=null && duration < home__update_threshold){
//            String  value = appDao.getValue(key).getValue();
//            if(value!=null &&  !value.isEmpty()){
//                List<Subcollection> collection = (new Gson()).fromJson(value, new TypeToken<List<Subcollection>>() {}.getType());
//                if(callBack!=null){
//                    callBack.didReceiveData(didReceiveSubCollations,name, collection);
//                    return;
//                }
//            }
//        }
//
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                response -> {
//                    List<Subcollection> collection = (new Gson()).fromJson(response, new TypeToken<List<Subcollection>>() {}.getType());
//
//                    if(callBack!=null){
//                        callBack.didReceiveData(didReceiveSubCollations,name, collection);
//
//                        new Thread(() -> {
//                            KeyValue keyValue = new KeyValue();
//                            keyValue.setKey(key);
//                            keyValue.setValue(response);
//                            if(appDao!=null){
//                                appDao.insertValue(keyValue);
//                                AppPreference.setLastUpdateTime(didReceiveSubCollations);
//                            }
//
//                        }).start();
//                    }
//                },
//                error -> {
//                    if(callBack!=null && error!=null){
//                        callBack.onError(error.getMessage());
//                    }
//                });
//        addToRequestQueue(stringRequest);
//    }
//
//
//    public void getAppsForSubCollId(String id,final ApiCallBack callBack){
//
//        String url=base_url + "home/subcollection/" + id;
//        AppDao appDao = DatabaseHandler.getInstance().appDao();
//        String key = String.valueOf(url.hashCode());
//        long duration  = System.currentTimeMillis() - AppPreference.getLastUpdateTime(didReceiveAppsForSubCollations);
//        if(appDao!=null && appDao.getValue(key)!=null && duration < home__update_threshold){
//            String  value = appDao.getValue(key).getValue();
//            if(value!=null &&  !value.isEmpty()){
//                List<CollApp> collApps = (new Gson()).fromJson(value, new TypeToken<List<CollApp>>() {}.getType());
//                if(callBack!=null){
//                    callBack.didReceiveData(didReceiveAppsForSubCollations, collApps );
//                    return;
//                }
//            }
//        }
//
//
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                response -> {
//                    List<CollApp> collApps = (new Gson()).fromJson(response, new TypeToken<List<CollApp>>() {}.getType());
//                    if(callBack!=null){
//                        callBack.didReceiveData(didReceiveAppsForSubCollations, collApps );
//                        new Thread(() -> {
//                            KeyValue keyValue = new KeyValue();
//                            keyValue.setKey(key);
//                            keyValue.setValue(response);
//                            if(appDao!=null){
//                                appDao.insertValue(keyValue);
//                                AppPreference.setLastUpdateTime(didReceiveAppsForSubCollations);
//                            }
//
//                        }).start();
//
//                    }
//                },
//                error -> {
//                    if(callBack!=null && error!=null){
//                        callBack.onError(error.getMessage());
//                    }
//                });
//        addToRequestQueue(stringRequest);
//    }
//
//
//
//    public void getRecommendedApps(final ApiCallBack callBack){
//        String url=base_url + "myapps/recom/";
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                response -> {
//                    List<Recommended> recommendeds = (new Gson()).fromJson(response, new TypeToken<List<Recommended>>() {}.getType());
//                    if(callBack!=null){
//                        callBack.didReceiveData(didReceiveRecommendedApps, recommendeds );
//                    }
//                },
//                error -> {
//                    if(callBack!=null && error!=null){
//                        callBack.onError(error.getMessage());
//                    }
//                });
//        addToRequestQueue(stringRequest);
//
//    }
//
//    public void getProxyList(final ApiCallBack callBack){
//        String url = base_url + "/proxy/";
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                response -> {
//
//                    List<Recommended> recommendeds = (new Gson()).fromJson(response, new TypeToken<List<Recommended>>() {}.getType());
//                    if(callBack!=null){
//                        callBack.didReceiveData(didReceiveRecommendedApps, recommendeds );
//                    }
//                },
//                error -> {
//                    if(callBack!=null && error!=null){
//                        callBack.onError(error.getMessage());
//                    }
//                });
//        addToRequestQueue(stringRequest);
//
//    }
//
//
//    public void getAdverizmentApps(final ApiCallBack callBack){
//        String url=base_url + "myapps/ad/";
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                response -> {
//                    List<Recommended> subsCatList = (new Gson()).fromJson(response, new TypeToken<List<Recommended>>() {}.getType());
//                    if(callBack!=null){
//                        callBack.didReceiveData(didReceiveAdvertisementApps, subsCatList);
//                    }
//                },
//                error -> {
//                    if(callBack!=null && error!=null){
//                        callBack.onError(error.getMessage());
//                    }
//                });
//        addToRequestQueue(stringRequest);
//
//    }
//
//    private void updateData(int type,Object data){
//        if(data==null)return;
//        SharedPreferences appPref= ApplicationLoader.applicationContext.getSharedPreferences("main_config",Context.MODE_PRIVATE);
//        if(type== didReceivedGameCategory || type== didReceivedAppCategory){
//            appPref.edit().putString("cat",data.toString()).apply();
//        }else if(type== didReceivePlayCategory){
//            appPref.edit().putString("play_cat",data.toString()).apply();
//        }else if(type==didReceivePlayFilters){
//            appPref.edit().putString("play_filter",data.toString()).apply();
//        }
//    }
//}