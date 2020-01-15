package ir.cafebazar.et.bazar.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AppRow {


    private int pos;
    public boolean isAdd;

    @SerializedName("type")
    private String type;


    @SerializedName("title")
    private String title;

    @SerializedName("more")
    private String more;


    @SerializedName("hasMore")
    private boolean hasMore;

    @SerializedName("content")
    private ArrayList<App> content;

    public AppRow() {
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public ArrayList<App> getContent() {
        return content;
    }

    public void setContent(ArrayList<App> content) {
        this.content = content;
    }
}
