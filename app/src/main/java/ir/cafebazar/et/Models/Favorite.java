package ir.cafebazar.et.Models;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;



@Entity
public class Favorite implements Serializable {

    @ColumnInfo(name = "developer_url")
    private String developer_url;

    @NonNull
    @PrimaryKey
    private String package_name;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "installs")
    private String installs;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "developer")
    private String developer;

    @ColumnInfo(name = "size")
    private String  size;

    @ColumnInfo(name = "url")
    private String url;

    @ColumnInfo(name = "icon")
    private String icon;

    @ColumnInfo(name = "cateogry")
    private String cateogry;

    @ColumnInfo(name = "sub_category")
    private String sub_category;


    @Ignore
    private List<String> screenshots;


    @ColumnInfo(name = "rating_total")
    private String rating_total;


    @ColumnInfo(name = "rating_total_count")
    private String rating_total_count;


    public String getDeveloper_url() {
        return developer_url;
    }

    public void setDeveloper_url(String developer_url) {
        this.developer_url = developer_url;
    }

    @NonNull
    public String getPackage_name() {
        return package_name;
    }

    public void setPackage_name(@NonNull String package_name) {
        this.package_name = package_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstalls() {
        return installs;
    }

    public void setInstalls(String installs) {
        this.installs = installs;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getCateogry() {
        return cateogry;
    }

    public void setCateogry(String cateogry) {
        this.cateogry = cateogry;
    }

    public String getSub_category() {
        return sub_category;
    }

    public void setSub_category(String sub_category) {
        this.sub_category = sub_category;
    }

    public List<String> getScreenshots() {
        return screenshots;
    }

    public void setScreenshots(List<String> screenshots) {
        this.screenshots = screenshots;
    }

    public String getRating_total() {
        return rating_total;
    }

    public void setRating_total(String rating_total) {
        this.rating_total = rating_total;
    }

    public String getRating_total_count() {
        return rating_total_count;
    }

    public void setRating_total_count(String rating_total_count) {
        this.rating_total_count = rating_total_count;
    }
}
