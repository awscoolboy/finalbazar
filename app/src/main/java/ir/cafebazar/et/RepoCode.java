package ir.cafebazar.et;

public class RepoCode {

//    public void getAppCategory(final ApiCallBack callBack){
//        final String data= AppPreference.getMainPreference().getString("cat","");
//        if(data == null){
//            return;
//        }
//        if(!data.isEmpty()){
//            new Thread(() -> {
//                Categories categories=gson.fromJson(data,Categories.class);
//
//                final List<Categories.Category> appsList=new ArrayList<>();
//                for(Categories.Category category:categories.getCategoryList()){
//                    if(category.getCategory_type()==1){
//                        appsList.add(category);
//                    }
//                }
//                AndroidHelper.runOnUIThread(() -> {
//                    if(callBack!=null){
//                        callBack.didReceiveData(didReceivedAppCategory, appsList);
//                    }
//                });
//            }).start();
//            return;
//        }
//        String  url= base_url + "categories/?limit=100&offset=0";
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                response -> {
//                    Categories categories=gson.fromJson(response,Categories.class);
//                    List<Categories.Category> appsList=new ArrayList<>();
//                    for(Categories.Category category:categories.getCategoryList()){
//                        if(category.getCategory_type()==1){
//                            appsList.add(category);
//                        }
//                    }
//                    if(callBack!=null){
//                        callBack.didReceiveData(didReceivedAppCategory, appsList);
//                        updateData(didReceivedAppCategory,response);
//                    }
//                },
//                error -> {
//                    if(callBack!=null){
//                        if(error!=null && error.getMessage()!=null){
//                            callBack.onError(error.getMessage());
//                        }
//                    }
//                });
//        addToRequestQueue(stringRequest);
//    }




//    public void getGameCategory(final ApiCallBack callBack){
//        final String data=AppPreference.getMainPreference().getString("cat","");
//        if (data != null && data.isEmpty()) {
//            new Thread(() -> {
//                Categories categories = gson.fromJson(data, Categories.class);
//                if(categories==null || categories.getCategoryList().isEmpty()){
//                    AppPreference.getMainPreference().edit().putString("cat","").apply();
//                    getGameCategory(callBack);
//                    return;
//                }
//
//                final List<Categories.Category> gameList = new ArrayList<>();
//                for (Categories.Category category : categories.getCategoryList()) {
//                    if (category.getCategory_type() == 2) {
//                        gameList.add(category);
//                    }
//                }
//                AndroidHelper.runOnUIThread(() -> {
//                    if (callBack != null) {
//                        callBack.didReceiveData(didReceivedGameCategory, gameList);
//                    }
//                });
//
//            }).start();
//            return;
//        }
//        String  url= base_url + "categories/?limit=100&offset=0";
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                response -> {
//                    Categories categories=gson.fromJson(response,Categories.class);
//                    List<Categories.Category> gameList=new ArrayList<>();
//                    for(Categories.Category category:categories.getCategoryList()){
//                        if(category.getCategory_type()==2){
//                            gameList.add(category);
//                        }
//                    }
//
//                    if(callBack!=null){
//                        callBack.didReceiveData(didReceivedGameCategory, gameList);
//                        updateData(didReceivedGameCategory,response);
//                    }
//                },
//                error -> {
//                    if(callBack!=null && error!=null){
//                        callBack.onError(error.getMessage());
//                    }
//                });
//        addToRequestQueue(stringRequest);
//    }


}
