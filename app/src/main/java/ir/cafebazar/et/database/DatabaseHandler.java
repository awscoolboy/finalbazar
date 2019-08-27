package ir.cafebazar.et.database;


import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import ir.cafebazar.et.ApplicationLoader;
import ir.cafebazar.et.Models.BazaarApps;
import ir.cafebazar.et.Models.Categories;
import ir.cafebazar.et.Models.Favorite;
import ir.cafebazar.et.Models.KeyValue;
import ir.cafebazar.et.Models.Play.PlayApp;
import ir.cafebazar.et.Models.SubCategories;
import ir.cafebazar.et.util.Converters;


@Database(entities = {BazaarApps.CafeBazaarApp.class, PlayApp.class, SubCategories.class, Categories.class, Favorite.class, KeyValue.class}, version = 5)
@TypeConverters({Converters.class})
public abstract class DatabaseHandler extends RoomDatabase {

    public abstract AppDao appDao();

    private static DatabaseHandler instance;


    public static synchronized DatabaseHandler getInstance(){
        if(instance==null){
            synchronized (DatabaseHandler.class){

                instance= Room.databaseBuilder(ApplicationLoader.applicationContext, DatabaseHandler.class,"data.db")
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build();
            }
        }
        return instance;
    }
}
