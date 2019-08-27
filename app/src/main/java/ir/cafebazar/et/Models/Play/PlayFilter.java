package ir.cafebazar.et.Models.Play;

import com.google.gson.annotations.SerializedName;

public class PlayFilter{

    @SerializedName("id")
     private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("filtercode")
    private String filtercode;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFiltercode() {
        return filtercode;
    }

    public void setFiltercode(String filtercode) {
        this.filtercode = filtercode;
    }
}
