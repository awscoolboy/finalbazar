package ir.cafebazar.et.bazar.models;

import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.gson.annotations.SerializedName;

public class App{


    @SerializedName("name")//for search only
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAdd() {
        return isAdd;
    }

    public void setAdd(boolean add) {
        isAdd = add;
    }

    public boolean isNative() {
        return isNative;
    }

    public void setNative(boolean aNative) {
        isNative = aNative;
    }

    public UnifiedNativeAd getUnifiedNativeAd() {
        return unifiedNativeAd;
    }

    public void setUnifiedNativeAd(UnifiedNativeAd unifiedNativeAd) {
        this.unifiedNativeAd = unifiedNativeAd;
    }

    public boolean isAdd;
    public boolean isNative;
    public UnifiedNativeAd unifiedNativeAd;

    @SerializedName("published")
    private String published;

    @SerializedName("id")
    private String id;

    @SerializedName("authorName")
    private String authorName;

    @SerializedName("hasInAppPurchase")
    private String hasInAppPurchase;

    @SerializedName("priceBeforeDiscountString")
    private String pricpriceBeforeDiscountStringeString;

    @SerializedName("priceString")
    private String priceString;

    @SerializedName("versionName")
    private String versionName;


    @SerializedName("versionCode")
    private String versionCode;


    @SerializedName("packageName")
    private String packageName;


    @SerializedName("appName")
    private String appName;

    @SerializedName("rate")
    private String rate;

    @SerializedName("price")
    private String price;

    @SerializedName("installCount")
    private String installCount;

    @SerializedName("priceBeforeDiscount")
    private String priceBeforeDiscount;


    @SerializedName("PriceBeforeDiscount2")
    private String PriceBeforeDiscount2;

    private String image;


    public App() {
    }

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getHasInAppPurchase() {
        return hasInAppPurchase;
    }

    public void setHasInAppPurchase(String hasInAppPurchase) {
        this.hasInAppPurchase = hasInAppPurchase;
    }

    public String getPricpriceBeforeDiscountStringeString() {
        return pricpriceBeforeDiscountStringeString;
    }

    public void setPricpriceBeforeDiscountStringeString(String pricpriceBeforeDiscountStringeString) {
        this.pricpriceBeforeDiscountStringeString = pricpriceBeforeDiscountStringeString;
    }

    public String getPriceString() {
        return priceString;
    }

    public void setPriceString(String priceString) {
        this.priceString = priceString;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getInstallCount() {
        return installCount;
    }

    public void setInstallCount(String installCount) {
        this.installCount = installCount;
    }

    public String getPriceBeforeDiscount() {
        return priceBeforeDiscount;
    }

    public void setPriceBeforeDiscount(String priceBeforeDiscount) {
        this.priceBeforeDiscount = priceBeforeDiscount;
    }

    public String getPriceBeforeDiscount2() {
        return PriceBeforeDiscount2;
    }

    public void setPriceBeforeDiscount2(String priceBeforeDiscount2) {
        PriceBeforeDiscount2 = priceBeforeDiscount2;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
