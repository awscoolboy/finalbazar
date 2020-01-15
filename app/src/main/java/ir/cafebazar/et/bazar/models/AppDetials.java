package ir.cafebazar.et.bazar.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AppDetials {


    public AppDetials() {
    }

    @SerializedName("name")
    private String name;

    @SerializedName("packageName")
    private String packageName;

    @SerializedName("appEmail")
    private String appEmail;

    @SerializedName("appPhone")
    private String appPhone;

    @SerializedName("homepage")
    private String homepage;

    @SerializedName("authorName")
    private String authorName;


    @SerializedName("authorSlug")
    private String authorSlug;

    @SerializedName("categoryName")
    private String categoryName;


    @SerializedName("description")
    private String description;


    @SerializedName("shortDescription")
    private String shortDescription;

    @SerializedName("contentRating")
    private String contentRating;


    @SerializedName("tinyRatingImage")
    private String tinyRatingImage;


    @SerializedName("hasInAppPurchase")
    private boolean hasInAppPurchase;


    @SerializedName("package")
    private Package aPackage;

    public static class Package{

        @SerializedName("packageID")
        private String packageID;

        @SerializedName("packageSize")
        private String packageSize;

        @SerializedName("packageToken")
        private String packageToken;

        @SerializedName("packageHash")
        private String packageHash;

        @SerializedName("versionCode")
        private String versionCode;

        @SerializedName("versionName")
        private String versionName;

        @SerializedName("changeLog")
        private String changeLog;

        @SerializedName("minimumSDKVersion")
        private String minimumSDKVersion;

        @SerializedName("lastUpdated")
        private String lastUpdated;

        @SerializedName("lastUpdatedTimeFromEpoch")
        private String  lastUpdatedTimeFromEpoch;

        @SerializedName("haveAdNetwork")
        private boolean haveAdNetwork;

        @SerializedName("permissions")
        private ArrayList<String> permissions;

        @SerializedName("verboseSize")
        private String verboseSize;

        @SerializedName("verboseSizeLabel")
        private String verboseSizeLabel;

        public Package() {
        }

        public String getPackageID() {
            return packageID;
        }

        public void setPackageID(String packageID) {
            this.packageID = packageID;
        }

        public String getPackageSize() {
            return packageSize;
        }

        public void setPackageSize(String packageSize) {
            this.packageSize = packageSize;
        }

        public String getPackageToken() {
            return packageToken;
        }

        public void setPackageToken(String packageToken) {
            this.packageToken = packageToken;
        }

        public String getPackageHash() {
            return packageHash;
        }

        public void setPackageHash(String packageHash) {
            this.packageHash = packageHash;
        }

        public String getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(String versionCode) {
            this.versionCode = versionCode;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        public String getChangeLog() {
            return changeLog;
        }

        public void setChangeLog(String changeLog) {
            this.changeLog = changeLog;
        }

        public String getMinimumSDKVersion() {
            return minimumSDKVersion;
        }

        public void setMinimumSDKVersion(String minimumSDKVersion) {
            this.minimumSDKVersion = minimumSDKVersion;
        }

        public String getLastUpdated() {
            return lastUpdated;
        }

        public void setLastUpdated(String lastUpdated) {
            this.lastUpdated = lastUpdated;
        }

        public String getLastUpdatedTimeFromEpoch() {
            return lastUpdatedTimeFromEpoch;
        }

        public void setLastUpdatedTimeFromEpoch(String lastUpdatedTimeFromEpoch) {
            this.lastUpdatedTimeFromEpoch = lastUpdatedTimeFromEpoch;
        }

        public boolean isHaveAdNetwork() {
            return haveAdNetwork;
        }

        public void setHaveAdNetwork(boolean haveAdNetwork) {
            this.haveAdNetwork = haveAdNetwork;
        }

        public ArrayList<String> getPermissions() {
            return permissions;
        }

        public void setPermissions(ArrayList<String> permissions) {
            this.permissions = permissions;
        }

        public String getVerboseSize() {
            return verboseSize;
        }

        public void setVerboseSize(String verboseSize) {
            this.verboseSize = verboseSize;
        }

        public String getVerboseSizeLabel() {
            return verboseSizeLabel;
        }

        public void setVerboseSizeLabel(String verboseSizeLabel) {
            this.verboseSizeLabel = verboseSizeLabel;
        }
    }


    @SerializedName("screenShots")
    private ArrayList<ScreenShot> screenShots;

    public static  class ScreenShot{

        @SerializedName("name")
        private String name;

        @SerializedName("thumbnailName")
        private String thumbnailName;


        public ScreenShot() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getThumbnailName() {
            return thumbnailName;
        }

        public void setThumbnailName(String thumbnailName) {
            this.thumbnailName = thumbnailName;
        }
    }

    @SerializedName("icon")
    private String icon;

    @SerializedName("isGame")
    private boolean isGame;

    @SerializedName("RelatedPage")
    private RelatedPage relatedPage;

    public static class RelatedPage{


        @SerializedName("icon")
        private String icon;


        @SerializedName("isGame")
        private boolean isGame;

        @SerializedName("rows")
        private ArrayList<Row> rows;

        public static class Row{

            public Row() {
            }

            @SerializedName("title")
            private String title;

            @SerializedName("more")
            private String  more;

            @SerializedName("type")
            private String type;

            @SerializedName("apps")
            private ArrayList<Apps> apps;


            public static  class Apps{

                @SerializedName("name")
                private String name;

                @SerializedName("packageName")
                private String packageName;

                @SerializedName("rate")
                private String rate;

                @SerializedName("price")
                private String  price;

                @SerializedName("authorName")
                private String authorName;

                public Apps() {
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getPackageName() {
                    return packageName;
                }

                public void setPackageName(String packageName) {
                    this.packageName = packageName;
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

                public String getAuthorName() {
                    return authorName;
                }

                public void setAuthorName(String authorName) {
                    this.authorName = authorName;
                }
            }


            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getMore() {
                return more;
            }

            public void setMore(String more) {
                this.more = more;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public ArrayList<Apps> getApps() {
                return apps;
            }

            public void setApps(ArrayList<Apps> apps) {
                this.apps = apps;
            }
        }

        public RelatedPage() {
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public boolean isGame() {
            return isGame;
        }

        public void setGame(boolean game) {
            isGame = game;
        }

        public ArrayList<Row> getRows() {
            return rows;
        }

        public void setRows(ArrayList<Row> rows) {
            this.rows = rows;
        }
    }


    @SerializedName("rate")
    private String rate;

    @SerializedName("reviewCount")
    private String reviewCount;


    @SerializedName("rate1Count")
    private String rate1Count;

    @SerializedName("rate2Count")
    private String rate2Count;

    @SerializedName("rate3Count")
    private String rate3Count;

    @SerializedName("rate4Count")
    private String rate4Count;

    @SerializedName("rate5Count")
    private String rate5Count;


    @SerializedName("installCountRange")
    private String installCountRange;


    @SerializedName("price")
    private Price prices;

    public static class Price{

        @SerializedName("price")
        private String price;

        @SerializedName("priceString")
        private String  priceString;

        public Price() {
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getPriceString() {
            return priceString;
        }

        public void setPriceString(String priceString) {
            this.priceString = priceString;
        }
    }


    @SerializedName("stats")
    private Stats stats;


    public static class Stats{

        @SerializedName("rate")
        private String rate;

        @SerializedName("reviewCount")
        private String reviewCount;


        @SerializedName("rate1Count")
        private String rate1Count;

        @SerializedName("rate2Count")
        private String rate2Count;

        @SerializedName("rate3Count")
        private String rate3Count;

        @SerializedName("rate4Count")
        private String rate4Count;

        @SerializedName("rate5Count")
        private String rate5Count;

        @SerializedName("installCountRange")

        private String installCountRange;

        public Stats() {
        }

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

        public String getReviewCount() {
            return reviewCount;
        }

        public void setReviewCount(String reviewCount) {
            this.reviewCount = reviewCount;
        }

        public String getRate1Count() {
            return rate1Count;
        }

        public void setRate1Count(String rate1Count) {
            this.rate1Count = rate1Count;
        }

        public String getRate2Count() {
            return rate2Count;
        }

        public void setRate2Count(String rate2Count) {
            this.rate2Count = rate2Count;
        }

        public String getRate3Count() {
            return rate3Count;
        }

        public void setRate3Count(String rate3Count) {
            this.rate3Count = rate3Count;
        }

        public String getRate4Count() {
            return rate4Count;
        }

        public void setRate4Count(String rate4Count) {
            this.rate4Count = rate4Count;
        }

        public String getRate5Count() {
            return rate5Count;
        }

        public void setRate5Count(String rate5Count) {
            this.rate5Count = rate5Count;
        }

        public String getInstallCountRange() {
            return installCountRange;
        }

        public void setInstallCountRange(String installCountRange) {
            this.installCountRange = installCountRange;
        }
    }

    @SerializedName("publishedStatus")
    private String publishedStatus;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getAppEmail() {
        return appEmail;
    }

    public void setAppEmail(String appEmail) {
        this.appEmail = appEmail;
    }

    public String getAppPhone() {
        return appPhone;
    }

    public void setAppPhone(String appPhone) {
        this.appPhone = appPhone;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorSlug() {
        return authorSlug;
    }

    public void setAuthorSlug(String authorSlug) {
        this.authorSlug = authorSlug;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getContentRating() {
        return contentRating;
    }

    public void setContentRating(String contentRating) {
        this.contentRating = contentRating;
    }

    public String getTinyRatingImage() {
        return tinyRatingImage;
    }

    public void setTinyRatingImage(String tinyRatingImage) {
        this.tinyRatingImage = tinyRatingImage;
    }

    public boolean getHasInAppPurchase() {
        return hasInAppPurchase;
    }

    public void setHasInAppPurchase(boolean hasInAppPurchase) {
        this.hasInAppPurchase = hasInAppPurchase;
    }

    public Package getaPackage() {
        return aPackage;
    }

    public void setaPackage(Package aPackage) {
        this.aPackage = aPackage;
    }

    public ArrayList<ScreenShot> getScreenShots() {
        return screenShots;
    }

    public void setScreenShots(ArrayList<ScreenShot> screenShots) {
        this.screenShots = screenShots;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public boolean isGame() {
        return isGame;
    }

    public void setGame(boolean game) {
        isGame = game;
    }

    public RelatedPage getRelatedPage() {
        return relatedPage;
    }

    public void setRelatedPage(RelatedPage relatedPage) {
        this.relatedPage = relatedPage;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(String reviewCount) {
        this.reviewCount = reviewCount;
    }

    public String getRate1Count() {
        return rate1Count;
    }

    public void setRate1Count(String rate1Count) {
        this.rate1Count = rate1Count;
    }

    public String getRate2Count() {
        return rate2Count;
    }

    public void setRate2Count(String rate2Count) {
        this.rate2Count = rate2Count;
    }

    public String getRate3Count() {
        return rate3Count;
    }

    public void setRate3Count(String rate3Count) {
        this.rate3Count = rate3Count;
    }

    public String getRate4Count() {
        return rate4Count;
    }

    public void setRate4Count(String rate4Count) {
        this.rate4Count = rate4Count;
    }

    public String getRate5Count() {
        return rate5Count;
    }

    public void setRate5Count(String rate5Count) {
        this.rate5Count = rate5Count;
    }

    public String getInstallCountRange() {
        return installCountRange;
    }

    public void setInstallCountRange(String installCountRange) {
        this.installCountRange = installCountRange;
    }

    public Price getPrices() {
        return prices;
    }

    public void setPrices(Price prices) {
        this.prices = prices;
    }

    public Stats getStats() {
        return stats;
    }

    public void setStats(Stats stats) {
        this.stats = stats;
    }

    public String getPublishedStatus() {
        return publishedStatus;
    }

    public void setPublishedStatus(String publishedStatus) {
        this.publishedStatus = publishedStatus;
    }
}
