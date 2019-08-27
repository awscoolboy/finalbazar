package ir.cafebazar.et.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.firebase.ui.auth.AuthUI;

import java.io.File;

import ir.cafebazar.et.ApplicationLoader;
import ir.cafebazar.et.database.DatabaseHandler;

public class AppPreference{

    private static final String main_pref="main_pref";
    private static final String update_pref="update_time";


    public static SharedPreferences getMainPreference(){
        return ApplicationLoader.applicationContext.getSharedPreferences(main_pref, Context.MODE_PRIVATE);
    }

    private static SharedPreferences getUpdatePrefrence(){
        return ApplicationLoader.applicationContext.getSharedPreferences(update_pref, Context.MODE_PRIVATE);
    }


    public static long getLastUpdateTime(int type){
        return getUpdatePrefrence().getLong("update_type_" + type,0);
    }



    public static void setLastUpdateTime(int type){
        getUpdatePrefrence().edit().putLong("update_type_" + type,System.currentTimeMillis()).apply();
    }


    public static void logout(Activity context){
        deleteCache(context);

        DatabaseHandler.getInstance().clearAllTables();

        AuthUI.getInstance()
                .signOut(context)
                .addOnCompleteListener(task -> {
                 context.recreate();

       });


    }


    private static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) { e.printStackTrace();}
    }

    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String child : children) {
                boolean success = deleteDir(new File(dir, child));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }






}
