package ir.cafebazar.et.database;



import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import ir.cafebazar.et.Models.BazaarApps;
import ir.cafebazar.et.Models.Categories;
import ir.cafebazar.et.Models.Favorite;
import ir.cafebazar.et.Models.KeyValue;
import ir.cafebazar.et.Models.Play.PlayApp;
import ir.cafebazar.et.Models.SubCategories;

@Dao
public interface AppDao{


    /**
     * favorite apps table
     */
    @Query("SELECT * FROM favorite")
     List<Favorite> getAllFavApps();


    @Query("SELECT * FROM Favorite WHERE package_name LIKE :packageName")
    Favorite findFavoriteAppByPackageName(String packageName);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavApp(Favorite... favApps);


    @Delete
    void deleteFavApp(Favorite favApp);


    //google play ranking
    @Query("SELECT * FROM PlayApp WHERE category Like :categoryID AND  rankfilter LIKE :filterID")
    List<PlayApp> getPlaysApps(String categoryID,String filterID);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPlayApps(PlayApp... playApps);

    @Delete
    void deletePlayApp(PlayApp playApp);
    //


    //sub categoryis
    @Query("SELECT * FROM SubCategories WHERE id Like :subCatId")
    List<SubCategories> getSubCategories(String subCatId);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSubCategories(SubCategories... subCategories);


    @Delete
    void deleteSubCategory(SubCategories subCategories);
    //


    //categories
    @Query("SELECT * FROM Categories")
    List<Categories> getCategories();

    @Query("SELECT * FROM Categories WHERE category_typed Like 1")
    List<Categories> getAppCategories();

    @Query("SELECT * FROM Categories WHERE category_typed Like 2")
    List<Categories> getGameCategories();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCategories(Categories... categories);

    @Delete
    void removeCategories(Categories categories);
    //end of categories


    /**
     * apps table for sync
     */

    @Query("SELECT * FROM favorite")
    List<BazaarApps.CafeBazaarApp> getAllApps();


    @Query("SELECT * FROM CafeBazaarApp WHERE package_name LIKE :packageName")
    BazaarApps.CafeBazaarApp findAppByPackageName(String packageName);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAppp(BazaarApps.CafeBazaarApp... favApps);


    @Delete
    void deleteApp(BazaarApps.CafeBazaarApp favApp);


    @Query("SELECT * FROM CafeBazaarApp WHERE sub_category LIKE :subCatId")
    List<BazaarApps.CafeBazaarApp> getAppsFroSubCat(String subCatId);


    //key value pair for data

    @Query("SELECT * FROM KeyValue WHERE `key` LIke :keyVal")
    KeyValue getValue(String keyVal);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertValue(KeyValue keyValue);





}

