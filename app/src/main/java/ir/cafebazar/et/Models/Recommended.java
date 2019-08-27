package ir.cafebazar.et.Models;

import com.google.gson.annotations.SerializedName;

public class Recommended {

    @SerializedName("package_name")
    private String package_name;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("size")
    private String size;

    @SerializedName("version")
    private String version;

    @SerializedName("url")
    private String url;

    @SerializedName("icon")
    private String icon;

    @SerializedName("rating_total")
    private String rating_total;

    @SerializedName("rating_total_count")
    private String rating_total_count;

    @SerializedName("app_type")
    private String app_type;


    public String getPackage_name() {
        return package_name;
    }

    public void setPackage_name(String package_name) {
        this.package_name = package_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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

    public String getApp_type() {
        return app_type;
    }

    public void setApp_type(String app_type) {
        this.app_type = app_type;
    }
}
