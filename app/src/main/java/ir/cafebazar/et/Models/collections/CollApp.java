package ir.cafebazar.et.Models.collections;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CollApp implements Serializable {


    @SerializedName("name")
    private String name;

    @SerializedName("price")
    private String price;

    @SerializedName("url")
    private String url;

    @SerializedName("icon")
    private String icon;

    @SerializedName("rating_total")
    private String rating_total;

    @SerializedName("rating_total_count")
    private String rating_total_count;

    @SerializedName("package_name")
    private String package_name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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

    public String getPackage_name() {
        return package_name;
    }

    public void setPackage_name(String package_name) {
        this.package_name = package_name;
    }
}
