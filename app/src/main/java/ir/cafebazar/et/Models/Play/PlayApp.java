package ir.cafebazar.et.Models.Play;



import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;


@Entity
public class PlayApp {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    @SerializedName("id")
    private String id;

    @ColumnInfo(name = "rank")
    @SerializedName("rank")
    private String rank;

    @ColumnInfo(name = "packagename")
    @SerializedName("packagename")
    private String packagename;


    @ColumnInfo(name = "icon")
    @SerializedName("icon")
    private String icon;

    @ColumnInfo(name = "developer")
    @SerializedName("developer")
    private String developer;

    @ColumnInfo(name = "category")
    @SerializedName("category")
    private String category;

    @ColumnInfo(name = "installs")
    @SerializedName("installs")
    private String installs;

    @ColumnInfo(name = "rankfilter")
    @SerializedName("rankfilter")
    private String rankfilter;

    @ColumnInfo(name = "rankcat")
    @SerializedName("rankcat")
    private String rankcat;

    @ColumnInfo(name = "name")
    @SerializedName("name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getPackagename() {
        return packagename;
    }

    public void setPackagename(String packagename) {
        this.packagename = packagename;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getInstalls() {
        return installs;
    }

    public void setInstalls(String installs) {
        this.installs = installs;
    }

    public String getRankfilter() {
        return rankfilter;
    }

    public void setRankfilter(String rankfilter) {
        this.rankfilter = rankfilter;
    }

    public String getRankcat() {
        return rankcat;
    }

    public void setRankcat(String rankcat) {
        this.rankcat = rankcat;
    }
}
